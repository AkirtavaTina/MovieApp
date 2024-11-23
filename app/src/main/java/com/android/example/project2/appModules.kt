package com.android.example.project2

import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val appModule = module {

    single{
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }


    single { get<Retrofit>().create(MyApi::class.java) }
    single {
        Room.databaseBuilder(
            androidApplication().applicationContext!!,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    single<MovieDao>{
        val database = get<MovieDatabase>()
        database.MovieDao()
    }

    single {
        MoviesRepository( get(),get())
    }

    viewModel{
        MoviesViewModel(get())
    }

}



