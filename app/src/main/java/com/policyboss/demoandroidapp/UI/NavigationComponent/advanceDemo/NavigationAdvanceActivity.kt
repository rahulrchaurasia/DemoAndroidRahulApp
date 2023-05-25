package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.showAlerDialog
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


       // binding.toolbar.setupWithNavController(navController)


        setupActionBarWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
    private fun setupNavigationViews() {
        navController = findNavController(R.id.nav_host_fragment)

      //  or
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController


        // For Bottom Navigation
        // binding.bottomNavView.setupWithNavController(navController)

//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.startFragment,
//                R.id.homeDashBoardFragment,
//                R.id.chooseReceiverFragment,
//                R.id.sendCashFragment
//
//            )
//        )


        appBarConfiguration = AppBarConfiguration(
            navController.graph
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
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