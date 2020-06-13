package pl.nataliana.githubfinder.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.nataliana.githubfinder.model.RepositoryDetailViewModel
import pl.nataliana.githubfinder.model.RepositoryListViewModel

val uiModule = module {
    viewModel { RepositoryListViewModel(get()) }
}

val uiDetailModule = module {
    viewModel { RepositoryDetailViewModel(get(), get()) }
}