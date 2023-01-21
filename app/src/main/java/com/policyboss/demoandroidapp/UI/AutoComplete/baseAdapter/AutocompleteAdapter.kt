package com.policyboss.demoandroidapp.UI.AutoComplete.baseAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.policyboss.demoandroidapp.DataModel.BankEntity
import com.policyboss.demoandroidapp.R

class AutocompleteAdapter(context: Context,
                          private var list: List<BankEntity>,
                         private var onItemClick : ((menu : BankEntity) -> Unit)
                          ) : BaseAdapter(), Filterable {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData( list: List<BankEntity>){

        this.list = list
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.listitems_layout, parent, false)
        } else {
            view = convertView
        }

        val bankEntity : BankEntity = list.get(position)
        if(bankEntity != null){

            val textTitle = view.findViewById<TextView>(R.id.txtTitle)
           // textTitle.text = getItem(position).toString()
            textTitle.text = bankEntity.bankname
        }


        view.setOnClickListener{

            onItemClick(bankEntity)
        }

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredData = list.filter { it.bankname.uppercase().contains(constraint.toString().uppercase()) }
                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as List<BankEntity>
                notifyDataSetChanged()
            }
        }
    }
}
