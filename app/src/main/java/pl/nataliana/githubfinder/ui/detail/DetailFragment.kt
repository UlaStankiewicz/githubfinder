package pl.nataliana.githubfinder.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.nataliana.githubfinder.R
import pl.nataliana.githubfinder.adapter.GithubRepositoryAdapter
import pl.nataliana.githubfinder.adapter.GithubRepositoryDetailAdapter
import pl.nataliana.githubfinder.adapter.RepositoryListener
import pl.nataliana.githubfinder.databinding.FragmentDetailBinding
import pl.nataliana.githubfinder.model.viewmodel.RepositoryDetailViewModel
import pl.nataliana.githubfinder.model.viewmodel.RepositoryDetailViewModelFactory
import pl.nataliana.githubfinder.model.viewmodel.RepositoryListViewModel
import pl.nataliana.githubfinder.service.GithubFinderApiClient
import pl.nataliana.githubfinder.service.base.Status
import pl.nataliana.githubfinder.ui.main.MainFragmentDirections
import timber.log.Timber

class DetailFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var repositoryDetailViewModel: RepositoryDetailViewModel
    private lateinit var detailAdapter: GithubRepositoryDetailAdapter
    private lateinit var githubRepositoryAdapter: GithubRepositoryAdapter
    private val githubFinderApiClient: GithubFinderApiClient by inject()
    private val repoViewModel: RepositoryListViewModel by inject()
    private lateinit var binding: FragmentDetailBinding
    private var userLogin: String = ""
    private var repoName: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
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

        repositoryDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(RepositoryDetailViewModel::class.java)

        binding.repositoryDetailViewModel = repositoryDetailViewModel
        binding.lifecycleOwner = this

        detailAdapter = GithubRepositoryDetailAdapter()
        githubRepositoryAdapter =
            GithubRepositoryAdapter(RepositoryListener { userLogin, repoName ->
                setClick(userLogin, repoName)
            })
        binding.detailRecyclerView.adapter = detailAdapter

        activity?.title = getString(R.string.repo_detail_fragment_title)

        loadRepository()
        getRepositoryCommitDetails()

        return binding.root
    }

    private fun loadRepository() {

        uiScope.launch {

            val response = githubFinderApiClient.getRepository(userLogin, repoName)
            Timber.i("Response: $response")

            when (response.status) {
                Status.ERROR -> {
                    Timber.i("ERROR: Repo details: $userLogin $repoName")
                    setNoSuchRepoText()
                }
                Status.EMPTY -> {
                    Timber.i("EMPTY: Repo details: $userLogin $repoName")
                    setNoSuchRepoText()
                }
                else -> {
                    response.data?.let {
                        Timber.i("SUCCESS: Repo details: $userLogin $repoName")
                        repositoryDetailViewModel.githubRepository.observe(
                            viewLifecycleOwner,
                            Observer { response ->
                                githubRepositoryAdapter.updateGithubRepository(response)
                            })
                        binding.textViewNameDetail.text = response.data.owner.userLogin
                        binding.textViewRepoDetail.text = response.data.repoName
                    }
                }
            }
        }
    }

    private fun setNoSuchRepoText() {
        binding.textViewNameDetail.text = getString(R.string.no_such_repository)
        binding.textViewRepoDetail.text = ""
        binding.textViewSlash.text = ""
    }

    private fun getRepositoryCommitDetails() {
        repositoryDetailViewModel.viewModelScope.launch {
            val response = githubFinderApiClient.getCommitsInRepository(
                userLogin, repoName, "desc"
            )
            Timber.i("Response: $response")

            response.data?.let {
                when (response.status) {
                    Status.SUCCESS -> {
                        repositoryDetailViewModel.repositoryCommits.observe(
                            viewLifecycleOwner,
                            Observer { response ->
                                it.items.let {
                                    detailAdapter.updateList(response.items)
                                }
                            })
                    }
                    else -> {
                        Timber.i("ERROR: Couldn't find repository commits: $response")
                    }
                }
            }
        }
    }

    private fun setClick(name: String, repo: String) {
        view?.findNavController()?.navigate(
            MainFragmentDirections
                .actionMainFragmentToDetailFragment(name, repo)
        )
        repoViewModel.onRepoDetailNavigated()
    }
}