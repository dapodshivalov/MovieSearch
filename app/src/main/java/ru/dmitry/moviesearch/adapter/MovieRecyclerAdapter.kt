package ru.dmitry.moviesearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_movie_list_item.view.*
import ru.dmitry.moviesearch.R
import ru.dmitry.moviesearch.model.MovieRecyclerViewItemData

class MovieRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<MovieRecyclerViewItemData>()
    private var itemsCopy = mutableListOf<MovieRecyclerViewItemData>()

    var onLikeListener: OnLikeListener? = null

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
                holder.bind(items[position], onLikeListener)
            }
        }
    }

    fun submitList(movieList: List<MovieRecyclerViewItemData>) {
        items = movieList as MutableList<MovieRecyclerViewItemData>
        itemsCopy = items.toMutableList()
    }

    class MovieViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val posterImage = itemView.movie_poster_image
        val movieTitle = itemView.movie_title
        val movieYear = itemView.movie_year
        val bookmarkImage = itemView.iv_bookmark
        val likeImage = itemView.iv_like

        fun bind(item: MovieRecyclerViewItemData, onLikeListener: OnLikeListener?) {
            movieTitle.text = item.movie.title
            movieYear.text = item.movie.release_date

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load("https://image.tmdb.org/t/p/w500${item.movie.poster_path}")
                .into(posterImage)

            bookmarkImage.isActivated = item.bookmarked
            likeImage.isActivated = item.liked

            bookmarkImage.setOnClickListener {
                it.isActivated = !it.isActivated
                onLikeListener?.onBookmarkClicked(item.movie.id, it.isActivated)
            }

            likeImage.setOnClickListener {
                it.isActivated = !it.isActivated
                onLikeListener?.onLikeClicked(item.movie.id, it.isActivated)
            }
        }
    }
}

interface OnLikeListener {
    fun onLikeClicked(movieId: Int, isLike: Boolean)
    fun onBookmarkClicked(movieId: Int, isLike: Boolean)
}