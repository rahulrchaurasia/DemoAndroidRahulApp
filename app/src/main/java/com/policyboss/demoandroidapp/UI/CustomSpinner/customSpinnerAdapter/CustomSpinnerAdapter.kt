package com.policyboss.policybosspro.login.customSpinnerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.policyboss.demoandroidapp.R
import com.policyboss.policybosspro.login.customSpinner.SpinnerItem


class CustomSpinnerAdapter(
    private val context: Context,
    private val fruitList: List<SpinnerItem>
) : BaseAdapter() {

    override fun getCount(): Int = fruitList.size

    override fun getItem(position: Int): Any = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rootView = LayoutInflater.from(context)
            .inflate(R.layout.custom_spinner_layout, parent, false)

        val txtName = rootView.findViewById<TextView>(R.id.spinner_text)
       // val image = rootView.findViewById<ImageView>(R.id.image)

        txtName.text = fruitList[position].name
       // image.setImageResource(fruitList[position].image)

        return rootView
    }
}