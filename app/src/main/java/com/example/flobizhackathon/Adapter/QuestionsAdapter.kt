package com.example.flobizhackathon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flobizhackathon.R
import com.example.flobizhackathon.model.Items
import kotlinx.android.synthetic.main.item_count.view.*
import kotlinx.android.synthetic.main.item_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Filter
import java.util.logging.LogRecord

class QuestionsAdapter(private val list:List<Items?>,private val context:Context) :
    RecyclerView.Adapter<QuestionsAdapter.ViewHolder>()
 {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType==0){
           val view= LayoutInflater.from(parent.context).inflate(R.layout.item_count,parent,false)
            return ViewHolder(view)
        }
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

          holder.bind(list.get(position),position)

    }

    override fun getItemViewType(position: Int): Int {
        if (position==0){
            return 0;
        }
        return 1
    }
    override fun getItemCount(): Int {
        return list.size;

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item:Items?,position: Int) {
            if (position==0){
                itemView.answer_count.text=item?.answerCount.toString()
                itemView.view_count.text=item?.viewCount.toString()
            }else{
                itemView.name.text=item?.owner?.displayName
                Glide.with(context)
                    .load(item?.owner?.profileImage)
                    .circleCrop()
                    .into(itemView.profile)
                itemView.title.text=item?.title
                itemView.date.text= item?.creationDate?.let { getDate(it) }
            }
            itemView.setOnClickListener {
                (context as onClick).itemClicked(position)
            }
        }
    }
     private fun getDate(time:Long): String {
         val formatter = SimpleDateFormat("dd/MM/yyyy");
         return formatter.format(Date(time));


     }

}