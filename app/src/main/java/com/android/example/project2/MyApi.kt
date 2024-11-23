package com.android.example.project2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {

    @GET("discover/tv")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = "f01ae87f32faec4d78371bb9347513bf",
        @Query("page") page: Int
    ): Response<GetMoviesResponse>
}