package pl.nataliana.githubfinder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.RepositoryCommits

@BindingAdapter("textViewName")
fun TextView.setTextViewName(item: GithubRepository?) {
    item?.let {
        text = item.owner.userLogin
    }
}

@BindingAdapter("textViewRepo")
fun TextView.setTextWineYear(item: GithubRepository?) {
    item?.let {
        text = item.repoName
    }
}

@BindingAdapter("textViewNameDetail")
fun TextView.setTextViewNameDetail(item: GithubRepository?) {
    item?.let {
        text = item.owner.userLogin
    }
}

@BindingAdapter("textViewRepoDetail")
fun TextView.setTextWineYearDetail(item: GithubRepository?) {
    item?.let {
        text = item.repoName
    }
}

@BindingAdapter("commitMessage")
fun TextView.setCommitMessage(item: RepositoryCommits?) {
    item?.let {
        text = item.commit.message
    }
}

@BindingAdapter("commitSha")
fun TextView.setCommitSha(item: RepositoryCommits?) {
    item?.let {
        text = item.commit.sha
    }
}

@BindingAdapter("commitAuthor")
fun TextView.setCommitAuthor(item: RepositoryCommits?) {
    item?.let {
        text = item.commit.author.name
    }
}