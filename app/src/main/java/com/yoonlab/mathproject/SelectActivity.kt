package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.selectlevel.*
import androidx.recyclerview.widget.RecyclerView

data class ProblemList(
    var number11: Int,
    var level11: String,
    var point11: Int,
    var solver11: Int
)

class SelectActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectlevel)

        val problemlist = arrayListOf<ProblemList>()
        for (i in 0..30){
            problemlist.add(ProblemList(i,"Easy",100,0))
        }

    }
}

class SelectAdapter(val items: List<ProblemList>) : RecyclerView.Adapter<SelectAdapter.SelectViewHolder>{
    class SelectViewHolder(val binding: ItemProblemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_item, parent, false)
        val viewHolder = SelectViewHolder(ItemProblemBinding.binf(view))
        return  viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        holder.binding.problem = items[position]
    }


}