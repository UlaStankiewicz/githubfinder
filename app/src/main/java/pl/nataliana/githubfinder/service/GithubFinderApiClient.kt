package pl.nataliana.githubfinder.service

import pl.nataliana.githubfinder.model.GithubRepository
import pl.nataliana.githubfinder.model.RepositoryCommits
import pl.nataliana.githubfinder.service.base.Resource

interface GithubFinderApiClient {

    suspend fun getRepository(user: String, repo: String): Resource<GithubRepository>

    suspend fun getCommitsInRepository(user: String, repo: String): Resource<RepositoryCommits>

}