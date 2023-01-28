package abbosbek.mobiler.retrofit

import abbosbek.mobiler.retrofit.api.ApiClient
import abbosbek.mobiler.retrofit.api.ApiService
import abbosbek.mobiler.retrofit.databinding.ActivityDetailMoviesBinding
import abbosbek.mobiler.retrofit.response.DetailMovieResponse
import abbosbek.mobiler.retrofit.utils.Constants
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMoviesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailMoviesBinding

    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("id",1)

        binding.apply {

            prgBarMovies.isVisible = true
            val callDetailMovieApi = api.getMovieDetail(movieId)

            callDetailMovieApi.enqueue(object : Callback<DetailMovieResponse>{
                override fun onResponse(
                    call: Call<DetailMovieResponse>,
                    response: Response<DetailMovieResponse>
                ) {
                    prgBarMovies.isVisible = false
                    when(response.code()){
                        //Successful response
                        in 200..299 -> {
                            response.body().let {itBody->
                                val imagePoster = Constants.POSTER_BASE_URL + itBody!!.poster_path
                                imgMovie.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }

                                imgMovieBack.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }

                                tvMovieTitle.text = itBody.title
                                tvMovieTagLine.text = itBody.tagline
                                tvMovieDateRelease.text = itBody.release_date
                                tvMovieRating.text = itBody.vote_average.toString()
                                tvMovieRuntime.text = itBody.runtime.toString()
                                tvMovieBudget.text = itBody.budget.toString()
                                tvMovieRevenue.text = itBody.revenue.toString()
                                tvMovieOverview.text = itBody.overview

                            }
                        }
                        //Redirection message
                        in 300..399 ->{
                            Log.d("Response Code", "onResponse: ${response.code()}")
                        }
                        //Client error response
                        in 400..499 ->{
                            Log.d("Response Code", "onResponse: ${response.code()}")
                        }
                        //server error message
                        in 500..599 ->{
                            Log.d("Response Code", "onResponse: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
                    prgBarMovies.isVisible = false
                    Toast.makeText(
                        this@DetailMoviesActivity,
                        t.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

        }

    }
}