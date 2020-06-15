package pl.nataliana.githubfinder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.nataliana.githubfinder.model.RepositoryCommits
import pl.nataliana.githubfinder.model.RepositoryCommitsItem

@BindingAdapter("textViewName")
fun TextView.setTextViewName(userLogin: String?) {
    userLogin?.let {
        text = userLogin
    }
}

@BindingAdapter("textViewRepo")
fun TextView.setTextViewRepo(repoName: String?) {
    repoName?.let {
        text = repoName
    }
}

@BindingAdapter("textViewNameDetail")
fun TextView.setTextViewNameDetail(userLogin: String?) {
    userLogin?.let {
        text = userLogin
    }
}

@BindingAdapter("textViewRepoDetail")
fun TextView.setTextViewRepoDetail(repoName: String?) {
    repoName?.let {
        text = repoName
    }
}

@BindingAdapter("commitMessage")
fun TextView.setCommitMessage(item: RepositoryCommitsItem?) {
    item?.let {
        text = item.commit.message
    }
}

@BindingAdapter("commitSha")
fun TextView.setCommitSha(item: RepositoryCommitsItem?) {
    item?.let {
        text = item.commit.sha
    }
}

@BindingAdapter("commitAuthor")
fun TextView.setCommitAuthor(item: RepositoryCommitsItem?) {
    item?.let {
        text = item.commit.author.name
    }
}