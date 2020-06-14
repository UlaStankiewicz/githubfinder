package pl.nataliana.githubfinder.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import pl.nataliana.githubfinder.BuildConfig
import pl.nataliana.githubfinder.service.GithubFinderApi
import pl.nataliana.githubfinder.service.GithubFinderApiClient
import pl.nataliana.githubfinder.service.GithubFinderApiClientImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val TIME_OUT: Long = 30
const val BASE_URL: String = "https://api.github.com/"

val githubFinderApiModule = module {
    single { setupRetrofit(get(), get()) }
    factory { setupGson() }
    factory { setupOkHttpClient(get()) }
    factory { setupGithubFinderApi(get()) }
    factory { setupHttpLoggingInterceptor() }
}

val githubFinderApiClientModule = module {
    single<GithubFinderApiClient> { GithubFinderApiClientImpl(get()) }
}

fun setupGithubFinderApi(retrofit: Retrofit): GithubFinderApi =
    retrofit.create(GithubFinderApi::class.java)

fun setupRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun setupGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setLenient()
        .create()
}


fun setupHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.tag("GithubFinderApi").i(message)
        }
    })
    interceptor.level = HttpLoggingInterceptor.Level.BASIC
    return interceptor
}

fun setupOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()

    builder
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            chain.proceed(requestBuilder.build())
        }
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(httpLoggingInterceptor)
    }

    return builder.build()
}