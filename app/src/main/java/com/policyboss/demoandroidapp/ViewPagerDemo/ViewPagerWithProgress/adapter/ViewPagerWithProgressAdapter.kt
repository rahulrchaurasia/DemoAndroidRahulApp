package com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.PagerLayoutItemBinding
import com.policyboss.demoandroidapp.databinding.ViewpagerLayoutItemBinding
import kotlinx.coroutines.Job

class ViewPagerWithProgressAdapter(private val context: Context,
                                   private val list: MutableList<FoodEntity>,
                                   val onItemClick: (FoodEntity ,Int) -> Unit
      ) :
    RecyclerView.Adapter<ViewPagerWithProgressAdapter.FoodViewHolder>() {




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

            onItemClick(entity ,position)


        }



    }


    override fun getItemCount(): Int = list.size






}