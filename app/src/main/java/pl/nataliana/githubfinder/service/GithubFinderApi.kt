package pl.nataliana.githubfinder.service

import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.RepositoryCommits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubFinderApi {

    @GET("repos/{user}/{repo}")
    suspend fun getRepository(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): Response<GithubRepository>


    @GET("repos/{user}/{repo}/commits")
    suspend fun getCommitsInRepository(
        @Path("user") user: String,
        @Path("repo") repo: String,
        @Query("sort") sort: String
    ): Response<RepositoryCommits>
}