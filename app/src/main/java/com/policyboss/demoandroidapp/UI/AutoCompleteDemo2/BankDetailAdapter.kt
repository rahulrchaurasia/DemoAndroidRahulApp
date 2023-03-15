package com.policyboss.demoandroidapp.UI.AutoCompleteDemo2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policyboss.demoandroidapp.DataModel.BankModel.BankEntity
import com.policyboss.demoandroidapp.databinding.ListitemsLayoutBinding

class BankDetailAdapter(private var context : Context,
                        private var listMenu: List<BankEntity>,
                        private var onItemClick : ((menu : BankEntity) -> Unit )
) :
    RecyclerView.Adapter<BankDetailAdapter.BankDataHolder>(){

    //private lateinit var binding: DashboardProdItemModelBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankDataHolder {

        val  binding = ListitemsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return BankDataHolder(binding)
    }

    override fun onBindViewHolder(holder: BankDataHolder, position: Int) {

        // getting a menuEnity from listMenu &
        // passing in viewholder's bind function
        holder.bind(listMenu[position])


        // region comment Alternate way
//        holder.binding.apply {
//
//            imgIcon.setImageDrawable(context.getDrawable(menuEntity.image))
//
//            txtProductName.text = menuEntity.name
//            txtProductDesc.text = menuEntity.description
//        }

        //endregion


    }

    override fun getItemCount() = listMenu.size


    inner class BankDataHolder (val binding: ListitemsLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(bankEntity: BankEntity) = binding.apply {


            txtTitle.text = bankEntity.bankname

            root.setOnClickListener {
                // Invoking this lambda function so that  it can give callback in our activity/fragment
                // We are also passing menuEntity object  in it while invoking/calling it

                onItemClick(bankEntity)
            }

        }


    }

    fun  setData(listMenu: List<BankEntity>){

        this.listMenu = listMenu
        notifyDataSetChanged()
    }

}
