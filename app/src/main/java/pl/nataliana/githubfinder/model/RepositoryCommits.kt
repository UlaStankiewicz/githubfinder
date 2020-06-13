package pl.nataliana.githubfinder.model

import com.google.gson.annotations.SerializedName

data class RepositoryCommits(
    @SerializedName("commit")
    val commit: Commit
)

data class Commit(
    @SerializedName("author")
    val author: Author,
    @SerializedName("message")
    val message: String,
    @SerializedName("sha")
    val sha: String
)

data class Author(
    @SerializedName("name")
    val name: String
)