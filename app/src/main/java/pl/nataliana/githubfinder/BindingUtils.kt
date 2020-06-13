package pl.nataliana.githubfinder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.nataliana.githubfinder.model.GithubRepository

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