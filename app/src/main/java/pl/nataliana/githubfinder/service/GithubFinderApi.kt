package pl.nataliana.githubfinder.service

import pl.nataliana.githubfinder.model.GetRepositoryCommitResponse
import pl.nataliana.githubfinder.model.GithubRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubFinderApi {

    @GET("repos/{user}/{repo}")
    suspend fun getRepository(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): Response<GithubRepository>


    @GET("repos/{user}/{repo}/commits")
    suspend fun getCommitsInRepository(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): Response<GetRepositoryCommitResponse>
}