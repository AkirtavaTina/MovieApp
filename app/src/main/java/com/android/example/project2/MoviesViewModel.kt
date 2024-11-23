package com.android.example.project2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch


class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {
    val allMovies: LiveData<List<Movies>> get() = repository.allMovies.asLiveData()
    private val _movies = MutableLiveData<List<Movies>>()
    val moviesList: LiveData<PagingData<Movies>> = repository.getMoviesStream().cachedIn(viewModelScope)

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            repository.getMovies(

                onSuccess = { movies ->
                    _movies.postValue((movies))
                },
                onError = {
                    Log.e("MoviesViewModel", "Error fetching movies")
                }

            )
        }
    }
}