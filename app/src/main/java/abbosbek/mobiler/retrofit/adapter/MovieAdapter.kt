package abbosbek.mobiler.retrofit.adapter

import abbosbek.mobiler.retrofit.DetailMoviesActivity
import abbosbek.mobiler.retrofit.R
import abbosbek.mobiler.retrofit.databinding.ItemRowBinding
import abbosbek.mobiler.retrofit.response.MovieListResponse
import abbosbek.mobiler.retrofit.response.Result
import abbosbek.mobiler.retrofit.utils.Constants
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var context : Context

    inner class ViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : abbosbek.mobiler.retrofit.response.Result){
            binding.apply {
                tvMovieName.text = item.title
                tvRate.text = item.vote_average.toString()
                val moviePosterUrl = Constants.POSTER_BASE_URL + item.poster_path
                imgMovie.load(moviePosterUrl){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }

                tvLang.text = item.original_language
                tvDate.text = item.release_date

                root.setOnClickListener {
                    val intent = Intent(context,DetailMoviesActivity::class.java)
                    intent.putExtra("id",item.id)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context

        return ViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<abbosbek.mobiler.retrofit.response.Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

}