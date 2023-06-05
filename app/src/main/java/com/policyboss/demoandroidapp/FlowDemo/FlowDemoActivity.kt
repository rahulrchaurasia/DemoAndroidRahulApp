package com.policyboss.demoandroidapp.FlowDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.databinding.ActivityFlowDemoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/*
  Refer below link for flow
 */
//https://www.youtube.com/watch?v=r6bhNDB3J9Y&list=PLdFC34ba_zA46uv1QteVWur1nHs6iTSIJ&index=5

class FlowDemoActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var binding : ActivityFlowDemoBinding


    private lateinit var fixedFlow : Flow<Int>
    private lateinit var collectionFlow : Flow<Int>
    private lateinit var lambdaFlow : Flow<Int>
    private lateinit var channelFlow : Flow<Int>

    private val list = listOf(1,2,3,4,5 )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlowDemoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Flow Demo"
        }

        setupFixedFlow()
        setupFromList()
        setupFlowWithLembda()
        setupChannelFlowWithLambda()


        setClickListener()
    }

    fun setClickListener(){

        binding.btnFixedFlow.setOnClickListener(this)
        binding.btnCollection.setOnClickListener(this)
        binding.btnlembda.setOnClickListener(this)
        binding.btnChannel.setOnClickListener(this)
        binding.btnFlowChain.setOnClickListener(this)

    }

    //region create flow
    private  fun  setupFixedFlow(){

       fixedFlow = flowOf(1,2,3,4,5).onEach {
           delay(300)
       }
    }

    private  fun setupFromList(){

        collectionFlow = list.asFlow().onEach {
            delay(300)
        }
    }


    fun setupFlowWithLembda() {

        lambdaFlow = flow {
            (1..5).forEach {
                delay(1000)
                emit(it)
            }
        }
    }


    private fun setupChannelFlowWithLambda(){

        channelFlow = channelFlow {

            (1..5).forEach {
                delay(1000)
                send(it)          // for channel we have to use send
            }
        }

    }

    //endregion

    //////// Collect ////////////////

    //region collect Flow
    private fun collectFixedFlow(){

        CoroutineScope(Dispatchers.Main).launch {
            fixedFlow.collect{ items ->

            Log.d(Constant.TAG," fixedFlow ${items}")

            }
        }
    }

    private fun collectCollectionFlow(){
        lifecycleScope.launch(Dispatchers.Main){

            collectionFlow.collect{
                Log.d(Constant.TAG,"collectionFlow ${it}")
            }

        }

    }


    private fun collectLembdaFlow(){

        lifecycleScope.launch (Dispatchers.Main){

            lambdaFlow.collect{
                Log.d(Constant.TAG,"collectLembdaFlow  ${it}")
            }
        }

    }

    private fun collectChannelFlow(){

        lifecycleScope.launch (Dispatchers.Main){

            channelFlow.collect{
                Log.d(Constant.TAG,"channelFlow  ${it}")
            }
        }

    }

    private suspend fun createFlowChain() {

        flow {

            ( 0..10).forEach(){ value ->

                delay(1000)
                emit(value)
           }

        }.collect{

            Log.d(Constant.TAG,"$it")

        }
    }

    private suspend fun createFlowChain1() {


        val flowData = flow {

            (0..100).forEach {

                delay(1000)

                emit(it)

            }
        }
    }

    //endregion


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            android.R.id.home ->{
                finish()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {

        when(view?.id)  {

            binding.btnFixedFlow.id -> {

                collectFixedFlow()
            }

            binding.btnCollection.id -> {

                collectCollectionFlow()
            }
            binding.btnlembda.id -> {

                collectLembdaFlow()
            }

            binding.btnChannel.id -> {

               collectChannelFlow()

            }

            binding.btnFlowChain.id -> {

                lifecycleScope.launch(Dispatchers.IO) {

                    createFlowChain()
                }

            }
        }
    }

}