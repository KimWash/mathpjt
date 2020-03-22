package com.yoonlab.mathproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.select_item.view.*

data class ProblemList(
    var number11: Int,
    var level11: String,
    var point11: Int,
    var solver11: Int
)

class SelectAdapter() : RecyclerView.Adapter<SelectAdapter.MainViewHolder>() {

    var items: MutableList<ProblemList> = mutableListOf(
        ProblemList(1, "Easy",100,0),
        ProblemList(2, "Middle",500,0),
        ProblemList(3, "Hard",1000,0)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAdapter.MainViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.select_item, parent, false)
        return MainViewHolder(view as ViewGroup)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder() {
        fun bindItems(data : ProblemList){
            itemView.number1 = data.number11
            itemView.name = data.level11
            itemView.point1 = data.point11
            itemView.solver = data.solver11

            //각각의 아이템 클릭시
            itemView.setOnClickListener({
                startActivity(SolveActivity)
            })
        }


    }






}
