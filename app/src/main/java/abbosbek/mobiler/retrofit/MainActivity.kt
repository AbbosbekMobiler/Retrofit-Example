package abbosbek.mobiler.retrofit

import abbosbek.mobiler.retrofit.adapter.MovieAdapter
import abbosbek.mobiler.retrofit.api.ApiClient
import abbosbek.mobiler.retrofit.api.ApiService
import abbosbek.mobiler.retrofit.databinding.ActivityMainBinding
import abbosbek.mobiler.retrofit.response.MovieListResponse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val movieAdapter by lazy {
        MovieAdapter()
    }

    private val api by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            prgBarMovie.isVisible = true

            val callMovieApi = api.getPopularMovie(1)
            callMovieApi.enqueue(object : Callback<MovieListResponse>{
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    prgBarMovie.isVisible = false
                    when(response.code()){
                        //Successful response
                        in 200..299 -> {
                            response.body().let { itBody->
                                itBody?.results.let { itData->
                                    if (itData!!.isNotEmpty()){
                                        movieAdapter.differ.submitList(itData)
                                        rvMovie.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = movieAdapter
                                        }
                                    }

                                }

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

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    binding.prgBarMovie.isVisible = false
                    Log.e("onFailure", "onFailure: ${t.localizedMessage}")
                }
            })

        }
    }
}