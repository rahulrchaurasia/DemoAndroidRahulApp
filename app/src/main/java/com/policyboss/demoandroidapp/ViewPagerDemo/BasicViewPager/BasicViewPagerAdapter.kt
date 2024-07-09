package com.policyboss.demoandroidapp.ViewPagerDemo.BasicViewPager

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ViewpagerLayoutItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BasicViewPagerAdapter(private val context: Context,

                            private val list: MutableList<FoodEntity>,
                            val onItemClick: (FoodEntity ,Int) -> Unit
      ) :
    RecyclerView.Adapter<BasicViewPagerAdapter.FoodViewHolder>() {

    // Create a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var refreshJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        val  binding =   ViewpagerLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return FoodViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        holder.bindfood(list[position], position)


    }



    inner class FoodViewHolder(val binding: ViewpagerLayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindfood(entity:  FoodEntity, position: Int){

            binding.txtTitle.text = entity.title

            // Reset progress before starting animation


            binding.imageView.load(entity.imageUrl) {
                crossfade(true)
                 placeholder(R.drawable.ic_splash_screen) // Show a placeholder while loading
               // transformations(CircleCropTransformation()) // Apply a transformation (optional)
            }


            Log.d(Constant.TAG,"position : $position " )

//            if(position  == list.size - 1 ){
//
//               // viewPager2.post(runnable)
//
//
//
//                refreshData()
//            }


        }



    }


    override fun getItemCount(): Int = list.size

    private val runnable = Runnable {

        list.addAll(list)

        notifyDataSetChanged()
    }

    private fun refreshData(){

        //here no job is created hence no need to check is Job is Active or not ?
//        coroutineScope.launch {
//            delay(1000) // delay for 1 second (adjust as needed)
//            list.addAll(list)
//            notifyDataSetChanged()
//        }

        refreshJob?.cancel() // Cancel the existing job if any
        refreshJob = coroutineScope.launch {
            delay(1000) // delay for 1 second (adjust as needed)
            list.addAll(list)

            notifyDataSetChanged()
        }
    }


    // Call this method to remove the callback when it's no longer needed
    fun removeCallback() {

       // viewPager2.removeCallbacks(runnable)

        coroutineScope.cancel()
    }




}