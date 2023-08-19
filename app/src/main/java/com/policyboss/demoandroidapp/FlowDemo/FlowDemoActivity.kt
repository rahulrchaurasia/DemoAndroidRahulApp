package com.policyboss.demoandroidapp.FlowDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
//https://www.youtube.com/watch?v=_1xH9d7w_tA&t=353s


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

        binding.btnFlowAdv1.setOnClickListener(this)
        binding.btnFlowAdv2.setOnClickListener(this)
        binding.btnFlowAdv3.setOnClickListener(this)
        binding.btnFlowAdv4.setOnClickListener(this)
        binding.btnFlowAdv5 .setOnClickListener(this)

        binding.btnFlowAdv6.setOnClickListener(this)
        binding.btnFlowAdv7 .setOnClickListener(this)
        binding.btnFlowAdv8.setOnClickListener(this)
        binding.btnFlowAdv9.setOnClickListener(this)
        binding.btnFlowAdv10.setOnClickListener(this)

    }

    //region create Basic flow
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

    //region collect  BasicFlow
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
                Log.d(Constant.TAG,Thread.currentThread().name)
                emit(value)
           }

        }.flowOn(Dispatchers.IO)
            /*
           // Mark : createFlowChain() is called in .launch(Dispatchers.Main)
           hence by default collect called in main Thread no need to again switch it in
           main thread like withContext(Dispatchers.Io)like in courotines.
              */
            .collect{

            Log.d(Constant.TAG,"in Collect "+ Thread.currentThread().name)
            Log.d(Constant.TAG,"Flow Chain $it")

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


    ////// Advance Flow ////////////////

    // region Advance Flow

    private fun producer() : Flow<Int>{

        return  flow {

            val list = listOf(1,2,3,4,5,6)

            list.forEach {
                delay(1000)
                emit(it)
            }
        }
    }

    private fun callAdvanceFlow1(){

        lifecycleScope.launch(Dispatchers.Main) {

            producer()
                .onStart {
                    emit(-1)
                    Log.d(Constant.TAG, "Starting Out")
                }
                .onCompletion {
                    emit(7 )
                    Log.d(Constant.TAG, "Completed")
                }

                .onEach {
                    Log.d(Constant.TAG, "About to Emit..$it")
                }
                .collect{
                    Log.d(Constant.TAG, "Collect..${it.toString()}")
                }
        }
    }

    private fun callAdvanceFlow2(){

        lifecycleScope.launch(Dispatchers.Main) {

            producer()
                .map {
                    it*2
                }
                .filter {
                    it <8
                }.flowOn(Dispatchers.IO)
                .collect{

                    Log.d(Constant.TAG, "Flow Advance 2 op:- $it")
                }

        }

    }

    private fun callAdvanceFlow3(){



        lifecycleScope.launch (Dispatchers.Main){

            getNotes()
                .map {
                   // FormattedNote(it.isActive,it.title.uppercase(),it.description,)
                    FormattedNote(isActive = it.isActive, title = it.title.uppercase(),

                        area = it.description +" MH-State"
                        )

                }
                .filter {
                    it.isActive && it.title.length < 5
                }
                .flowOn(Dispatchers.IO)
                .buffer(2)
                .collect{
                    Log.d(Constant.TAG, "Flow Advance 3 Use OF map and filter op:-  ${it.toString()}")

                }
        }
    }

    //endregion

    //region Basic Intermidiate Opertor
    private fun callTakeFlow(){

        lifecycleScope.launch(Dispatchers.Main) {

            flowOf(1, 2, 3, 4, 5, 6)

                .take(3)
                .collect {

                    Log.d(Constant.TAG, "Flow take op:- $it")
                }
        }

    }

    private fun callTakeWhileFlow(){

        lifecycleScope.launch(Dispatchers.Main) {

            flowOf(1, 2, 3, 4, 5, 6)

                .takeWhile { it < 3 }
                .collect {

                    Log.d(Constant.TAG, "Flow take op:- $it")
                }
        }
    }

    private fun calltransformFlow(){

        lifecycleScope.launch(Dispatchers.Main) {

            flowOf(1, 2, 3, 4, 5, 6)

                .transform{

                    emit("First${it}")
                    emit("Second ${it*2}")
                    emit("Third ${it+1}")
                }
                .collect {

                    Log.d(Constant.TAG, "Flow take op:- $it")
                }
        }
    }

    private fun callDistincUntilChangedFlow(){

        lifecycleScope.launch(Dispatchers.Main) {

            flowOf(1,1, 2, 3,3, 4, 5, 6,1,4)

                .distinctUntilChanged()
                .collect {

                    Log.d(Constant.TAG, "Flow take op:- $it")
                }
        }
    }

    private fun callSequentiallDemoFlow(){

        lifecycleScope.launch(Dispatchers.Main) {


            val flow = flow {

                (1..5).forEach(){
                    Log.d(Constant.TAG, "Emitter Start Cooking Pancacke:- $it")

                    delay(1000)
                    Log.d(Constant.TAG, "Emitter Pancake  $it ready:-")
                    emit(it)

                }

            }


            flow.collect{
                Log.d(Constant.TAG, "Collector Start eating Pancake:- $it")
                delay(3000)
                Log.d(Constant.TAG, "Collector Finished eating  Pancake  $it ")
            }
        }
    }

    private fun callBufferDemoFlow(){

        lifecycleScope.launch(Dispatchers.Main) {


            val flow = flow {

                repeat(5){
                    Log.d(Constant.TAG, "Emitter Start Cooking Pancacke:- $it")

                    delay(1000)
                    Log.d(Constant.TAG, "Emitter Pancake  $it ready:-")
                    emit(it)

                }

            }.buffer()


            flow.collect{
                Log.d(Constant.TAG, "Collector Start eating Pancake:- $it")
                delay(3000)
                Log.d(Constant.TAG, "Collector Finished eating  Pancake  $it ")
            }
        }
    }
    //endregion


    fun MutableStateFlowDemo(){

        lifecycleScope.launch(Dispatchers.Main) {
            val flow = MutableStateFlow(0)

      // Emit values to the Flow
            flow.value = 1
            flow.value = 2
            flow.value = 3

           // Collect the Flow and print the latest value
            flow.collect { value ->

                Log.d(Constant.TAG, "Collector Finished eating  Pancake  $value ")
            }
        }
    }

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
                lifecycleScope.launch(Dispatchers.Main) {
                    createFlowChain()
                }
            }
            binding.btnFlowAdv1.id -> {

                callAdvanceFlow1()

            }
            binding.btnFlowAdv2.id -> {

                callAdvanceFlow2()
            }
            binding.btnFlowAdv3.id -> {
                callAdvanceFlow3()

            }
            binding.btnFlowAdv4.id -> {

                callTakeFlow()

            }

            binding.btnFlowAdv5.id -> {

                callTakeWhileFlow()

            }

            binding.btnFlowAdv6.id -> {

                calltransformFlow()

            }

            binding.btnFlowAdv7.id -> {

                callDistincUntilChangedFlow()

            }
            binding.btnFlowAdv8.id -> {

                callSequentiallDemoFlow()

            }
            binding.btnFlowAdv9.id -> {

                callBufferDemoFlow()

            }
            binding.btnFlowAdv10.id -> {

                MutableStateFlowDemo()

            }

        }
    }

    private fun getNotes1() = flow<Note>{


        val list = listOf(

            Note(id = 1, isActive = true, title = "First ", description = "First description") ,
             Note(id = 2, isActive = false, title = "Second ", description = "Second description"),
            Note(id = 3, isActive = false, title = "Third ", description = "Third description"),
            Note(id = 4, isActive = true, title = "Four ", description = "Four description"),
            Note(id = 5, isActive = true, title = "Five ", description = "Five description"),
           Note(id = 6, isActive = true, title = "Six ", description = "Six description")
        )
         list.asFlow()
    }

    private fun getNotes() : Flow<Note>
    {


        val list = listOf(

            Note(id = 1, isActive = true, title = "First ", description = "First description") ,
            Note(id = 2, isActive = false, title = "Second ", description = "Second description"),
            Note(id = 3, isActive = false, title = "Third ", description = "Third description"),
            Note(id = 4, isActive = true, title = "Four ", description = "Four description"),
            Note(id = 5, isActive = true, title = "Five ", description = "Five description"),
            Note(id = 6, isActive = true, title = "Six ", description = "Six description")
        )
        return list.asFlow()
    }
}

data class Note(val id : Int, val isActive : Boolean ,val title : String, val description : String)

data class FormattedNote( val isActive : Boolean ,val title : String, val area : String)