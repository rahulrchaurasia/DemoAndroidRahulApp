package com.policyboss.demoandroidapp.ActivityLifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityDemoLifeCycleThirdBinding

class FragmentDemoLifeCycleActivity : AppCompatActivity() {

    lateinit var binding : ActivityDemoLifeCycleThirdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoLifeCycleThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Activity :- onCreat invoked")
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Alert Dialog Demo"


        }
        binding.btnAddFrag.setOnClickListener{


               // openFragment(fragment = FragmentLifeCycleDdemo())
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragmentA = FragmentLifeCycleDdemo()

                fragmentTransaction

                    .add(R.id.fragment_container, fragmentA)
                    .addToBackStack(null)
                    .commit()

        }

        binding.btnReplaceAnotherdFrag.setOnClickListener{

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragmentB = FragmentLifeCycleDdemo2()

            fragmentTransaction
                .replace(R.id.fragment_container, fragmentB)
               // .add(R.id.fragment_container, fragmentB)
                 .addToBackStack(null)
                .commitAllowingStateLoss()


        }

        binding.btnCloseFrag.setOnClickListener{

            closeFragment()
        }
    }

    private fun openFragment(fragment: Fragment) {

        if(fragment != null && !fragment.isAdded) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragment_container, fragment)
           // fragmentTransaction.addToBackStack("HomePage")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }else{
            showToast("Fragment is Already added")
        }
    }

    private fun closeFragment() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            // No fragments in the back stack, handle as needed
        }
    }

    //region Life Cycle Method


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Activity :- onStart invoked")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Activity :-onResume invoked")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Activity :-onPause invoked")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Activity :-onStop invoked")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "Activity :-onRestart invoked")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Activity :-onDestroy invoked")
    }

    //endregion
}