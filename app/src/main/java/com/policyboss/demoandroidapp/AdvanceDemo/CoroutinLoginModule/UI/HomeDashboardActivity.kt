package com.policyboss.demoandroidapp.LoginModule.UI

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.demo.kotlindemoapp.HomeMain.CarouselViewPager.CarouselTransformer
import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.ViewModel.ResponseOLD
import com.policyboss.demoandroidapp.AdvanceDemo.RetrofitHelper1
import com.policyboss.demoandroidapp.AdvanceDemo.Room.Database.DemoDatabase
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.ActivityResultLauncherDemoActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.MultiplePermissionActivity
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardEntity
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardMenu
import com.policyboss.demoandroidapp.LoginModule.Repository.DBDashboardMenuRepository
import com.policyboss.demoandroidapp.LoginModule.Repository.DashboardRepository
import com.policyboss.demoandroidapp.LoginModule.UI.Adapter.DashBoardAdapter
import com.policyboss.demoandroidapp.LoginModule.UI.Adapter.DashBoardMenuAdapter
import com.policyboss.demoandroidapp.LoginModule.ViewModel.DashboardViewModel
import com.policyboss.demoandroidapp.LoginModule.ViewmodelFactory.DashboardViewModelFactory
import com.policyboss.demoandroidapp.OpenAnotherApp.OpenAnotherActivity
import com.policyboss.demoandroidapp.RetrofitHelper
import com.policyboss.demoandroidapp.Utility.NetworkUtils
import com.policyboss.demoandroidapp.databinding.ActivityHomeDashboardBinding
import com.policyboss.demoandroidapp.databinding.ContentHomeMainBinding

import kotlinx.coroutines.*
import java.lang.Runnable

/****************************************************************************************************
 * Task For Async Task parallel :-- > we Call  UserConstant ans DynamicDashboard Api parallel
// https://www.youtube.com/watch?v=dTqOVsdj0pY&t=555s
//https://discuss.kotlinlang.org/t/how-to-measure-execution-time-of-an-aync-query-request-inside-kotlin-coroutines/23352

 Use of LifecycleScope, we have  two way : lifecycle.coroutineScope.launch {..}
                                           or
                                        lifecycleScope.launch {....}
 ****************************************************************************************************/
class HomeDashboardActivity : BaseActivity() {

    //region Declaration
    lateinit var bindingRoot : ActivityHomeDashboardBinding
    lateinit var binding : ContentHomeMainBinding
    lateinit var layout: View
    lateinit var viewModel: DashboardViewModel

    lateinit var dashBoardAdapter: DashBoardAdapter
    lateinit var dashBoardMenuAdapter: DashBoardMenuAdapter

    lateinit var viewPager2 : ViewPager2
    lateinit var sliderHandler : Handler

    private var viewpager2Job: Job? = null


    // var sliderHandler = Handler(Looper.myLooper())
   //  var sliderRun : Runnable? = null

    //endregion

    private val callback = object  : OnBackPressedCallback(enabled = true){
        override fun handleOnBackPressed() {
            exitOnBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingRoot = ActivityHomeDashboardBinding.inflate(layoutInflater)
        setContentView(bindingRoot.root)

        layout = bindingRoot.root
        binding = bindingRoot.includedHomeMain
        viewPager2 =  binding.viewPager


        init()

        if(NetworkUtils.isNetworkAvailable(this)){

            viewModel.getParallelAPIForUser_Dasbboard(fbaID = "89158")

        }else{
            showSnackBar(layout, Constant.NetworkError)
        }

        //viewModel.getParallelAPIForUser_Dasbboard_ObservingBoth(fbaID = "89158")
        getDashboardResponse()
        getUserConstantResponse()


        onBackPressedDispatcher.addCallback(this, callback)

    }



    //region Event
    override fun onPause() {
        super.onPause()

        //region commented
//        if (sliderRun != null) {
//
//            sliderHandler.removeCallbacks(sliderRun)
//        }
        //endregion
    }

    override fun onResume() {
        super.onResume()

        //region comment
//        if (sliderRun != null) {
//
//            sliderHandler.postDelayed(sliderRun,3000)
//        }
        //endregion
//
    }

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        super.onBackPressed()
//        //  super.onBackPressed()
//
//      //  layout.showAlerDialog(this)
//
//        //exitAlert(){
//
//       // this.finish()
//    }
//
    fun exitOnBackPressed(){

        showAlert("Exit","Do you want to exit!!") { type  : String, dialog : DialogInterface ->

            when(type){
                "Y" -> {
                    // toast("Logout Successfully...!!")
                    this@HomeDashboardActivity.finish()
                }
                "N" -> {
                    dialog.dismiss()
                    toast("Cancel Exit")
                }

            }



        }

    }

    override fun onDestroy() {
        super.onDestroy()

        dashBoardAdapter.cancelJob()
    }

    //endregion

    //region Init method
    private fun init(){

        var demoDatabase = DemoDatabase.getDatabase(applicationContext)
        var dashboardRepository = DashboardRepository(RetrofitHelper1.retrofitLoginApi,demoDatabase)
        var viewModelFactory = DashboardViewModelFactory(dashboardRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(DashboardViewModel::class.java)


        sliderHandler = Handler(Looper.myLooper()!!)
        initData()


    }

    private fun initData(){

        dashBoardAdapter = DashBoardAdapter(ArrayList())
       binding.rvImgSlide.apply {

           setHasFixedSize(true)
           layoutManager = LinearLayoutManager(this@HomeDashboardActivity)
           adapter = dashBoardAdapter
       }


        // Callback using Heigher Order fun ie Lemda we can pass
        dashBoardMenuAdapter = DashBoardMenuAdapter(this,ArrayList()){ it : DashboardMenu ->

            // Here we'll receive callback of
            // every recyclerview item click
            // Now, perform any action here.
            // for ex: navigate to different screen

            navigateDashboardMenu(it)


        }
        binding.rvProduct.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeDashboardActivity)
            adapter = dashBoardMenuAdapter
        }
        binding.rvProduct.scheduleLayoutAnimation()

    }

    //endregion

    //region Api ResponseOLD
    private fun getDashboardResponse(){

        viewModel.dashBoardDataLiveData.observe(this, {

            when(it){

                is ResponseOLD.Loading ->{
                    showDialog()
                }

                is ResponseOLD.Success -> {

                    cancelDialog()

                    it.data?.let {
                        Log.d(Constant.TAG_Coroutine +" Dasbboard :", it.toString())
                        loadViewPager(it.MasterData.Dashboard)

                        dashBoardMenuAdapter.setData(DBDashboardMenuRepository.getDashBoardMenu())
                        binding.rvProduct.scheduleLayoutAnimation()

                    }
                }

                is ResponseOLD.Error -> {
                    cancelDialog()
                    Snackbar.make(layout,it.errorMessage.toString(), Snackbar.LENGTH_SHORT).show()
                    Log.d(Constant.TAG_Coroutine, it.errorMessage.toString())
                }
            }
        })

    }

    private fun getUserConstantResponse(){

        viewModel.constantData.observe(this, {

            when(it){

                is ResponseOLD.Loading ->{
                    //showDialog()
                }

                is ResponseOLD.Success -> {

                   // cancelDialog()

                    it.data?.let {
                        Log.d(Constant.TAG_Coroutine + " UserConstant :", it.toString())


                    }
                }

                is ResponseOLD.Error -> {
                  //  cancelDialog()
                    Snackbar.make(layout,it.errorMessage.toString(), Snackbar.LENGTH_SHORT).show()
                    Log.d(Constant.TAG_Coroutine, it.errorMessage.toString())
                }
            }
        })

    }
    //endregion

    //region Job {Couroutine }  for handling Viewpager2 to run continously
    fun initJOB() {

        //cancelJob()

        //region comment
//        viewpager2Job =   lifecycleScope.launch(Dispatchers.Main){
//
//
//
//           var delayViewpage2 = delayViewpager2()
//
//
//            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
//
//        }

        //endregion
        // Since COroutine Not Stop when I move from current page to another page,we use lifecycleScope which cancel
        //coroutine when Activity destroy but in our case DashBoard Activity Not Destroy it was in stack and new activity come on Top.

        // repeatOnLifecycle launches the block in a new coroutine every time the
        // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.


        viewpager2Job =   lifecycleScope.launchWhenResumed {

               val job = CoroutineScope(Dispatchers.IO).launch {
                    delayViewpager2()
                }

                job.join()

                viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)


        }

          //  or
        // region Comment :  lifecycle.coroutineScope.launch or lifecycleScope.launch
         /*
        lifecycle.coroutineScope.launch(Dispatchers.Main) {

            repeatOnLifecycle(Lifecycle.State.STARTED) {


                val job = CoroutineScope(Dispatchers.IO).launch {
                    delayViewpager2()
                }

                job.join()

                viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
            }

        }

          */







        // Note : For UI update we must to come the other thered To "Main Thread" Only
//        withContext(Dispatchers.Main){
//
//            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
//
//        }




    }

    suspend fun delayViewpager2(){
        delay((4*1000))
        Log.d("VIEWPAGER", "Coroutine viewPager Current Item position " + viewPager2.currentItem)


    }

    fun cancelJob() {
        viewpager2Job?.cancel()
    }
    //endregion

    //region ViewPager2 Setup
    private fun loadViewPager(listInsur: MutableList<DashboardEntity>){



        dashBoardAdapter = DashBoardAdapter(listInsur,viewPager2)
        viewPager2.adapter = dashBoardAdapter


        setupCarousel(listInsur)


        // binding.includedHomeMain.rvImgSlide.isNestedScrollingEnabled = false


    }

    private fun setupCarousel(listInsur: List<DashboardEntity>){

        viewPager2.offscreenPageLimit = 3
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false

        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager2.setPageTransformer(CarouselTransformer(this))




        viewPager2.registerOnPageChangeCallback(

            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                   // Log.d("VIEWPAGER", "Coroutine viewPager page Selection triggered " + position)

//                    sliderHandler.removeCallbacks(sliderRun)
//                    sliderHandler.postDelayed(sliderRun, 3000)

                      initJOB()

//                    lifecycleScope.launchWhenStarted {
//
//                        Log.d("VIEWPAGER", "launchWhenStarted " + position)
//                    }
//
//                    lifecycleScope.launchWhenResumed {
//
//                        Log.d("VIEWPAGER", "launchWhenResumed " + position)
//                    }
//
//                    lifecycleScope.launchWhenCreated {
//
//                        Log.d("VIEWPAGER", "launchWhenCreated " + position)
//                    }
//

                }


            }

        )

    }

    //endregion

    fun navigateDashboardMenu(menuEntity: DashboardMenu){


        when(menuEntity.id){

//            "1" -> {
//                startActivity(Intent(this, HomePageActivity::class.java))
//            }
//            "2" -> {
//                startActivity(Intent(this, CommonWebViewActivity::class.java))
//            }
//            "3" -> {
//                startActivity(Intent(this, WebViewDemoActivity::class.java))
//            }

            "4" -> {
                startActivity(Intent(this, ActivityResultLauncherDemoActivity::class.java))
            }
            "5" -> {
                startActivity(Intent(this, MultiplePermissionActivity::class.java))
            }

            "6" -> {
                startActivity(Intent(this, OpenAnotherActivity::class.java))
              //  startActivity(Intent(this, CallerDialogActivity::class.java))


                //CallerDialogActivity
            }


        }

    }


    //region Runnable Not in Used
    // for creating Runnable
    private val  sliderRun = Runnable {

        Log.d("VIEWPAGER", "viewPager Current Item position " + viewPager2.currentItem)

        viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)



    }

    private fun setupCarouselOLD(listInsur: List<DashboardEntity>){

        viewPager2.offscreenPageLimit = 3
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false

        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager2.setPageTransformer(CarouselTransformer(this))




        viewPager2.registerOnPageChangeCallback(

            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    sliderHandler.removeCallbacks(sliderRun)
                    sliderHandler.postDelayed(sliderRun, 3000)


                }


            }

        )

    }


    fun stopViewPager()  {

        if (sliderRun != null) {

                sliderHandler.removeCallbacks(sliderRun)

        }



    }

    fun getSliderImagePosition(position: Int)  {


        // Toast.makeText(requireContext(),"Pos"+position ,Toast.LENGTH_SHORT).show()

        viewPager2.setCurrentItem(position, true)


    }
    //getParallelAPIForUser_Dasbboard

    //endregion


}