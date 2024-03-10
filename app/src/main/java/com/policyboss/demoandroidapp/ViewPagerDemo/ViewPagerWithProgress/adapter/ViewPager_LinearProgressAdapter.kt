package com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ViewpagerProgressLayoutItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Note : we used 5sec delay manually..for auto linear prgress

class ViewPager_LinearProgressAdapter(private val context: Context,
                                      private val list: MutableList<FoodEntity>,
                                      private var activePosition: Int = 0
      ) :
    RecyclerView.Adapter<ViewPager_LinearProgressAdapter.FoodViewHolder>() {


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


            //Note : for Handling 5 sec
            val job =  CoroutineScope(Dispatchers.Main).launch {
                delay(100) // Initial delay to avoid immediate progress update
                for (progress in 4..100 step 2) {
                    if (isActive && !job!!.isCancelled) { // Check for both isActive and cancellation
                        withContext(Dispatchers.Main) {
                           // binding.progress.progress = progress

                            if(progress <= 6){
                                binding.progress.progress = 8
                            }else{
                                binding.progress.progress = minOf(progress, 100)
                            }

                        }
                    } else {
                        // Job has been cancelled, break out of the loop
                        break
                    }
                    delay(100) // Delay between each progress update

                }


                // Switch to next ViewPager item only if the job is active and for the current view holder
                if (isActive && list[pos] == entity ) {
                    withContext(Dispatchers.Main) {

                        notifyDataSetChanged()
                      //  notifyItemChanged(activePosition,entity)

                    }
                }
            }
            return job
        }


    }


    override fun getItemCount(): Int = list.size

    fun updateProgressAnimations(foodEntity: FoodEntity) {

        CoroutineScope(Dispatchers.Main).launch {

            //delay(100)
            cancelProgressAnimation()
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

    }

    fun hideProgressAnimations() {

        CoroutineScope(Dispatchers.Main).launch {

            //delay(100)
            cancelProgressAnimation()

            list.forEach { it.isUpdate = false }


            notifyDataSetChanged()

        }

    }

    fun cancelProgressAnimation() {
        job?.cancel()
        job = null


    }


}