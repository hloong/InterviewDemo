package com.hloong.interview.ui.custom.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hloong.interview.R
import kotlinx.android.synthetic.main.activity_custom_rv.*

class CustomRvActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_rv)
        rv_custom.adapter = object:CustomRecyclerView.Adapter{
            override fun getHeight(index: Int): Int {
                return 100
            }

            override fun onCreateViewHolder(
                pos: Int,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                var cv = convertView
                cv = LayoutInflater.from(this@CustomRvActivity).inflate(R.layout.item_table,parent,false)
                var tv = cv!!.findViewById<TextView>(R.id.text1)
                tv.text = "This is Row$pos"
                return cv
            }

            override fun onBinderViewHolder(
                pos: Int,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                var tv = convertView!!.findViewById<TextView>(R.id.text1)
                tv.text = "Custom RV$pos"
                return convertView
            }

            override fun getItemViewType(row: Int): Int {
                return 0
            }

            override fun getViewTypeCount(): Int {
                return 1
            }

            override fun getCount(): Int {
                return 30
            }

        }
    }
}