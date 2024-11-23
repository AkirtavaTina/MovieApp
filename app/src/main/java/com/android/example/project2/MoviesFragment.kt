package com.android.example.project2
import MoviesAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.project2.databinding.FragmentMoviesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var movies: RecyclerView
    private var moviesAdapter: MoviesAdapter = MoviesAdapter()
    private val moviesViewModel: MoviesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movies = binding.movies
        movies.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        movies.adapter = moviesAdapter


        if(isOnline()) {
            Log.d("movie", "1")
            moviesViewModel.moviesList.observe(viewLifecycleOwner) {
                moviesAdapter.submitData(lifecycle, it)
            }
        }else {
            Log.d("movie", "2")
            moviesViewModel.allMovies.observe(viewLifecycleOwner) { movies ->
                if (movies.isNotEmpty()) {
                    moviesAdapter.submitData(lifecycle, PagingData.from(movies))
                }
            }
        }
    }

    private fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return (exitValue == 0)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}