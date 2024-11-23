package com.android.example.project2
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch



class MoviesRepository (private val movieDao: MovieDao, private val api:MyApi){

    val allMovies: Flow<List<Movies>> = movieDao.getAllMovies()

    fun getMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movies>) -> Unit,
        onError: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getMovies(page = page)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        movieDao.addMovie(responseBody.movies)
                        onSuccess.invoke(responseBody.movies)
                    } else onError()
                }
                else {
                    onError()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError.invoke()
            }
        }
    }

    fun getMoviesStream():LiveData<PagingData<Movies>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 30),
        pagingSourceFactory = { MoviePagingSource(api) }
    ).liveData
}