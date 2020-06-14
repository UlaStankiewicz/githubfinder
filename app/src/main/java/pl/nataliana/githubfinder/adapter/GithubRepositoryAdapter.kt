package pl.nataliana.githubfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.nataliana.githubfinder.databinding.RepoItemBinding
import pl.nataliana.githubfinder.model.GithubRepository

class GithubRepositoryAdapter(private val clickListener: RepositoryListener) :
    ListAdapter<GithubRepository, GithubRepositoryAdapter.ViewHolder>(GithubRepositoryDiffCallback()) {

    private var mRepository: GithubRepository? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun updateGithubRepository(repository: GithubRepository?) {
        mRepository = repository
        notifyDataSetChanged()
    }

    class ViewHolder private constructor(private val binding: RepoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: RepositoryListener, item: GithubRepository) {
            binding.githubRepository = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RepoItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class GithubRepositoryDiffCallback :
    DiffUtil.ItemCallback<GithubRepository>() {
    override fun areItemsTheSame(oldItem: GithubRepository, newItem: GithubRepository): Boolean {
        return oldItem.repoName == newItem.repoName
    }

    override fun areContentsTheSame(oldItem: GithubRepository, newItem: GithubRepository): Boolean {
        return oldItem == newItem
    }
}

class RepositoryListener(val clickListener: (name: String, repo: String) -> Unit) {
    fun onClick(repository: GithubRepository) = clickListener(repository.owner.userLogin, repository.repoName)
}