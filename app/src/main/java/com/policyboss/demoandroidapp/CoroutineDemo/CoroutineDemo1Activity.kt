package com.policyboss.demoandroidapp.CoroutineDemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.FlowDemo.FlowDemoActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCoroutineDemo1Binding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoroutineDemo1Activity : AppCompatActivity(),  View.OnClickListener {

    lateinit var binding : ActivityCoroutineDemo1Binding

    private val viewModel: CoroutineViewModel by viewModels()

    var jobRepeat: Job? = null


    lateinit var sliderRun : Runnable
    var runnable: Runnable? = null
    var sliderHandler = Handler(Looper.getMainLooper())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoroutineDemo1Binding.inflate(layoutInflater)


        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        supportActionBar!!.apply {

            setDisplayShowHomeEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle("Couroutine Demo")
        }

        setonclickListner()

    }

    fun setonclickListner(){

        binding.btnbasic.setOnClickListener(this)

        binding.btnDemo1.setOnClickListener(this)
        binding.btnDemo2.setOnClickListener(this)
        binding.btnDemo3.setOnClickListener(this)
        binding.btnDemo4.setOnClickListener(this)

        binding.btnDemo5.setOnClickListener(this)
        binding.btnDemo6.setOnClickListener(this)
        binding.btnDemo7.setOnClickListener(this)
        binding.btnDemo8.setOnClickListener(this)

        binding.btnDemo10.setOnClickListener(this)
        binding.btnDemo11.setOnClickListener(this)
        binding.btnDemo12.setOnClickListener(this)
        binding.btnDemo13.setOnClickListener(this)

        binding.btnDemo14.setOnClickListener(this)
        binding.btnDemo15.setOnClickListener(this)
        binding.btnDemo16.setOnClickListener(this)
        binding.btnDemo17.setOnClickListener(this)

        binding.btnDemo18.setOnClickListener(this)
        binding.btnDemo19.setOnClickListener(this)
        binding.btnDemo20.setOnClickListener(this)

        binding.btnDemo21.setOnClickListener(this)

        binding.btnHandler.setOnClickListener(this)
        binding.btnHandlerCancel.setOnClickListener(this)
        binding.btnCoroutineHandler.setOnClickListener(this)
        binding.btnHandlerCoroutineCancel.setOnClickListener(this)
        binding.btnDispatchersMain.setOnClickListener(this)
        binding.btnJobDemo.setOnClickListener(this)
        binding.btnJobDemo1.setOnClickListener(this)


    }

    //region Cheezy code refer : Baic Demo method

    suspend fun task1(){

        Log.d(Constant.TAG_Coroutine,"Task 1 Start")
        delay(1000)   // Task go to the suspended
        Log.d(Constant.TAG_Coroutine,"Task 1 End")
    }

    suspend fun task2(){

        Log.d(Constant.TAG_Coroutine,"Task 2 Start")
        delay(2000) // Task go to the suspended
        Log.d(Constant.TAG_Coroutine,"Task 2 End")
    }

    private suspend fun getData(){

       // Log.d(Constant.TAG_Coroutine,"Task  Start :" + viewModel.getCount() )

    }
    private suspend fun printFollowers(){

        var fbFollowers = 0

        // below line execute parallel
        CoroutineScope(Dispatchers.IO).launch{
            fbFollowers = getFbFollowers()
        }
        Log.d(Constant.TAG_Coroutine,"FBFollower" + fbFollowers)   // we get fbFollowers = 0 , bec we are not waiting
        // for parallel execution task
    }


    private suspend fun printFollowerswithJoin(){

        var fbFollowers = 0

        // below line execute parallel
        val job = CoroutineScope(Dispatchers.IO).launch{
            fbFollowers = getFbFollowers()
        }

        job.join()

        Log.d(Constant.TAG_Coroutine,"FBFollower" + fbFollowers)   // we get fbFollowers = 21 , bec we first wait
        // for parallel execution task
    }

    private suspend fun printFollowersJobMultiple(){

        // Note :  task executed  sequentialy .ie one after each other
        var fbFollowers = 0
        var instaFollowers = 0
        var twitterFollowers = 0


        // below line execute parallel
        val job1 = CoroutineScope(Dispatchers.IO).launch{
            fbFollowers = getFbFollowers()
        }

        val  job2 = CoroutineScope(Dispatchers.IO).launch {

            instaFollowers = getInstaFollowers()

        }
        val  job3 = CoroutineScope(Dispatchers.IO).launch {

            twitterFollowers = getTwitterFollowers()

        }

        job1.join()
        job2.join()
        job3.join()

        Log.d(Constant.TAG_Coroutine,"FBFollower " + fbFollowers +" , InstagramFollower ${instaFollowers} and Twitter ${twitterFollowers}")   // we get fbFollowers = 21 , bec we first wait
        // for parallel execution task
    }


    private suspend fun printFollowersAsync(){

        // Note : it take less time bec both task executed parallely not sequentialy.


        // below line execute parallel
        val fb = CoroutineScope(Dispatchers.IO).async{
            getFbFollowers()
        }

        val  insta = CoroutineScope(Dispatchers.IO).async {

            getInstaFollowers()

        }

        val  twitter = CoroutineScope(Dispatchers.IO).async {

            getTwitterFollowers()

        }

        Log.d(Constant.TAG_Coroutine,"FBFollower " + fb.await() +", InstagramFollower ${insta.await()} and Twitter  ${twitter.await()} ")   // we get fbFollowers = 21 , bec we first wait
        // for parallel execution task
    }

    private suspend fun getFbFollowers() : Int{

        delay(3000)
        return  21
    }

    private suspend fun getInstaFollowers() : Int{

        delay(2000)
        return  5
    }

    private suspend fun getTwitterFollowers() : Int{

        delay(1000)
        return  7
    }


    /*********************************************************
    Child - Parent Relation
     ******************************************************/

    private suspend fun executeData(){

        val  parentJob = lifecycleScope.launch(Dispatchers.Main){

            Log.d(Constant.TAG_Coroutine,"Parent Job Started.. ${coroutineContext}")

            val childJob = launch (Dispatchers.IO){

                Log.d(Constant.TAG_Coroutine,"Child Job Started.. ${coroutineContext}")

                delay(5000)
                Log.d(Constant.TAG_Coroutine,"Child Job Ended")
            }

            delay(3000)

            Log.d(Constant.TAG_Coroutine,"Parent Ended")


        }

        parentJob.join()   // Coroutine is suspended untill parent job function completed

        Log.d(Constant.TAG_Coroutine,"Parent Completed")
    }

    /*********************************************************
    Cancel Demo Using Child - Parent Relation
     ******************************************************/

    private suspend fun executeCancelDEmo(){

        val  parentJob = lifecycleScope.launch(Dispatchers.Main){

            Log.d(Constant.TAG_Coroutine,"Parent Job Started.. ${coroutineContext}")

            val childJob = launch (Dispatchers.IO){

                Log.d(Constant.TAG_Coroutine,"Child Job Started.. ${coroutineContext}")

                delay(5000)
                Log.d(Constant.TAG_Coroutine,"Child Job Ended")
            }

            delay(3000)

            Log.d(Constant.TAG_Coroutine,"Parent Ended")


        }
        delay(1000)
        parentJob.cancel()
        parentJob.join()   // Coroutine is suspended untill parent job function completed

        Log.d(Constant.TAG_Coroutine,"Parent Completed")
    }


    private suspend fun executeChildCancelDEmo(){

        val  parentJob = lifecycleScope.launch(Dispatchers.Main){

            Log.d(Constant.TAG_Coroutine,"Parent Job Started.. ${coroutineContext}")

            val childJob = launch (Dispatchers.IO){

                try {
                    Log.d(Constant.TAG_Coroutine,"Child Job Started.. ${coroutineContext}")

                    //delay(5000)
                    executeLongRunningTask()
                    Log.d(Constant.TAG_Coroutine,"Child Job Ended")
                }catch (ex : CancellationException){
                    Log.d(Constant.TAG_Coroutine,"Child is Cancelled via Exception")
                }

            }

            delay(3000)
            Log.d(Constant.TAG_Coroutine,"Child is Cancelled ")
            childJob.cancel()


            Log.d(Constant.TAG_Coroutine,"Parent Ended")


        }
        delay(1000)

        parentJob.join()   // Coroutine is suspended untill parent job function completed

        Log.d(Constant.TAG_Coroutine,"Parent Completed")
    }


    /*********************************************************
    Cancel Long Running Task using isActive
     ******************************************************/

    private suspend fun executeTask(){

        //Job is cancelled but it is still execute the whole task
        // because cpu is very busy and stuck to execute the Longrunning task so it can not realize
        val parentJob = lifecycleScope.launch(Dispatchers.IO){

            for( i in 1..1000){

                executeLongRunningTask()
                Log.d(Constant.TAG_Coroutine,"i is ${i.toString()}")
            }
        }
        delay(1000)
        Log.d(Constant.TAG_Coroutine," Cancel Job")
        parentJob.cancel()
        parentJob.join()
        Log.d(Constant.TAG_Coroutine," Parent Completed")

    }

    private  fun  executeLongRunningTask(){



        for( i in 1..100000000){

        }


    }

    private suspend fun executeTaskIsActive(){

        //Job is cancelled but it is still execute the whole task
        // because cpu is very busy and stuck to execute the Longrunning task so it can not realize
        val parentJob = lifecycleScope.launch(Dispatchers.IO){

            for( i in 1..10000){

                if(isActive){

                    executeLongRunningTask()
                    Log.d(Constant.TAG_Coroutine,"i is ${i.toString()}")
                }

            }
        }
        delay(1000)
        Log.d(Constant.TAG_Coroutine," Cancel Job")
        parentJob.cancel()
        parentJob.join()
        Log.d(Constant.TAG_Coroutine," Parent Completed")

    }



    //endregion


    // region Other suspende method
    suspend fun networkCall() : Int{

        Log.d(Constant.TAG_Coroutine,"network Call Start")
        delay(3000)

        Log.d(Constant.TAG_Coroutine,"network Call End")

        return 21
    }

    suspend fun networkCall1() : String{

        delay(2000)

        Log.d(Constant.TAG_Coroutine,"network Call 1")
        return  "Data 1"

    }

    suspend fun networkCall2() : String{

        delay(1000)

        Log.d(Constant.TAG_Coroutine,"network Call 2")

        return  "Data 2"

    }

    suspend fun networkCall3() : String{

        delay(1000)

        Log.d(Constant.TAG_Coroutine,"network Call 3")

        return  "Data 3"

    }

    suspend fun JobDemo(){
        // Note : Since Job is run asynchronously

        Log.d(Constant.TAG_Coroutine, "Coroutine Start  :")
        val job = lifecycleScope.launch{

            delay(2000)
            Log.d(Constant.TAG_Coroutine, "Job Done :")
        }

        Log.d(Constant.TAG_Coroutine, "Test 1 Complete")

    }

    suspend fun test2JobJoin(){
        // Note :  job.join() wait for asynchronous task to complete

        Log.d(Constant.TAG_Coroutine, "Coroutine Start  :")
        val job = lifecycleScope.launch{

            delay(5000)
            Log.d(Constant.TAG_Coroutine, "Job Done :")
        }

        job.join()
        Log.d(Constant.TAG_Coroutine, "Test 1 Complete")

    }

    suspend fun test3Async(){
        // Note : Since Job is run asynchronously

        Log.d(Constant.TAG_Coroutine, "Coroutine Start  :")
        val deffere  = lifecycleScope.async{

            delay(2000)
            Log.d(Constant.TAG_Coroutine, "Job Done :")

            "20"
        }

        var result =  deffere.await()

        Log.d(Constant.TAG_Coroutine, "Test 3 Async Demo Complete with Result : ${result}")

    }

    suspend fun jobDemo(){


            val job: Job = lifecycleScope.launch() { // Launch a coroutine that simulates work
                delay(1000) // Simulate some work for 1 second
                println("Coroutine is finished")
            }
            // Wait for 500 milliseconds, then try to cancel the coroutine
            delay(500)
            if (job.isActive) {
                println("Trying to cancel the coroutine...")
                job.cancel()
            }
            // Check if the coroutine was cancelled
            if (job.isCancelled) {
                println("Coroutine was cancelled successfully")
            } else {
                println("Coroutine completed normally")
            }


    }

    /// handler Demo ////

    private val taskHandler: Handler by lazy {
        Handler(Looper.getMainLooper()) // Main looper just for example
    }
    fun initiateTask() {

        sliderRun = Runnable {


            Log.d(Constant.TAG_Coroutine, "Handler Task Started each 3 sec " + viewModel.getCount() )

            taskHandler.postDelayed(sliderRun, 3000)
        }


        taskHandler.postDelayed(sliderRun,3000)

// For Complex Code in kotlin

//        taskHandler.postDelayed(Runnable {
//            taskHandler.postDelayed(runnable!!, 3000)
//          Log.d(Constant.TAG_Coroutine, "Handler Task Started each 3 sec " + viewModel.Count + 1)
//        }.also { runnable = it }, 3000)




    }

    fun cancelHandlerTask() {
        //taskHandler.removeCallbacksAndMessages(null)

        if (sliderRun != null) {

            if(this::sliderRun.isInitialized)

                taskHandler.removeCallbacks(sliderRun)

        }
    }


    private fun startRepeatingJob(timeInterval: Long): Job {

        return this.lifecycleScope.launch(Dispatchers.Default){

            while (isActive){
                getData()
                delay(timeInterval)
            }


        }


    }


    //endregion
    override fun onClick(view: View?) {

        when(view!!.id) {


            binding.btnDemo1.id -> {

                Log.d(Constant.TAG_Coroutine, "Start :")
                GlobalScope.launch {

                    networkCall1()
                    networkCall2()
                }
                Log.d(Constant.TAG_Coroutine, "End :")
            }

            binding.btnDemo2.id -> {

                //  So GlobalScope, simply means the scope of this coroutines is the entire application.
                //  It will not get finished, if it is running in the background it won’t stop unless the whole application stop.
                GlobalScope.launch {

                    while (true) {
                        Log.d(Constant.TAG_Coroutine, "Continue Start GlobalScope :")
                        delay(300)
                    }


                }

                startActivity(Intent(this, FlowDemoActivity::class.java))
                this.finish()


            }


            binding.btnDemo3.id -> {


                startActivity(Intent(this, FlowDemoActivity::class.java))
                this.finish()

            }
            ///////////////  Job //////////////////

            //   Job : job will be returned when a new coroutine will be launched.

//                    start()
//                    join()
//                    cancel()


            binding.btnDemo4.id -> {


                lifecycleScope.launch {
                    JobDemo()
                }


            }
            /*
            join() function is a suspending function, i.e it can be called from a coroutine or from within another suspending function.
            Job blocks all the threads until the coroutine in which it is written or have context finished its work.
             Only when the coroutine gets finishes, lines after the join() function will be executed.
             */
            binding.btnDemo5.id -> {


                lifecycleScope.launch {
                    test2JobJoin()
                }


            }
            binding.btnDemo6.id -> {


                lifecycleScope.launch {

                    val job1 = launch { networkCall1() }


                    job1.join()

                    val job2 = launch { networkCall2() }

                    job2.join()

                    val job3 = launch { networkCall3() }

                    job3.join()



                    Log.d(Constant.TAG_Coroutine, "Coroutine Synchronous called .  ")
                }


            }

            binding.btnDemo7.id -> {


                lifecycleScope.launch {
                    test3Async()
                }
            }


            binding.btnDemo8.id -> {


                lifecycleScope.launch {

                    val s1 = async { networkCall1() }
                    // s1.await()
                    val s2 = async { networkCall2() }
                    // s2.await()
                    val s3 = async { networkCall3() }
                    //  s3.await()
                    Log.d(Constant.TAG_Coroutine, "Continue async sequential result")

                    Log.d(
                        Constant.TAG_Coroutine,
                        "Continue async parellel result : FOR 1> ${s1.await()} ,2> ${s2.await()} and 3> ${s3.await()} "
                    )
                }
            }


            binding.btnDemo10.id -> {
                /*
                 Dispatchers : is a way to define a thread on which Coroutine are executed.

                 Predefined Dispatchers :--

                  Dispathers.IO
                  Dispathers.Main
                  Dispathers.Default

                 */

                lifecycleScope.launch(Dispatchers.IO) {
                    val result = networkCall()
                    Log.d(Constant.TAG_Coroutine, "Continue result : ${result}")
                }

            }

            binding.btnbasic.id -> {

                CoroutineScope(Dispatchers.Main).launch {
                    task1()
                }

                CoroutineScope(Dispatchers.Main).launch {

                    task2()
                }

            }
            binding.btnDemo11.id -> {


                lifecycleScope.launch(Dispatchers.IO) {
                    val result = networkCall()
                    Log.d(Constant.TAG_Coroutine, "Continue my result : ${result}")
                    // binding.txtDemo.setText(result.toString())  // We required Main thread to bind the view

                    withContext(Dispatchers.Main) {
                        binding.txtDemo.setText(result.toString())
                    }
                }

            }


            /***********************************************************************************************
             *
             *         Cheezy Code
             *********************************************************************************************/


            binding.btnDemo12.id -> {

                CoroutineScope(Dispatchers.IO).launch {
                    printFollowers()
                }
            }

            binding.btnDemo13.id -> {

                CoroutineScope(Dispatchers.IO).launch {
                    printFollowerswithJoin()
                }
            }

            binding.btnDemo14.id -> {

                CoroutineScope(Dispatchers.IO).launch {
                    printFollowersJobMultiple()
                }
            }

            binding.btnDemo15.id -> {

                CoroutineScope(Dispatchers.IO).launch {
                    printFollowersAsync()
                }
            }

            binding.btnDemo16.id -> {

                lifecycleScope.launch(Dispatchers.Main) {
                    executeData()
                }
            }

            binding.btnDemo17.id -> {


                lifecycleScope.launch(Dispatchers.Main) {
                    executeCancelDEmo()
                }
            }

            ///


            binding.btnDemo18.id -> {


                lifecycleScope.launch(Dispatchers.Main) {
                    executeChildCancelDEmo()
                }
            }

            //LongRunning - Cancel  Demo
            binding.btnDemo19.id -> {


                lifecycleScope.launch(Dispatchers.Main) {
                    executeTask()
                }
            }
            binding.btnDemo20.id -> {


                lifecycleScope.launch(Dispatchers.Main) {
                    executeTaskIsActive()
                }

            }

            // WithContext : Blocking in nature : ie run in Sequentially
            // Mostly used for switching the scope
            binding.btnDemo21.id -> {

                Log.d(Constant.TAG_Coroutine, "Start work")


                lifecycleScope.launch {

                    Log.d(Constant.TAG_Coroutine, " Coroutine Start ")
                    var data = networkCall()

                    Log.d(Constant.TAG_Coroutine, "After network")

                    withContext(Dispatchers.Main) {

                        //
                        Log.d(Constant.TAG_Coroutine, "Run Sequntially...")
                        binding.txtDemo.text = "Data From solution " + data.toString()
                    }
                    Log.d(Constant.TAG_Coroutine, "End Task")
                }


                Log.d(Constant.TAG_Coroutine, "Finish Work")

            }

            binding.btnHandler.id -> {

                initiateTask()
            }

            binding.btnHandlerCancel.id -> {

                cancelHandlerTask()
            }

            binding.btnCoroutineHandler.id -> {

                //region Commented
//                jobRepeat =    this.lifecycleScope.launch {
//
//                   lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
//
//                       while (true){
//                           if(isActive){
//                               getData()
//                           }
//                           delay(2000)
//                       }
//                   }
//                }
                //endregion

                //  jobRepeat = startRepeatingJob(2000)

            }


            binding.btnHandlerCoroutineCancel.id -> {

                jobRepeat?.cancel()
                jobRepeat = null
            }

            binding.btnDispatchersMain.id -> {

                lifecycle.coroutineScope.launch(Dispatchers.Main) {

                    Log.d(Constant.TAG_Coroutine, " Coroutine Start ")
                    delay(1500)

                    val job = CoroutineScope(Dispatchers.IO).launch {
                        Log.d(Constant.TAG_Coroutine, " Coroutine Middle ")
                        networkCall()
                    }

                    //job.join()

                    Log.d(Constant.TAG_Coroutine, " Coroutine End ")
                    delay(800)
                    Log.d(Constant.TAG_Coroutine, " Coroutine Finish ")

                }
            }
            binding.btnJobDemo.id ->{

                lifecycle.coroutineScope.launch(Dispatchers.Main) {


                    val job: Job = CoroutineScope(Dispatchers.Default).launch  { // Launch a coroutine that simulates work
                        delay(1000) // Simulate some work for 1 second
                        Log.d(Constant.TAG_Coroutine,"Coroutine1 is finished")
                    }
                    // Wait for 500 milliseconds, then try to cancel the coroutine
                    delay(500)
                    if (job.isActive) {
                        Log.d(Constant.TAG_Coroutine,"Trying to cancel the coroutine...")
                        job.cancel()
                    }
                    // Check if the coroutine was cancelled
                    if (job.isCancelled) {
                        println("Coroutine was cancelled successfully")
                    } else {
                        Log.d(Constant.TAG_Coroutine,"Coroutine completed normally")
                    }
                }

            }

            binding.btnJobDemo1.id ->{

                lifecycle.coroutineScope.launch(Dispatchers.Main) {

                    val job1 = CoroutineScope(Dispatchers.Default).launch {
                        Log.d(Constant.TAG_Coroutine, " Coroutine1 Started ")

                        delay(300) // Simulate some work for 1 second
                        Log.d(Constant.TAG_Coroutine,"Coroutine1 is finished")
                    }
                    val job2: Job = CoroutineScope(Dispatchers.Default).launch  { // Launch a coroutine that simulates work
                        delay(500) // Simulate some work for 1 second
                        Log.d(Constant.TAG_Coroutine,"Coroutine2 is finished")
                    }
                    // Wait for 400 milliseconds, then try to cancel the coroutine
                    delay(400)
                    if (job1.isActive) {
                        Log.d(Constant.TAG_Coroutine,"Trying to cancel the coroutine1...")
                        job1.cancel()
                    }
                    if (job2.isActive) {
                        Log.d(Constant.TAG_Coroutine,"Trying to cancel the coroutine2...")
                        job2.cancel()
                    }
                    // Check if the coroutine was cancelled
                    if (job1.isCancelled) {
                        println("Coroutine1 was cancelled successfully")
                    } else {
                        Log.d(Constant.TAG_Coroutine,"Coroutine1 completed normally")
                    }

                    if (job2.isCancelled) {
                        println("Coroutine2 was cancelled successfully")
                    } else {
                        Log.d(Constant.TAG_Coroutine,"Coroutine2 completed normally")
                    }
                }

            }

        }


    }
}