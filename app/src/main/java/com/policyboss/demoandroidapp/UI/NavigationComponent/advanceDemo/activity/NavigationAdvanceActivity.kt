package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.NavigationComponent.PushNotificationEntity
import com.policyboss.demoandroidapp.databinding.ActivityNavigationAdvanceBinding
import dagger.hilt.android.AndroidEntryPoint

/*********************************setupActionBarWithNavController *******************/
/*
 Note :  findNavController()( ie navController )we can't use in toolbar case bec in normal case all fragment are connected via
 inside the fragmentContainer view but activity toolbar is seperate hence we have to manually add
  1>  First find navHostFragment manually using ID bec we are in Activity.

  val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

  2> get navController using navHostFragment
   navController = navHostFragment.navController

  3> add  setupActionBarWithNavController(navController)


    onBackPressedDispatcher.hasEnabledCallbacks()
    This checks if there are any callbacks registered with the onBackPressedDispatcher
 */

///***********************  toolbar  *******************/
/*
    1> set toolbar using :-->
      setSupportActionBar(binding.toolbar)

      For Handing Toolbar default button ie <----
       Step we have two option


      1> Use either
      If you're using a custom Toolbar component within a fragment and have data binding enabled:

       binding.toolbar.setupWithNavController(navController)

       2> or
       If you're using the default action bar provided by the activity and don't have data binding: Use setupActionBarWithNavController directly in your activity's code.

       setupActionBarWithNavController(navController)

       override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp() || super.onSupportNavigateUp()
       }

       // dont know much below

         NavigationUI.setupActionBarWithNavController(this, navController)
 */


interface NavigationAdvanceCallback {
    fun hideAppBar()
    fun showAppBar()
}

@AndroidEntryPoint
class NavigationAdvanceActivity : BaseActivity(),NavigationAdvanceCallback {

   private lateinit var binding : ActivityNavigationAdvanceBinding
   private lateinit var layout: View

   // For Toolbar in Navigation Controller
   private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener

    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationAdvanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layout = binding.root

        /***** For Tolbar Adding **************/

        setSupportActionBar(binding.toolbar)


        // For Showing Toolbar in Fragment
        // Find FragmentContainerView using its  id here nav_host_fragment  and set it in setupActionBarWithNavController
        // Get the NavHostFragment

        //  navController = findNavController(R.id.nav_host_fragment)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Get the NavController associated with the NavHostFragmen
        navController = navHostFragment.navController

        //or
      //  navController = navHostFragment.findNavController()


        //region Create an AppBarConfiguration with the top-level destinations in your graph
        /*

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        // Set up the action bar with the NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

         */

        //endregion

        // region Mark : optional use toolbar or setupActionBarWithNavController

       // binding.toolbar.setupWithNavController(navController)


        // 1>Mark : use Below or if req. appBar Configuration than add in param
//        /********************** use this **********************************/
       // setupActionBarWithNavController(navController)
        /*
           above code is ways to set up the toolbar back button to navigate up the back stack in a Navigation graph.
         */
        //endregion


      //  or with appBar use below { Note : we need this bec at bottom navigation we dont need
      //                              toolbar up button hence to remove this we add below}

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeDashBoardFragment, R.id.settingFragment, R.id.notificationFragment),     // for removing  toolbar up button arrow we add this
            binding.drawerLayout    // Adding drawerLayout in App bar ie for Hamburger icon to show
        )
       // or   2>add navController and appBar which have bottom nav and Navigattion Drawer  into toolbar
       // If you're using the default action bar provided by the activity and don't have data binding
        setupActionBarWithNavController(navController,appBarConfiguration)

      //  or we can used
//If you're using a custom Toolbar component within a fragment and have data binding enabled:
      //  binding.toolbar.setupWithNavController(navController,appBarConfiguration)

       //Set Only Toolbar usinf  navController
//        val appBarConfiguration = AppBarConfiguration(navController.graph)



        // For Bottom nav connect to navController
        binding.bottomNavView.setupWithNavController(navController)

        // For NavigationView : Drawer layout
        binding.navDrawer.setupWithNavController(navController )


        // For NavigationUp Handling Using Toolbar
        binding.toolbar.setNavigationOnClickListener {

            when(navController.currentDestination?.id){
                R.id.sendCashFragment -> {

                    //Note : here we check sendCashFragment has any onBackPressedDispatcher
                    // if has than handle by its onBackPressedDispatcher callback written in sendCashFragment
                    if (onBackPressedDispatcher.hasEnabledCallbacks())
                       onBackPressedDispatcher.onBackPressed()



                    else
                        navController.navigateUp()
                }
                else -> navController.navigateUp()
            }
        }



        listener =  NavController.OnDestinationChangedListener{contoller, destination , argument ->

            when(destination.id){

                R.id.startFragment -> {
                   setHomeToolbar()
                   // showToast("Home")
                }
                R.id.homeDashBoardFragment -> {
                    includeBottomNavigation()
                }
                R.id.settingFragment -> {
                    includeBottomNavigation()
                }
                R.id.notificationFragment -> {
                    includeBottomNavigation()
                }

                else -> {
                    excludeBottomNavigation()
                   // showToast("Other")
                }
            }
        }



        handlinkNotification()
    }


    fun handlinkNotification(){

        if (intent.hasExtra(Constant.PUSH_NOTITIFICATION)) {
            val pushNotificationEntity = intent.getParcelableExtra<PushNotificationEntity>(Constant.PUSH_NOTITIFICATION)

            Log.d(Constant.TAG, "Notification: ${pushNotificationEntity?.header} and ${pushNotificationEntity?.message}")

            // Create a bundle to pass data to the destination fragment
            val bundle = Bundle().apply {

                putString(Constant.NOTIFICATION_RECEIVERNAME,pushNotificationEntity?.message?:"No Data")
            }

            // Navigate to the desired destination in your navigation graph

            navController.navigate(R.id.sendCashFragment, bundle)
        }
    }

    private fun setHomeToolbar() {
        binding.toolbar.let {
            it.logo = ContextCompat.getDrawable(this, R.drawable.ic_bank_24)
            it.title = "Hi,"
            it.setTitleTextAppearance(this, R.style.semibold_regular_white)
            it.subtitle = "subtitle Data"
            it.setSubtitleTextAppearance(this, R.style.sub_textview_white_12)
            it.background = ContextCompat.getDrawable(this, R.color.blue)
            it.setTitleMargin(80, 0, 0, 0)
        }
    }

    private fun excludeBottomNavigation() {
        hideBottomNavigation()

        binding.toolbar.let {
            //it.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_backarrow)
            it.subtitle = null
            it.logo = null
            it.setTitleTextAppearance(this, R.style.semibold_primary_16)
            it.background = ContextCompat.getDrawable(this, R.color.red_custom)
            it.setTitleMargin(0, 0, 0, 0)
            // it.setNavigationOnClickListener {
            // val action = MobileRechargeStatusFragmentDirections.actionPopToHome()
            // navController.navigate(action)

            // onBackPressed()
            // }
        }
    }

    private fun includeBottomNavigation() {
        showBottomNavigation()

        binding.toolbar.let {
            it.subtitle = null
            it.logo = null
            it.setTitleTextAppearance(this, R.style.semibold_primary_16)
            it.background = ContextCompat.getDrawable(this, R.color.red_custom)
            it.setTitleMargin(0, 0, 0, 0)
        }
    }

    fun hideBottomNavigation() {
        binding.bottomNavView.visibility = View.GONE
    }
    fun showBottomNavigation() {
        binding.bottomNavView.visibility = View.VISIBLE
    }



    override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

        // Note : we added (appBarConfiguration) bec it used to click on default hamburger icon
    }

    // region Menu WHen Apply on Activity
    /*
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        menuInflater.inflate(R.menu.main_menu,menu)
//        return super.onCreateOptionsMenu(menu)
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//      return when(item.itemId) {
//
//
//
//          R.id.item_about_app -> {
//
//              val action = NavGraphDirections.actionGlobalAboutAppFragment()
//              navController.navigate(action)
//
//              return true
//          }
//
//          else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//      }
//    }

*/
    //endregion

    override fun onResume() {
        navController.addOnDestinationChangedListener(listener)
        super.onResume()
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
    }

      /*
        For handling Navigation back from Toolbar add below code
       */



    override fun onBackPressed() {
        super.onBackPressed()

        // onBackPressedDispatcher.onBackPressed()
//        if (getOnBackPressedDispatcher().hasEnabledCallbacks()) {
//            super.onBackPressed()  // dispatch event to custom callback, which implemented in fragment
//        } else {
//            // use activity backPressed if there is no callback          added to mOnBackPressedDispatcher
//
//            Toast.makeText(this@NavigationAdvanceActivity,"Back press from activity", Toast.LENGTH_SHORT).show()
//
//            this.finish()
//        }



//        if (supportFragmentManager.backStackEntryCount > 0) {
//            // Handle fragment back navigation
//            supportFragmentManager.popBackStack()
//        } else {
//            // Handle activity back navigation
//            onBackPressedDispatcher.onBackPressed()  // This checks if there are any callbacks registered with the onBackPressedDispatcher
//        }


        if(onBackPressedDispatcher.hasEnabledCallbacks()){  //// This checks if there are any callbacks registered with the onBackPressedDispatcher

            onBackPressedDispatcher.onBackPressed()
           // moveTaskToBack(true)
        }else{

            navController.navigateUp()
        }


        //region using backpress Handle Fragment : But we can also handle via that fragment callback
        // supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = findNavController(R.id.nav_host_fragment)
//
//        // Check if the current destination is actually the start destination (Home screen)
//        if (navController.graph.startDestinationId == navController.currentDestination?.id) {
//            // Check if back is already pressed. If yes, then exit the app.
//            if (backPressedOnce) {
//                //super.onBackPressed()
//                onBackPressedDispatcher.onBackPressed()
//                return
//            }
//
//            backPressedOnce = true
//            hideKeyboard(binding.root)
//            showSnackbar(binding.root, "Press back again to Exit")
//
//            Handler(Looper.getMainLooper()).postDelayed(2000) {
//                backPressedOnce = false
//            }
//        }
//
//        else if(navController.currentDestination?.id == R.id.sendCashFragment){
//            var prevId = 0
//            prevId = navController.previousBackStackEntry?.destination?.id ?: 0
//
//            showToast("Fom Toolbar")
//        }
        //endregion
    }

    override fun hideAppBar() {
        binding.appBarLayout?.visibility = View.GONE
    }

    override fun showAppBar() {
        binding.appBarLayout?.visibility = View.VISIBLE
    }


}