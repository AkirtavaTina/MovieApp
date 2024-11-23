package com.android.example.project2

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException

class MoviePagingSource(private val api: MyApi):
    PagingSource<Int, Movies>() {

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val page = params.key ?: 1
        return try {
            val response = api.getMovies(page = page)
            if (response.isSuccessful) {
                val movies = response.body()?.movies ?: emptyList()
                val nextPage = if (movies.isEmpty()) null else page + 1
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}