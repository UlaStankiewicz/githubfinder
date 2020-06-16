package pl.nataliana.githubfinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import pl.nataliana.githubfinder.R
import pl.nataliana.githubfinder.adapter.GithubRepositoryAdapter
import pl.nataliana.githubfinder.adapter.RepositoryListener
import pl.nataliana.githubfinder.databinding.FragmentMainBinding
import pl.nataliana.githubfinder.gone
import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.Owner
import pl.nataliana.githubfinder.model.viewmodel.RepositoryListViewModel
import pl.nataliana.githubfinder.model.viewmodel.RepositoryListViewModelFactory
import pl.nataliana.githubfinder.toast
import pl.nataliana.githubfinder.visible

class MainFragment : Fragment() {

    private lateinit var mainAdapter: GithubRepositoryAdapter
    private val repoViewModel: RepositoryListViewModel by inject()
    // TODO this is a variable which will hold cached list
    private var cachedRepositoryList: List<GithubRepository> =
        mutableListOf(
            GithubRepository(Owner("natansalda"), "mywine", 123233),
            GithubRepository(Owner("bright"), "shouldko", 122311654)
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            RepositoryListViewModelFactory(
                application
            )

        val repositoryListViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(RepositoryListViewModel::class.java)

        binding.lifecycleOwner = this
        binding.repositoryListViewModel = repositoryListViewModel
        binding.searchForRepo.setOnClickListener {
            val userInput = binding.userInputRepo.text.toString().trim()
            val userInputAfterSplit = userInput.split("/").toTypedArray()
            val searchedUser = userInputAfterSplit.first()
            val searchedRepo = userInputAfterSplit.last()

            if (userInput.contains(SLASH)) {
                setSearchButton(searchedUser, searchedRepo)
            } else {
                requireActivity().toast(getString(R.string.toast_no_slash_in_search))
            }
        }

        mainAdapter = GithubRepositoryAdapter(RepositoryListener { userLogin, repoName ->
            setClick(userLogin, repoName)
        })
        showListOfCachedRepos()
        binding.recyclerView.adapter = mainAdapter

        checkIfRecyclerViewIsEmpty(binding, cachedRepositoryList)
        activity?.title = getString(R.string.app_name_title)

        return binding.root
    }

    private fun showListOfCachedRepos() {
        repoViewModel.getCachedRepositories()?.observe(this,
            Observer<List<GithubRepository>> { cachedRepositoryList ->
                cachedRepositoryList?.let {
                    mainAdapter.submitList(it)
                }
            })
    }

    private fun setSearchButton(userLogin: String, repoName: String) {

        view?.findNavController()?.navigate(
            MainFragmentDirections
                .actionMainFragmentToDetailFragment(userLogin, repoName)
        )
        repoViewModel.onRepoDetailNavigated()
    }

    private fun checkIfRecyclerViewIsEmpty(binding: FragmentMainBinding, list: List<GithubRepository>) {
        if (list.isNotEmpty()) {
            binding.emptyViewLayout.gone()
            binding.previouslySearchedTextView.visible()
        }
    }

    private fun setClick(name: String, repo: String) {
        view?.findNavController()?.navigate(
            MainFragmentDirections
                .actionMainFragmentToDetailFragment(name, repo)
        )
        repoViewModel.onRepoDetailNavigated()
    }

    companion object {
        const val SLASH = "/"
    }
}
