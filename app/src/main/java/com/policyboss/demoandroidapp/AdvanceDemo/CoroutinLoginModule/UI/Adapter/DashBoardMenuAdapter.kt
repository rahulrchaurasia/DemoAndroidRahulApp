package com.policyboss.demoandroidapp.LoginModule.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardMenu
import com.policyboss.demoandroidapp.databinding.DashboardProdItemModelBinding


/************************** lambda function’s callback in our fragment/activity ***************************
1> For  Adapter to Activity Communication  use lambda function’s
First, say bye to the lengthy interface way….
we will now add the lambda function as an argument in our adapter’s constructor.
We are taking DashBoardMenuAdapter as an example that takes listMenu & lambda function named ‘onItemClicked’
as an argument & in our lambda function ‘onItemClicked’ we have added menuEntity object
as an argument so that we can receive this movie object in this lambda function’s callback in our fragment/activity.

 2> For ViewBinding
https://www.youtube.com/watch?v=vmJFXmefBsM
https://www.geeksforgeeks.org/how-to-use-view-binding-in-recyclerview-adapter-class-in-android/

 **********************************************************************************************************/
class DashBoardMenuAdapter(private var context : Context,
                           private var listMenu: List<DashboardMenu>,
                           private var onItemClick : ((menu : DashboardMenu) -> Unit )
    ) :
 RecyclerView.Adapter<DashBoardMenuAdapter.MenuHolder>(){

     //private lateinit var binding: DashboardProdItemModelBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {

      val  binding = DashboardProdItemModelBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return MenuHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {

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

   inner class MenuHolder (val binding: DashboardProdItemModelBinding) : RecyclerView.ViewHolder(binding.root){

       fun bind(menuEntity: DashboardMenu) = binding.apply {

           imgIcon.setImageDrawable(context.getDrawable(menuEntity.image))
          // imgIcon.setImageResource(menuEntity.image)

           txtProductName.text = menuEntity.name
           txtProductDesc.text = menuEntity.description

           root.setOnClickListener {
               // Invoking this lambda function so that  it can give callback in our activity/fragment
               // We are also passing menuEntity object  in it while invoking/calling it

               onItemClick(menuEntity)
           }

       }


   }

   fun  setData(listMenu: List<DashboardMenu>){

       this.listMenu = listMenu
       notifyDataSetChanged()
   }

}