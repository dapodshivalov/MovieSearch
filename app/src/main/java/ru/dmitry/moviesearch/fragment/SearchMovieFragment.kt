package ru.dmitry.moviesearch.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dmitry.moviesearch.BuildConfig
import ru.dmitry.moviesearch.R
import ru.dmitry.moviesearch.activity.MainActivity
import ru.dmitry.moviesearch.adapter.MovieRecyclerAdapter
import ru.dmitry.moviesearch.adapter.OnLikeListener
import ru.dmitry.moviesearch.model.UserMovieRelation
import ru.dmitry.moviesearch.model.MovieBrief
import ru.dmitry.moviesearch.model.MovieRecyclerViewItemData
import ru.dmitry.moviesearch.model.PageResponse
import ru.dmitry.moviesearch.service.Client
import ru.dmitry.moviesearch.service.Service


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchMovieFragment : Fragment(), OnLikeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = "SearchMovieFragment"

    private lateinit var movieAdapter: MovieRecyclerAdapter

    private lateinit var movieService: Service

    private var currentPage = 1

    private var likes = mutableSetOf<Int>()
    private var bookmarks = mutableSetOf<Int>()

    private var items = listOf<MovieRecyclerViewItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieService = Client.client?.create(Service::class.java)!!
        initRecyclerView()
        loadUserLikes()
        loadUserBookmarks()
        loadNextPage()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchMovieFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.orEmpty().isEmpty()) {
                    loadNextPage()
                } else {
                    searchMovie(query!!)
                }
//                movieAdapter.filter(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.orEmpty().isEmpty()) {
                    loadNextPage()
                } else {
                    searchMovie(newText!!)
                }
//                movieAdapter.filter(newText.orEmpty())
                return true
            }

        })
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SearchMovieFragment.context)
            movieAdapter = MovieRecyclerAdapter()
            movieAdapter.onLikeListener = this@SearchMovieFragment
//            val topSpacingDecorator = TopSpacingDecorator(30)
//            addItemDecoration(topSpacingDecorator)
            adapter = movieAdapter
        }
    }

    private fun loadUserLikes() {
        val userId = MainActivity.mFirebaseUser!!.uid
        MainActivity.mLikeDatabaseReference!!.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var umr: UserMovieRelation
                    for (ds in dataSnapshot.children) {
                        umr = ds.getValue(UserMovieRelation::class.java) as UserMovieRelation
                        likes.add(umr.movieId!!.toInt())
                    }
                    items = items.map { MovieRecyclerViewItemData(it.movie, likes.contains(it.movie.id), it.bookmarked) }
                    movieAdapter.submitList(items)
                    movieAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    private fun loadUserBookmarks() {
        val userId = MainActivity.mFirebaseUser!!.uid
        MainActivity.mBookmarkDatabaseReference!!.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var umr: UserMovieRelation
                    for (ds in dataSnapshot.children) {
                        umr = ds.getValue(UserMovieRelation::class.java) as UserMovieRelation
                        bookmarks.add(umr.movieId!!.toInt())
                    }
                    items = items.map { MovieRecyclerViewItemData(it.movie, it.liked, bookmarks.contains(it.movie.id)) }
                    movieAdapter.submitList(items)
                    movieAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun loadNextPage() {
        Log.d(TAG, "loadNextPage: $currentPage")

        callPopularMovie().enqueue(object : Callback<PageResponse<MovieBrief>> {
            override fun onResponse(
                call: Call<PageResponse<MovieBrief>?>?,
                response: Response<PageResponse<MovieBrief>?>?
            ) {
                val results: List<MovieBrief> = response?.body()?.results ?: emptyList()
                items = results.map { MovieRecyclerViewItemData(it, likes.contains(it.id)) }
                movieAdapter.submitList(items)
                movieAdapter.notifyDataSetChanged()
            }

            override fun onFailure(
                call: Call<PageResponse<MovieBrief>>?,
                t: Throwable
            ) {
                t.printStackTrace()
                // TODO: 08/11/16 handle failure
            }
        })
    }

    fun searchMovie(query: String) {
        Log.d(TAG, "loadNextPage: $currentPage")

        callSearchMovie(query).enqueue(object : Callback<PageResponse<MovieBrief>> {
            override fun onResponse(
                call: Call<PageResponse<MovieBrief>?>?,
                response: Response<PageResponse<MovieBrief>?>?
            ) {
                val results: List<MovieBrief> = response?.body()?.results ?: emptyList()
                items = results.map { MovieRecyclerViewItemData(it, likes.contains(it.id)) }
                movieAdapter.submitList(items)
                movieAdapter.notifyDataSetChanged()
            }

            override fun onFailure(
                call: Call<PageResponse<MovieBrief>>?,
                t: Throwable
            ) {
                t.printStackTrace()
                // TODO: 08/11/16 handle failure
            }
        })
    }

    private fun callPopularMovie(): Call<PageResponse<MovieBrief>> {
        return movieService.getPopularMovies(
            apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN,
            pageIndex = 1
        )
    }

    private fun callSearchMovie(query: String): Call<PageResponse<MovieBrief>> {
        return movieService.searchMovie(
            apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN,
            pageIndex = 1,
            query = query
        )
    }

    override fun onLikeClicked(movieId: Int, isLike: Boolean) {
        val userId = MainActivity.mFirebaseUser!!.uid

        MainActivity.mLikeDatabaseReference!!.orderByChild("movieId").equalTo(movieId.toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var umr: UserMovieRelation
                    var exists = false
                    for (ds in dataSnapshot.children) {
                        umr = ds.getValue(UserMovieRelation::class.java) as UserMovieRelation
                        if (umr.userId == userId) {
                            exists = true
                            ds.ref.removeValue()
                            break
                        }
                    }
                    if (!exists) {
                        likes.add(movieId)
                        MainActivity.mLikeDatabaseReference!!.push()
                            .setValue(UserMovieRelation(userId, movieId.toString()))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    override fun onBookmarkClicked(movieId: Int, isLike: Boolean) {
        val userId = MainActivity.mFirebaseUser!!.uid

        MainActivity.mBookmarkDatabaseReference!!.orderByChild("movieId").equalTo(movieId.toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var umr: UserMovieRelation
                    var exists = false
                    for (ds in dataSnapshot.children) {
                        umr = ds.getValue(UserMovieRelation::class.java) as UserMovieRelation
                        if (umr.userId == userId) {
                            exists = true
                            ds.ref.removeValue()
                            break
                        }
                    }
                    if (!exists) {
                        MainActivity.mBookmarkDatabaseReference!!.push()
                            .setValue(UserMovieRelation(userId, movieId.toString()))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })    }


}