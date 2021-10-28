package com.example.flobizhackathon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flobizhackathon.R
import com.example.flobizhackathon.databinding.ItemBsBinding
import java.util.*

class TagsAdapter(val list: List<String>,val click: onClick,val clicked:String) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemBsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position),position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding:ItemBsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(tags:String, position: Int) {
            binding.tvTag.text= tags.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if((clicked.isEmpty() && position == 0) || tags == clicked)
                binding.tvTag.setBackgroundResource(R.color.light_purple)
           else
                binding.tvTag.setBackgroundResource(R.color.white)
            binding.tvTag.setOnClickListener {
                click.itemClicked(position)
            }
        }
    }

}