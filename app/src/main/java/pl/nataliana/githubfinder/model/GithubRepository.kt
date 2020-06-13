package pl.nataliana.githubfinder.model

import com.google.gson.annotations.SerializedName

data class GithubRepository(
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("name")
    val repoName: String,
    @SerializedName("id")
    val id: Int
)

data class Owner(
    @SerializedName("login")
    val userLogin: String
)