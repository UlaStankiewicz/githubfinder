package pl.nataliana.githubfinder

import android.widget.TextView
import androidx.databinding.BindingAdapter

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
fun TextView.setCommitMessage(commitMessage: String?) {
    commitMessage?.let {
        text = commitMessage
    }
}

@BindingAdapter("commitSha")
fun TextView.setCommitSha(commitSha: String?) {
    commitSha?.let {
        text = commitSha
    }
}

@BindingAdapter("commitAuthor")
fun TextView.setCommitAuthor(commitAuthor: String?) {
    commitAuthor?.let {
        text = commitAuthor
    }
}