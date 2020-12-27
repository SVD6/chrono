package com.example.chrono.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chrono.R
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.main.stopwatch.StopwatchFrag
import com.example.chrono.main.timer.CircuitFrag
import com.example.chrono.main.timer.CircuitDashboardFrag
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.PreferenceManager
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity() {

    var pager: ViewPager? = null // ViewPager where the fragments sit
    private var bind: ActivityMainBinding? = null // Bind variable for the activity
    private var tablay: TabLayout? = null // The timer/stopwatch navigation tab layout

    lateinit var circuitFrag: CircuitFrag
    lateinit var circuitDashFrag: CircuitDashboardFrag
    lateinit var stopwatchFrag: StopwatchFrag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        PreferenceManager.with(this)

        // Initialize Frags
        circuitFrag = CircuitFrag()
        circuitDashFrag = CircuitDashboardFrag()
        stopwatchFrag = StopwatchFrag()

        // Set the pager and tab layouts by finding them in the bound layout
        pager = bind!!.pager
        tablay = bind!!.tablayout

        // Set the adapter of the viewpager (our custom fragment adapter below)
        val fragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        pager!!.adapter = fragmentAdapter

        // Tells the tablayout to follow the viewpager
        tablay!!.setupWithViewPager(pager)

        // Change up some UI elements based on light/dark mode
        if (isUsingNightModeResources()) {
            tablay!!.background = ContextCompat.getDrawable(this, R.drawable.nav_tab_dark)
        } else {
            tablay!!.background = ContextCompat.getDrawable(this, R.drawable.nav_tab_light)
        }
    }

    // Our custom tab fragment adapter
    private inner class TabFragmentAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            // Code to switch between the two fragments based on position
            return when (position) {
                0 -> {
                    circuitFrag
                }
                else -> {
                    stopwatchFrag
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }

        // Tablayout uses this function to get the titles of the tabs
        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Timer"
                else -> {
                    return "Stopwatch"
                }
            }
        }
    }
}


