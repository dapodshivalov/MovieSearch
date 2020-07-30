package ru.dmitry.moviesearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_movie_list_item.view.*
import ru.dmitry.moviesearch.R
import ru.dmitry.moviesearch.model.Movie
import java.util.*

class MovieRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<Movie>()
    private var itemsCopy = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun submitList(movieList: List<Movie>) {
        items = movieList as MutableList<Movie>
        itemsCopy = items.toMutableList()
    }

    class MovieViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val posterImage = itemView.movie_poster_image
        val movieTitle = itemView.movie_title
        val movieYear = itemView.movie_year

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            movieYear.text = movie.year.toString()

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(movie.posterUrl)
                .into(posterImage)
        }
    }


    fun filter(text: String) {
        var text = text
        items.clear()
        if (text.isEmpty()) {
            items.addAll(0, itemsCopy)
        } else {
            text = text.toLowerCase()
            for (item in itemsCopy) {
                if (item.title.toLowerCase().contains(text)) {
                    items.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}