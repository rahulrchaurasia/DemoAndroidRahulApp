package com.policyboss.demoandroidapp.ActivityLifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.databinding.ActivityDemoLifeCycleSecondBinding

class DemoLifeCycleSecond  : BaseActivity() {


   lateinit var binding : ActivityDemoLifeCycleSecondBinding
    var strMessage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate invoked-Second")
        setContentView(R.layout.activity_demo_life_cycle_second)

        binding = ActivityDemoLifeCycleSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Alert Dialog Demo"


        }

        if (intent.hasExtra(Constant.DEMO_MESSAGE)) {
            strMessage = intent.getStringExtra(Constant.DEMO_MESSAGE) ?:""

            binding.txtMessage.setText("Transferred Data is $strMessage")
        }

        binding.btnNext.setOnClickListener{

          //  startActivity(Intent(this, ThirdDemoActivity::class.java))

        }
    }

    //region menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent: Intent
        when (item.itemId) {
            R.id.action_chat -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Life Cycle Method
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart invoked -Second")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume invoked-Second")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause invoked-Second")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop invoked-Second")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart invoked-Second")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy invoked-Second")
    }

    //endregion
}