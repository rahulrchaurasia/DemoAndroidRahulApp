package com.policyboss.demoandroidapp.UI.RecyclerViewOperation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.policyboss.demoandroidapp.UI.RecyclerViewOperation.model.SectionData
import com.policyboss.demoandroidapp.databinding.ActivityScrollViewWithTabBinding
import kotlin.math.min

class RecycleMultiViewTypeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityScrollViewWithTabBinding

    private lateinit var tabLayout: TabLayout

    private lateinit var layoutManager: LinearLayoutManager
    lateinit var multViewTypeAdpter : MulipleViewTypeAdapter

    private val sections = listOf(
        SectionData("Section 1"),
        SectionData("Section 2", listOf("childSect2.1", "childSect2.2", "childSect2.3", "childSect2.4")),
        SectionData("Section 3"),
        SectionData("Section 4", listOf("childSect4.1", "childSect4.2", "childSect4.3", "childSect4.4")),

        SectionData("Section 5", listOf("childSect5.1", "childSect5.2", "childSect5.3", "childSect5.4")),
        SectionData("Section 6", listOf("childSect6.1", "childSect6.2", "childSect6.3", "childSect6.4")),


        SectionData("Section 7", listOf("childSect5.1", "childSect5.2", "childSect5.3", "childSect5.4")),
        SectionData("Section 8", listOf("childSect6.1", "childSect6.2", "childSect6.3", "childSect6.4")),
        SectionData("Section M"),

        SectionData("Section 5", listOf("childSect5.1", "childSect5.2", "childSect5.3", "childSect5.4")),
        SectionData("Section 6", listOf("childSect6.1", "childSect6.2", "childSect6.3", "childSect6.4")),
        SectionData("Section 7", listOf("childSect7.1", "childSect7.2", "childSect7.3", "childSect7.4")),
        SectionData("Section 8"),
        SectionData("Section 8", listOf("childSect8.1", "childSect8.2", "childSect7.3", "childSect7.4")),
        SectionData("Section 9"),
        SectionData("Section 9", listOf("childSect9.1", "childSect9.2", "childSect7.3", "childSect7.4")),
        SectionData("Section 10"),
        SectionData("Section 10", listOf("childSect10.1", "childSect10.2", "childSect7.3", "childSect7.4")),

        // Add more sections...
    )
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollViewWithTabBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tabLayout = binding.tabLayout


        layoutManager = LinearLayoutManager(this@RecycleMultiViewTypeActivity) // Initialize the layout manager
        binding.rvMultiViewType.layoutManager = layoutManager // Set the layout manager to the RecyclerView

        bindRecyclerView()


        // Create tabs for each section
        sections.forEach { section ->
            val tab = tabLayout.newTab().setText(section.title)
            tabLayout.addTab(tab)
        }




        // Handle tab selection changes
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                val position = tab.position
//                scrollToPosition(position)
               // scrollView.smoothScrollTo(0, calculateScrollYForSection(position))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }


        })


//        binding.rvMultiViewType.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//
//        })

        binding.rvMultiViewType.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dx == 0 && dy == 0) return // No scrolling, do nothing



                val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                val firstVisibleItem = layoutManager.findViewByPosition(firstVisibleItemPosition)

                val totalItemCount = layoutManager.itemCount

                // Ensure there's at least one item and it's completely visible
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION && firstVisibleItem != null) {
                    val visibleHeight = recyclerView.height - recyclerView.paddingTop - recyclerView.paddingBottom
                    val itemHeight = firstVisibleItem.height

                    val visiblePercentage = itemHeight.toFloat() / visibleHeight.toFloat()
                    val itemTop = firstVisibleItem.top

                    val itemVisibility = if (dy > 0) {
                        (itemTop + itemHeight).toFloat() / itemHeight.toFloat()
                    } else {
                        (visibleHeight - itemTop).toFloat() / itemHeight.toFloat()
                    }

                    // Determine the selected tab based on the scroll position
                    val selectedTabPosition = calculateSelectedTabPosition(itemVisibility, firstVisibleItemPosition, totalItemCount)

                    // Select the tab
                    tabLayout.getTabAt(selectedTabPosition)?.select()
                }
            }

            // Calculate the position of the first visible item


        })
    }

    // Update selected tab based on scroll position




    private fun bindRecyclerView(){

        multViewTypeAdpter = MulipleViewTypeAdapter(this,sections)

        binding.rvMultiViewType.apply {

            setHasFixedSize(true)
           // layoutManager = LinearLayoutManager(this@RecycleMultiViewTypeActivity)
            adapter = multViewTypeAdpter
        }
    }

    private fun scrollToPosition(position: Int) {
        // Calculate the position to scroll to based on the selected tab
        // For example, you can use LinearLayoutManager to scroll to the desired position
        val layoutManager = binding.rvMultiViewType.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)
    }

    private fun calculateSelectedTabPosition(itemVisibility: Float, firstVisibleItemPosition: Int, totalItemCount: Int): Int {
        // Adjust this threshold according to your requirements
        val threshold = 0.5f

        return if (itemVisibility >= threshold) {
            firstVisibleItemPosition
        } else {
            // Ensure that we don't exceed the total number of items
            min(firstVisibleItemPosition + 1, totalItemCount - 1)
        }
    }

}