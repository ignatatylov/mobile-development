package com.example.lab23

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.util.Log
import com.example.lab23.databinding.ActivityMoviesListBinding

class MoviesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movies = loadMoviesFromAssets()
        Log.d("MoviesListActivity", "Movies loaded in onCreate: ${movies.size}")

        if (movies.isEmpty()) {
            Log.e("MoviesListActivity", "No movies found. Check JSON file in assets.")
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.addItemDecoration(Decoration(resources))
        binding.recyclerView.adapter = MoviesAdapter(movies) { movie ->
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra("movie", movie)
            startActivity(intent)
        }
    }


    private fun loadMoviesFromAssets(): List<Movie> {
        val movieList = mutableListOf<Movie>()
        try {
            assets.open("movies.json").use { inputStream ->
                val reader = InputStreamReader(inputStream)
                val type = object : TypeToken<List<Movie>>() {}.type
                movieList.addAll(Gson().fromJson(reader, type))
                Log.d("MoviesListActivity", "Successfully loaded movies: ${movieList.size} movies")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MoviesListActivity", "Error loading movies: ${e.message}")
        }
        return movieList
    }
}
