package pl.nataliana.githubfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.experimental.property.inject
import pl.nataliana.githubfinder.databinding.RepoDetailItemBinding
import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.RepositoryCommitsItem
import pl.nataliana.githubfinder.model.viewmodel.RepositoryDetailViewModel

class GithubRepositoryDetailAdapter(private val commitClickListener: RepositoryDetailListener)  :
    ListAdapter<RepositoryCommitsItem, GithubRepositoryDetailAdapter.ViewHolder>(
        RepositoryCommitsDiffCallback()
    ) {

    private var commitsList: List<RepositoryCommitsItem>? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun updateList(list: List<RepositoryCommitsItem>?) {
        commitsList = list
        notifyDataSetChanged()
    }

    class ViewHolder private constructor(private val binding: RepoDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepositoryCommitsItem) {
            binding.repositoryDetails = item
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RepoDetailItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class RepositoryCommitsDiffCallback :
    DiffUtil.ItemCallback<RepositoryCommitsItem>() {
    override fun areItemsTheSame(oldItem: RepositoryCommitsItem, newItem: RepositoryCommitsItem): Boolean {
        return oldItem.commit == newItem.commit
    }

    override fun areContentsTheSame(oldItem: RepositoryCommitsItem, newItem: RepositoryCommitsItem): Boolean {
        return oldItem == newItem
    }
}

class RepositoryDetailListener(val commitClickListener: (sha: String) -> Unit) {
    fun onClick(commit: RepositoryCommitsItem) = commitClickListener(commit.sha)
}