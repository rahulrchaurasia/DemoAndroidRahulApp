package com.policyboss.demoandroidapp.ViewPagerDemo.CarouselViewPager.DotIndicator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.DotIdicatorEntity
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.LayoutDotItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DottedViewPagerAdapter(private val context: Context,
                             private val list: MutableList<DotIdicatorEntity>,
                             val onItemClick: (DotIdicatorEntity ) -> Unit
      ) :
    RecyclerView.Adapter<DottedViewPagerAdapter.FoodViewHolder>() {


    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var Job: Job? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        val  binding =   LayoutDotItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return FoodViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        holder.bindfood(list[position], position)


    }



    inner class FoodViewHolder(val binding: LayoutDotItemBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var animatorSet : AnimatorSet
        fun bindfood(entity:  DotIdicatorEntity, position: Int){

            // Reset progress before starting animation


            if(entity.isSelected){
                binding.imgDot.load(R.drawable.selected_dot)
            }else{
                binding.imgDot.load(R.drawable.unselected_dot)
            }


           // onItemClick(entity )




        }


    }


    override fun getItemCount(): Int = list.size



    fun updateDotList(foodEntity: FoodEntity) {


//        list.find {
//
//            it.id == foodEntity.id
//        }?.isSelected = true
//
//        list.forEach { if (it.id != foodEntity.id) it.isSelected = false }
//
//        notifyDataSetChanged()

        CoroutineScope(Dispatchers.Main).launch {
           // delay(100)

            // Find the selected item and update its selection state
            val selectedItem = list.find { it.id == foodEntity.id }
            selectedItem?.isSelected = true

            // Deselect all other items
            list.filterNot { it == selectedItem }
                .forEach { it.isSelected = false }

            // Notify only the changed item
            selectedItem?.let {
                notifyItemRangeChanged(0,list.size)
            }

        }

    }



    // Call this method to remove the callback when it's no longer needed








}