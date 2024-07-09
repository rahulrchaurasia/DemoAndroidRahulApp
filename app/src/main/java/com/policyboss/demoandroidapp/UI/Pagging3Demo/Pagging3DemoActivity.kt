package com.policyboss.demoandroidapp.UI.Pagging3Demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.Pagging3Demo.pagging.QuotePagingAdapter
import com.policyboss.demoandroidapp.UI.Pagging3Demo.viewModel.QuoteViewModel
import com.policyboss.demoandroidapp.databinding.ActivityPagging3DemoBinding
import com.policyboss.demoandroidapp.databinding.ActivityRecyclerViewMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Pagging3DemoActivity : BaseActivity() {

    lateinit var binding : ActivityPagging3DemoBinding

    lateinit var quoteViewModel: QuoteViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: QuotePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPagging3DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Pagging 3 Demo"


        }

        setPaginData()
        responseListner()
    }

    fun setPaginData(){

        recyclerView = findViewById(R.id.quoteList)

        quoteViewModel = ViewModelProvider(this).get(QuoteViewModel::class.java)

        adapter = QuotePagingAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

    }



    private fun responseListner(){

        lifecycleScope.launch {
            quoteViewModel.list.collect{

                adapter.submitData(lifecycle,it)

            }

        }
    }

}