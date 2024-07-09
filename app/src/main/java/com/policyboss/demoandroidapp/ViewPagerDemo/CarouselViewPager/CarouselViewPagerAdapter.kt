package com.policyboss.demoandroidapp.ViewPagerDemo.CarouselViewPager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ViewpagerCarouselLayoutItemBinding
import com.policyboss.demoandroidapp.databinding.ViewpagerLayoutItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CarouselViewPagerAdapter(private val context: Context,
                               private val list: MutableList<FoodEntity>,
                               val onItemClick: (FoodEntity ,Int) -> Unit
      ) :
    RecyclerView.Adapter<CarouselViewPagerAdapter.FoodViewHolder>() {


    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var Job: Job? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        val  binding =   ViewpagerCarouselLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return FoodViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        holder.bindfood(list[position], position)


    }



    inner class FoodViewHolder(val binding: ViewpagerCarouselLayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindfood(entity:  FoodEntity, position: Int){

            binding.txtTitle.text = entity.title

            // Reset progress before starting animation


            binding.imageView.load(entity.imageUrl) {
               // crossfade(true)
                placeholder(R.drawable.ic_splash_screen) // Show a placeholder while loading
                // Apply a transformation (optional)
            }

            onItemClick(entity ,position)


            if(position  == list.size - 2){

                // viewPager2.post(runnable)

                refreshData()
            }


        }



    }






    override fun getItemCount(): Int = list.size


//    fun updateItems(newItems: List<Item>) {
//        val diffResult = DiffUtil.calculateDiff(DiffUtil.ItemCallback(this), items, newItems)
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//    }

    private fun refreshData(){

        cancelJob()

        Job =     coroutineScope.launch {
            //delay(2000) // delay for 1 second (adjust as needed)
            list.addAll(list)
            notifyDataSetChanged()
        }
    }


    // Call this method to remove the callback when it's no longer needed


    fun cancelJob() {
        Job?.cancel()
    }





}