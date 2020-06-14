package pl.nataliana.githubfinder.model

import com.google.gson.annotations.SerializedName

data class GetRepositoryCommitResponse(
    @SerializedName("total_count") var totalCount: Long,
    @SerializedName("items") var items: List<RepositoryCommitsItem>
)