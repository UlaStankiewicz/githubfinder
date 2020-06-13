package pl.nataliana.githubfinder.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

class RepositoryDetailViewModel(
    private val userLogin: String = "",
    private val repoName: String = ""
) : ViewModel() {

    private val viewModelJob = Job()
    val repositoryCommits: ObservableField<List<RepositoryCommits>> = ObservableField()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}