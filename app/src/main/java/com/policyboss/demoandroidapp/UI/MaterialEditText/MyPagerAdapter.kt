package com.policyboss.demoandroidapp.UI.MaterialEditText

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.progressindicator.LinearProgressIndicator

import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.PagerLayoutItemBinding
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPagerAdapter(private val context: Context, private val viewPager : ViewPager2,
                     private val list: MutableList<FoodEntity>,
                     val onItemComplete: (Int) -> Unit
      ) :
    RecyclerView.Adapter<MyPagerAdapter.FoodViewHolder>() {


    private var progressIndicator: LinearProgressIndicator? = null
    private var job: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        val  binding =   PagerLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return FoodViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        holder.bindfood(list[position], position)


    }



    inner class FoodViewHolder(val binding: PagerLayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bindfood(entity: FoodEntity, position: Int){

            binding.txtTitle.text = entity.title

            // Reset progress before starting animation
            binding.progress.progress = 0

            binding.imageView.load(entity.imageUrl) {
                crossfade(true)
                 placeholder(R.drawable.ic_splash_screen) // Show a placeholder while loading
                transformations(CircleCropTransformation()) // Apply a transformation (optional)
            }


            cancelProgressAnimation()// Cancel any ongoing animation
            job = startProgressAnimation(entity, position)



            if(position == list.size){
                viewPager.post(slideRun)

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
                      // viewPager.setCurrentItem(viewPager.currentItem + 1, false)
                        onItemComplete?.invoke(pos)
                    }
                }
            }
            return job
        }


    }


    override fun getItemCount(): Int = list.size



    val slideRun = object : Runnable{
        override fun run() {

            list.addAll(list)

            notifyDataSetChanged()
        }
        //runnable

    }
    fun cancelProgressAnimation() {
        job?.cancel()
        job = null

    }

}