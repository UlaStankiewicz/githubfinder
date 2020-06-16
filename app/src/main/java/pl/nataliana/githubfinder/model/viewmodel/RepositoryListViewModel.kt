package pl.nataliana.githubfinder.model.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.Owner

class RepositoryListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    val githubRepository: ObservableField<GithubRepository> = ObservableField()
    private var cachedRepositories: LiveData<List<GithubRepository>>? = null

    private val _navigateToRepoDetail = MutableLiveData<GithubRepository>()
    val navigateToRepoDetail: LiveData<GithubRepository>
        get() = _navigateToRepoDetail

    private val _navigateToMainFragment = MutableLiveData<Long>()
    val navigateToMainFragment
        get() = _navigateToMainFragment

    // TODO method to update searched repositories list
    fun getCachedRepositories(): LiveData<List<GithubRepository>>? {
        return cachedRepositories
    }

    fun onRepoDetailNavigated() {
        _navigateToRepoDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}