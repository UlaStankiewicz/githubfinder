package pl.nataliana.githubfinder.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.nataliana.githubfinder.R
import pl.nataliana.githubfinder.adapter.GithubRepositoryDetailAdapter
import pl.nataliana.githubfinder.databinding.FragmentDetailBinding
import pl.nataliana.githubfinder.model.RepositoryDetailViewModel
import pl.nataliana.githubfinder.model.RepositoryDetailViewModelFactory
import pl.nataliana.githubfinder.service.GithubFinderApiClient
import pl.nataliana.githubfinder.service.base.Status
import timber.log.Timber

class DetailFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val loadStateLiveData: MutableLiveData<Status> = MutableLiveData()
    private lateinit var repositoryDetailViewModel: RepositoryDetailViewModel
    private lateinit var detailAdapter: GithubRepositoryDetailAdapter
    private val githubFinderApiClient: GithubFinderApiClient by inject()
    private var userLogin: String = ""
    private var repoName: String = ""

    @SuppressLint("SetTextI18n")
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

        val viewModelFactory = RepositoryDetailViewModelFactory(userLogin, repoName)

        repositoryDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(RepositoryDetailViewModel::class.java)

        binding.repositoryDetailViewModel = repositoryDetailViewModel
        binding.lifecycleOwner = this

        detailAdapter = GithubRepositoryDetailAdapter()
        binding.detailRecyclerView.adapter = detailAdapter

        activity?.title = getString(R.string.repo_detail_fragment_title)

        loadRepository(userLogin, repoName)
        getRepositoryCommitDetails(userLogin, repoName)

        return binding.root
    }

    private fun loadRepository(userLogin: String, repoName: String) {
        Timber.i("Before coroutine launch")
        uiScope.launch {
            loadStateLiveData.postValue(Status.LOADING)

            Timber.i("Before: Repo details: $userLogin $repoName")

            val response = githubFinderApiClient.getRepository(userLogin, repoName)
            Timber.i("Response: $response")

            when (response.status) {
                Status.ERROR -> {
                    Timber.i("ERROR: Repo details: $userLogin $repoName")
                    loadStateLiveData.postValue(Status.ERROR)
                }
                Status.EMPTY -> {
                    Timber.i("EMPTY: Repo details: $userLogin $repoName")
                    loadStateLiveData.postValue(Status.EMPTY)
                }
                else -> {
                    response.data?.let {
                        Timber.i("SUCCESS: Repo details: $userLogin $repoName")
                        loadStateLiveData.postValue(Status.SUCCESS)
                    }
                }
            }
        }
    }

    private fun getRepositoryCommitDetails(userLogin: String, repoName: String) {
        repositoryDetailViewModel.viewModelScope.launch {
            val result = githubFinderApiClient.getCommitsInRepository(userLogin, repoName)
            if (result.status == Status.SUCCESS) {
                repositoryDetailViewModel.repositoryCommits.set(result.data)

            } else {
                repositoryDetailViewModel.repositoryCommits.set(null)
            }
        }
    }
}