package pl.nataliana.githubfinder.model.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import pl.nataliana.githubfinder.model.GetRepositoryCommitResponse

class RepositoryDetailViewModel(
    private val userLogin: String = "",
    private val repoName: String = ""
) : ViewModel() {

    private val viewModelJob = Job()
    val repositoryCommits: ObservableField<GetRepositoryCommitResponse> = ObservableField()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}