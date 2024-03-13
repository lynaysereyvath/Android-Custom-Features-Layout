package com.lynaysereyvath.customfeatures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lynaysereyvath.customfeatures.databinding.RecyclerviewItemLayoutBinding

data class OptionModel(
    val id: Int,
    val name: String
)

interface IClickListener {
    fun onClicked(item: OptionModel)
}

class OptionsAdapter(private val listener: IClickListener): RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    private val mList = ArrayList<OptionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerviewItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mList[position], listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(private val binding: RecyclerviewItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(option: OptionModel, listener: IClickListener) {
            binding.root.setOnClickListener {
                listener.onClicked(option)
            }
            binding.textView.text = option.name
        }
    }
}