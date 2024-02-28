package com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.PagerLayoutItemBinding
import com.policyboss.demoandroidapp.databinding.ViewpagerLayoutItemBinding
import com.policyboss.demoandroidapp.databinding.ViewpagerProgressLayoutItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.FieldPosition

class ViewPager_ProgressAdapter(private val context: Context,
                                private val list: MutableList<FoodEntity>,
             private var activePosition: Int = 0
      ) :
    RecyclerView.Adapter<ViewPager_ProgressAdapter.FoodViewHolder>() {


    private var progressIndicator: LinearProgressIndicator? = null
    private var job: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        val  binding =   ViewpagerProgressLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return FoodViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        holder.bindfood(list[position], position)



    }



    inner class FoodViewHolder(val binding: ViewpagerProgressLayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindfood(entity:  FoodEntity, position: Int){

           binding.txtTitle.text = entity.progressHeader


            if(entity.isUpdate){

                binding.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.light_blue))
                binding.txtTitle.setTypeface(null, Typeface.BOLD)
                cancelProgressAnimation()// Cancel any ongoing animation
                 job = startProgressAnimation(entity, position)
            }else{

                binding.progress.progress = 0
                binding.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.description_text))
                binding.txtTitle.setTypeface(null, Typeface.NORMAL)

            }

        }


        private fun startProgressAnimation(entity: FoodEntity, pos:  Int) : Job{

            val job =  CoroutineScope(Dispatchers.Main).launch {
                delay(50) // Initial delay to avoid immediate progress update
                for (progress in 0..100 step 1) {
                    if (isActive) {
                        withContext(Dispatchers.Main) {
                            binding.progress.progress = progress
                        }
                    }
                    delay(50) // Delay between each progress update
                }

                // Switch to next ViewPager item only if the job is active and for the current view holder
                if (isActive && list[pos] == entity ) {
                    withContext(Dispatchers.Main) {
                       // val nextPosition = (pos + 1) % list.size

//                         viewPager.setCurrentItem(viewPager.currentItem + 1, false)
//                        onItemComplete?.invoke(pos)
                        notifyItemChanged(activePosition,entity)
//                        if(list.size-1 == activePosition){
//                            activePosition = 0
//                        }else{
//                            activePosition = activePosition + 1
//                        }

                    }
                }
            }
            return job
        }


    }


    override fun getItemCount(): Int = list.size

    fun updateProgressAnimations(foodEntity: FoodEntity) {

//            list.find {
//
//                it.id == foodEntity.id
//            }?.isUpdate = foodEntity.isUpdate
//
//        list.forEach { if (it.id != foodEntity.id) it.isUpdate = false }


        list.find {

            it.id == foodEntity.id
        }?.isUpdate = true

        list.forEach { if (it.id != foodEntity.id) it.isUpdate = false }


        notifyDataSetChanged()
    }

    fun cancelProgressAnimation() {
        job?.cancel()
        job = null

    }

}