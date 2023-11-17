package com.policyboss.demoandroidapp.LoginModule.UI.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardEntity
import com.policyboss.demoandroidapp.databinding.SliderItemModelBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashBoardAdapter (private var listDashBoard: MutableList<DashboardEntity> , private var viewPager2: ViewPager2? = null) :
 RecyclerView.Adapter<DashBoardAdapter.DasbboardHolder>(){


    private lateinit var binding: SliderItemModelBinding    // layout using viewBinding

    private var Job: Job? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DasbboardHolder {

//        binding = DashboardProdItemModelBinding.inflate(
//            LayoutInflater.from(parent.context),parent,false)

        binding = SliderItemModelBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return DasbboardHolder(binding.root)

    }

    override fun onBindViewHolder(holder: DasbboardHolder, position: Int) {
       var dashboardEntity = listDashBoard[position]

        binding.apply {

            txtProductName.text = dashboardEntity.menuname
            txtProductDesc.text = dashboardEntity.description
           // imgIcon.
        }
        if(position == listDashBoard.size -2) {

            if (viewPager2 != null) {

                Log.d(Constant.TAG_Coroutine,"viewpager at Adapter" + position)
                cancelJob()
              //  viewPager2!!.post(slideRun)

                 Job =   CoroutineScope(Dispatchers.Main).launch{
                    listDashBoard.addAll(listDashBoard)

                    notifyDataSetChanged()
                }
            }
        }
    }




    override fun getItemCount() = listDashBoard.size


    class DasbboardHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

    }

    fun cancelJob() {
        Job?.cancel()
    }
    val slideRun = object : Runnable{
        override fun run() {

            listDashBoard.addAll(listDashBoard)

            notifyDataSetChanged()
        }
        //runnable

    }
    fun setData(listDashBoard: MutableList<DashboardEntity>){

        this.listDashBoard = listDashBoard
        notifyDataSetChanged()
    }


}