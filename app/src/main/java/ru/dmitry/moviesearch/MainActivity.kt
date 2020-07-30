package ru.dmitry.moviesearch

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.dmitry.moviesearch.adapter.MovieRecyclerAdapter


class MainActivity : AppCompatActivity(){

    private lateinit var movieAdapter: MovieRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        addDataSet()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                movieAdapter.filter(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieAdapter.filter(newText.orEmpty())
                return true
            }

        })

        return true
    }

    private fun addDataSet() {
        val data = DataSource.createDataSet()
        movieAdapter.submitList(data)
    }

    fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            movieAdapter = MovieRecyclerAdapter()
//            val topSpacingDecorator = TopSpacingDecorator(30)
//            addItemDecoration(topSpacingDecorator)
            adapter = movieAdapter
        }
    }

}

