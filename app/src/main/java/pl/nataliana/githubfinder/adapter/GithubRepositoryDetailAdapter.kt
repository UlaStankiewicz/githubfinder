package pl.nataliana.githubfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.nataliana.githubfinder.databinding.RepoDetailItemBinding
import pl.nataliana.githubfinder.model.RepositoryCommits

class GithubRepositoryDetailAdapter() :
    ListAdapter<RepositoryCommits, GithubRepositoryDetailAdapter.ViewHolder>(
        RepositoryCommitsDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: RepoDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepositoryCommits) {
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
    DiffUtil.ItemCallback<RepositoryCommits>() {
    override fun areItemsTheSame(oldItem: RepositoryCommits, newItem: RepositoryCommits): Boolean {
        return oldItem.commit == newItem.commit
    }

    override fun areContentsTheSame(oldItem: RepositoryCommits, newItem: RepositoryCommits): Boolean {
        return oldItem == newItem
    }
}