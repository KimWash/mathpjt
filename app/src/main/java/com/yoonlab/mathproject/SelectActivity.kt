package com.yoonlab.mathproject

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


var problemlevel: String = ""
var problempoint1: String = ""
var problemsolver1: String = ""
var sProblem: Int = 0

class SelectActivity : AppCompatActivity() {

    private var items: MutableList<ProblemList> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectlevel)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl2 = useruuid?.getString("uuid", null)
        problemView = findViewById(R.id.problems)
        val recycle = findViewById<RecyclerView>(R.id.selecting)
        val adapter = SelectAdapter(items)
        recycle.adapter = adapter
        recycle.layoutManager = LinearLayoutManager(this)
        Catalyst(items, recycle, adapter).execute()
        val selvage = Intent(this@SelectActivity, SolveActivity::class.java)
        startActivity(selvage)

    }

    class Catalyst(
        private val items: MutableList<ProblemList>, private val recycle: RecyclerView,
        private val adapter: SelectAdapter
    ) : AsyncTask<Void, Int, Any>() {
        override fun onPreExecute() {
            super.onPreExecute()
            adapter.setItemClickListener(object : SelectAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    sProblem = position
                }
            })
        }

        override fun doInBackground(vararg params: Void?): Any {
            //어떤 문제를 불러옴??
            val pCount = HWP().execute().get() as Int
            for (i in 1 until pCount + 1) {
                val problemInf = GetProblem(i).execute().get() as Array<*>
                problempoint1 = problemInf[0].toString()
                problemsolver1 = problemInf[1].toString()
                problemlevel = problemInf[2].toString()
                when (problemlevel) {
                    "0" -> {
                        items.add(ProblemList("$i", "Easy", problempoint1, problemsolver1))
                    }
                    "1" -> {
                        items.add(ProblemList("$i", "Middle", problempoint1, problemsolver1))
                    }
                    "2" -> {
                        items.add(ProblemList("$i", "Hard", problempoint1, problemsolver1))
                    }
                }
            }
            return items
        }

        override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
        }

    }
}
