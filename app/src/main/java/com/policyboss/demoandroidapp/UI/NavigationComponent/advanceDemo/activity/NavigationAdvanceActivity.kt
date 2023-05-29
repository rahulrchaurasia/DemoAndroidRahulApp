package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.policyboss.demoandroidapp.NavGraphDirections
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.showAlerDialog
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityNavigationAdvanceBinding

/*********************************setupActionBarWithNavController *******************/
/*
 Note :  findNavController()( ie navController )we can't use in toolbar case bec in normal case all fragment are connected via
 inside the fragmentContainer view but activity toolbar is seperate hence we have to manually add
  1>  First find navHostFragment manually using ID bec we are in Activity.

  val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

  2> get navController using navHostFragment
   navController = navHostFragment.navController

  3> add  setupActionBarWithNavController(navController)

 */

///***********************  toolbar  *******************/
/*
    1> set toolbar using :-->
      setSupportActionBar(binding.toolbar)

      For Handing Toolbar default button ie <----
      2> and 3> Step we have two option
      2> Use either
       binding.toolbar.setupWithNavController(navController)

       3> or
       setupActionBarWithNavController(navController)

       override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp() || super.onSupportNavigateUp()
       }
 */

class NavigationAdvanceActivity : AppCompatActivity() {

   private lateinit var binding : ActivityNavigationAdvanceBinding
   private lateinit var layout: View

   // For Toolbar in Navigation Controller
   private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener

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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Get the NavController associated with the NavHostFragmen
        navController = navHostFragment.navController


        /*
        // Create an AppBarConfiguration with the top-level destinations in your graph
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        // Set up the action bar with the NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

         */

          // Mark : optional use toolbar or setupActionBarWithNavController
       // binding.toolbar.setupWithNavController(navController)


        // 1>Mark : use Below or if req. appBar Configuration than add in param
//        /********************** use this **********************************/
       // setupActionBarWithNavController(navController)

      //  or with appBar use below { Note : we need this bec at bottom navigation we dont need
        // toolbar up button hence to remove this we add below

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeDashBoardFragment, R.id.settingFragment, R.id.notificationFragment),
            binding.drawerLayout    // Adding drawerLayout in App bar
        )
       // or   2>
        setupActionBarWithNavController(navController,appBarConfiguration)

        // For Bottom nav connect to navController
        binding.bottomNavView.setupWithNavController(navController)

        // For Drawer layout
        binding.navDrawer.setupWithNavController(navController )



        listener =  NavController.OnDestinationChangedListener{contoller, destination , argument ->

            when(destination.id){

                R.id.startFragment -> {
                   setHomeToolbar()
                   // showToast("Home")
                }
                R.id.homeDashBoardFragment -> {
                    includeBottomNavigation()
                }
                R.id.homeDashBoardFragment -> {
                    includeBottomNavigation()
                }
                R.id.homeDashBoardFragment -> {
                    includeBottomNavigation()
                }
                else -> {
                    excludeBottomNavigation()
                   // showToast("Other")
                }
            }
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

      return when(item.itemId) {

          R.id.item_about_app -> {

              val action = NavGraphDirections.actionGlobalAboutAppFragment()
              navController.navigate(action)

              return true
          }
          else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
      }
    }


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
        if (getOnBackPressedDispatcher().hasEnabledCallbacks()) {
            super.onBackPressed()  // dispatch event to custom callback, which implemented in fragment
        } else {
            // use activity backPressed if there is no callback          added to mOnBackPressedDispatcher

            Toast.makeText(this@NavigationAdvanceActivity,"Back press from activity", Toast.LENGTH_SHORT).show()

            this.finish()
        }
    }


}