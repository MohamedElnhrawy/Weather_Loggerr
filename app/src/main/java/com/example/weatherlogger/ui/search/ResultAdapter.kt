package com.example.weatherlogger.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.databinding.ItemSearchResultBinding
import com.example.weatherlogger.ui.base.BaseViewHolder


class ResultAdapter(var playerList: List<SearchResult>,var mListener: ResultItemViewModel.ResultItemViewModelListener) : RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val mMovieViewBinding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return PlayerViewHolder(mMovieViewBinding)

    }

    fun updateList(playerList: List<SearchResult>){
        this.playerList = playerList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = playerList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class PlayerViewHolder(val mViewBinding: ItemSearchResultBinding?) :
        BaseViewHolder(mViewBinding!!.root) {


        private lateinit var playerItemViewModel: ResultItemViewModel
        override fun onBind(position: Int) {
            val movie = playerList[position]
            playerItemViewModel = ResultItemViewModel(movie,mListener)
            mViewBinding!!.viewModel = playerItemViewModel
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mViewBinding!!.executePendingBindings()
        }
    }


}
