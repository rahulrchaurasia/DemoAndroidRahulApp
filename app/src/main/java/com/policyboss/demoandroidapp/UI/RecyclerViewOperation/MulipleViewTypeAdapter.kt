package com.policyboss.demoandroidapp.UI.RecyclerViewOperation

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policyboss.demoandroidapp.UI.RecyclerViewOperation.model.SectionData
import com.policyboss.demoandroidapp.databinding.ChildtemsLayoutBinding
import com.policyboss.demoandroidapp.databinding.ListitemsLayoutBinding


class MulipleViewTypeAdapter(private var context : Context,
                             private val sectionDataList : List<SectionData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private  val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_CHILD = 1


    inner class HeaderViewHolder(private val binding: ListitemsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindHeader(headerData: SectionData) {
            binding.txtTitle.text = headerData.title


            binding.txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            binding.icon.visibility = View.GONE
        }
    }

    inner class ChildViewHolder(private val binding: ChildtemsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindChild(childData: SectionData) {
            binding.txtChildHeader.text = childData.content?.get(0)?:""
            binding.txtChildHeaderDes.text = childData.content?.get(1)?:""
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if(viewType == VIEW_TYPE_CHILD){

            val  binding = ChildtemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

            return ChildViewHolder(binding)
        }
        else{

            val  binding = ListitemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

           return HeaderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

//        if(getItemViewType(position) == VIEW_TYPE_CHILD){
//
//            (holder as HeaderViewHolder).bindHeader(sectionDataList[position])
//
//
//        }else{
//
//            (holder as ChildViewHolder).bindChild(sectionDataList[position])
//        }

        val sectionData = sectionDataList[position]

        when(holder){

            is HeaderViewHolder ->{
                holder.bindHeader(sectionData)
            }
            is ChildViewHolder -> {
                holder.bindChild(sectionData)
            }
        }

    }


    override fun getItemCount() = sectionDataList.size

    override fun getItemViewType(position: Int): Int {




        if(sectionDataList[position].content != null){
           return VIEW_TYPE_CHILD
        }else{
            return VIEW_TYPE_HEADER
        }

    }
}