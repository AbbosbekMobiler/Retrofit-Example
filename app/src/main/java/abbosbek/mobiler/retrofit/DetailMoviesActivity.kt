package abbosbek.mobiler.retrofit

import abbosbek.mobiler.retrofit.databinding.ActivityDetailMoviesBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailMoviesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}