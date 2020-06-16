package pl.nataliana.githubfinder.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.nataliana.githubfinder.R
import pl.nataliana.githubfinder.adapter.RepositoryCommitsAdapter
import pl.nataliana.githubfinder.adapter.RepositoryDetailListener
import pl.nataliana.githubfinder.databinding.FragmentDetailBinding
import pl.nataliana.githubfinder.gone
import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.RepositoryCommits
import pl.nataliana.githubfinder.model.viewmodel.RepositoryDetailViewModel
import pl.nataliana.githubfinder.model.viewmodel.RepositoryDetailViewModelFactory
import pl.nataliana.githubfinder.service.GithubFinderApiClient
import pl.nataliana.githubfinder.service.base.Resource
import pl.nataliana.githubfinder.service.base.Status
import pl.nataliana.githubfinder.visible
import timber.log.Timber

class DetailFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var repositoryDetailViewModel: RepositoryDetailViewModel
    private lateinit var commitsAdapter: RepositoryCommitsAdapter
    private val githubFinderApiClient: GithubFinderApiClient by inject()
    private var userLogin: String = ""
    private var repoName: String = ""
    private lateinit var repositoryResponse: Resource<GithubRepository>
    private lateinit var commitsResponse: Resource<RepositoryCommits>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false
            )

        val arguments = DetailFragmentArgs.fromBundle(requireArguments())
        userLogin = arguments.userLogin
        repoName = arguments.repoName

        val viewModelFactory =
            RepositoryDetailViewModelFactory(
                userLogin,
                repoName
            )

        loadRepository(binding)

        repositoryDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(RepositoryDetailViewModel::class.java)

        binding.repositoryDetailViewModel = repositoryDetailViewModel
        binding.lifecycleOwner = this

        commitsAdapter =
            RepositoryCommitsAdapter(RepositoryDetailListener { sha ->
                setClick(sha)
            })
        binding.detailRecyclerView.adapter = commitsAdapter

        activity?.title = getString(R.string.repo_detail_fragment_title)

        binding.shareRepo.setOnClickListener {
            shareRepositoryCommits()
        }

        return binding.root
    }

    private fun shareRepositoryCommits() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            val repositoryData =
                "Owner: ${repositoryResponse.data?.owner?.userLogin}, repository: ${repositoryResponse.data?.repoName}"
            putExtra(Intent.EXTRA_TEXT, "$repositoryData \n\n $commitsResponse")
            type = "text/json"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun loadRepository(binding: FragmentDetailBinding) {
        uiScope.launch {
            val response = githubFinderApiClient.getRepository(userLogin, repoName)
            Timber.i("Response: $response")

            when (response.status) {
                Status.ERROR -> {
                    Timber.i("ERROR: Repo details: $userLogin $repoName")
                    setNoSuchRepoText(binding)
                }
                Status.EMPTY -> {
                    Timber.i("EMPTY: Repo details: $userLogin $repoName")
                    setNoSuchRepoText(binding)
                }
                else -> {
                    response.data?.let {
                        Timber.i("SUCCESS: Repo details: $userLogin $repoName")
                        binding.textViewNameDetail.text = response.data.owner.userLogin
                        binding.textViewRepoDetail.text = response.data.repoName
                        repositoryResponse = response
                        getRepositoryCommitDetails(binding)
                        binding.shareRepo.visible()
                    }
                }
            }
        }
    }

    private fun getRepositoryCommitDetails(binding: FragmentDetailBinding) {
        uiScope.launch {

            val response = githubFinderApiClient.getCommitsInRepository(
                userLogin, repoName, "desc"
            )
            Timber.i("Response: $response")

            response.data?.let {
                when (response.status) {
                    Status.SUCCESS -> {
                        for (commit in 0..it.size) {
                            // TODO make binding working
//                            binding.detailRecyclerView.commit_message.text =
//                                it[commit].commit.message
//                            binding.detailRecyclerView.commit_sha.text = it[commit].sha
//                            binding.detailRecyclerView.commit_author.text =
//                                it[commit].commit.author.name
                        }
                        repositoryDetailViewModel.repositoryCommits.observe(
                            viewLifecycleOwner,
                            Observer { response ->
                                commitsAdapter.updateList(response)
                            })
                        binding.detailRecyclerView.adapter = commitsAdapter
                        commitsResponse = response
                    }
                    else -> {
                        Timber.i("ERROR: Couldn't find repository commits: $response")
                    }
                }
            }
        }
        binding.shareRepo.setOnClickListener {
            shareRepositoryCommits()
        }
    }

    private fun setNoSuchRepoText(binding: FragmentDetailBinding) {
        binding.textViewNameDetail.text = getString(R.string.no_such_repository)
        binding.textViewRepoDetail.gone()
        binding.textViewSlash.gone()
    }

    private fun setClick(sha: String) {
        // TODO implement logic to allows select particular commit to share with share button
    }
}