package pl.nataliana.githubfinder.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.nataliana.githubfinder.model.GetRepositoryCommitResponse
import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.service.base.Resource
import retrofit2.Response

class GithubFinderApiClientImpl(private val githubFinderApi: GithubFinderApi) :
    GithubFinderApiClient {

    override suspend fun getRepository(
        user: String,
        repo: String
    ): Resource<GithubRepository> = withContext(Dispatchers.IO) {
        try {
            val response: Response<GithubRepository> = githubFinderApi.getRepository(user, repo)
            if (response.isSuccessful) {
                Resource.success(response.body())

            } else {
                Resource.error(response.message())
            }
        } catch (ex: Throwable) {
            Resource.error<GithubRepository>("${ex.message}")
        }
    }

    override suspend fun getCommitsInRepository(
        user: String,
        repo: String,
        sort: String
    ): Resource<GetRepositoryCommitResponse> = withContext(Dispatchers.IO) {
        try {
            val response: Response<GetRepositoryCommitResponse> =
                githubFinderApi.getCommitsInRepository(user, repo, sort)
            if (response.isSuccessful) {
                Resource.success(response.body())

            } else {
                Resource.error(response.message())
            }
        } catch (ex: Throwable) {
            Resource.error<GetRepositoryCommitResponse>("${ex.message}")
        }
    }
}