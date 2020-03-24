package com.yoonlab.mathproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.select_item.view.*
import kotlinx.android.synthetic.main.selectlevel.view.*
import com.yoonlab.mathproject.ProblemList

class SelectAdapter(data : MutableList<ProblemList>) : RecyclerView.Adapter<SelectAdapter.SelectViewHolder>() {
    var items: MutableList<ProblemList> = data

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }



    //뷰홀더 생성
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = SelectViewHolder(p0)

    //item 사이즈
    override fun getItemCount(): Int = items.size

    //여기서 item을 textview에 옮겨준다.
    override fun onBindViewHolder(p0: SelectViewHolder, p1: Int) {
        items[p1].let { item ->
            with(p0) {
                whatnumber.text = item.number11
                whatlevel.text = item.level11
                whatsolver.text = item.solver11
                whatpoint.text = item.point11
                whatlevelnum.text = item.levelnumber11
            }
        }
        p0.itemView.setOnClickListener {
            itemClickListner.onClick(it, p1)
        }
    }

    inner class SelectViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.select_item, parent, false)) {
        val whatnumber = itemView.number1
        val whatlevel = itemView.name
        val whatsolver = itemView.solver
        val whatpoint = itemView.point1
        val whatlevelnum = itemView.levelnumber1
    }
}