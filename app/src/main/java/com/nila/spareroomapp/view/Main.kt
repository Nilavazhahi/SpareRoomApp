package com.nila.spareroomapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.nila.spareroomapp.R

class Main : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    private var adapter : RoomTabAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        tabLayout = findViewById(R.id.roomtablayout)
        viewPager = findViewById(R.id.viewPager)

        if (supportActionBar == null) {
            setSupportActionBar(toolbar)
        } else toolbar!!.visibility = View.GONE
        supportActionBar!!.title = "SpeedRoommating"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.elevation = 0f

        viewPager!!.offscreenPageLimit = 3

        adapter =  RoomTabAdapter(supportFragmentManager, this)
        adapter!!.addFragment(UpcomingFragment(), "UPCOMING")
        adapter!!.addFragment(UpcomingFragment(), "ARCHIVED")
        adapter!!.addFragment(UpcomingFragment(), "OPTIONS")

        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)

        highLightCurrentTab(0)
        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                highLightCurrentTab(position) // for tab change
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    private fun highLightCurrentTab(position: Int) {
        for (i in 0 until tabLayout!!.tabCount) {
            val tab = tabLayout?.getTabAt(i)
            assert(tab != null)
            tab!!.customView = null
            tab.customView = adapter!!.getTabView(i)
        }
        val tab = tabLayout!!.getTabAt(position)
        assert(tab != null)
        tab!!.customView = null
        tab.customView = adapter!!.getSelectedTabView(position)
    }
}