package pl.nataliana.githubfinder.model

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job

class RepositoryListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    val githubRepository: ObservableField<GithubRepository> = ObservableField()

    private val _navigateToRepoDetail = MutableLiveData<GithubRepository>()
    val navigateToRepoDetail: LiveData<GithubRepository>
        get() = _navigateToRepoDetail

    private val _navigateToMainFragment = MutableLiveData<Long>()
    val navigateToMainFragment
        get() = _navigateToMainFragment

    fun onRepoDetailNavigated() {
        _navigateToRepoDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}