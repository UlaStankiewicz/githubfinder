package pl.nataliana.githubfinder.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import pl.nataliana.githubfinder.model.GetRepositoryCommitResponse
import pl.nataliana.githubfinder.model.GithubRepository

class RepositoryDetailViewModel(
    private val userLogin: String = "",
    private val repoName: String = ""
) : ViewModel() {

    private val viewModelJob = Job()

    private val _repositoryCommits = MutableLiveData<GetRepositoryCommitResponse>()
    val repositoryCommits: LiveData<GetRepositoryCommitResponse>
        get() = _repositoryCommits

    private val _githubRepository = MutableLiveData<GithubRepository>()
    val githubRepository: LiveData<GithubRepository>
        get() = _githubRepository

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}