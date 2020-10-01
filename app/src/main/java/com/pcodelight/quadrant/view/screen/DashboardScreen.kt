package com.pcodelight.quadrant.view.screen

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pcodelight.joindesign.AuthHelper
import com.pcodelight.listener.QuadrantDataListener
import com.pcodelight.model.LocationData
import com.pcodelight.qlretriever.QLRetriever
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.view.fragment.*
import com.pcodelight.quadrant.view.ui.TabItem
import com.pcodelight.quadrant.viewmodel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*

class DashboardScreen : AppCompatActivity() {
    private lateinit var dashboardViewModel: DashboardViewModel
    private val tabItems = listOf(
        Pair(R.drawable.ic_home, "Home"),
        Pair(R.drawable.ic_history, "History"),
        Pair(R.drawable.ic_insight, "Insight")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authToken = AuthHelper.instance.getAuthToken() ?: ""
        if (authToken.isBlank()) {
            gotoLoginScreen()
        } else {
            QLRetriever.init(this@DashboardScreen, applicationContext, authToken)
        }

        initViewModel()
        initView()
    }

    private fun initViewModel() {
        dashboardViewModel = ViewModelFactory().create(DashboardViewModel::class.java)
    }

    private fun initView() {
        vpHome.offscreenPageLimit = 2
        vpHome.adapter = PagerAdapter(supportFragmentManager,
            listOf(
                HomeSection(),
                LocationListSection(),
                MonthlyDataSection()
            )
        )

        tabItems.forEach {
            tlHome.addTab(tlHome.newTab().apply {
                customView = TabItem(this@DashboardScreen).apply {
                    bind(it.first, it.second)
                }
            })
        }

        tlHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                vpHome.currentItem = tab?.position ?: 0
            }
        })

        tlHome.setSelectedTabIndicator(null)
    }

    private fun gotoLoginScreen() {
        startActivity(Intent(this@DashboardScreen, LoginScreen::class.java))
        finish()
    }

    class PagerAdapter(fm: FragmentManager, private val fragmentList: List<Fragment>) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragmentList[position]
        override fun getCount(): Int = fragmentList.count()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.none { it == PackageManager.PERMISSION_DENIED }) {
            /**
             * re-init tracking activity since permissions already allowed
             */
            dashboardViewModel.initTracking()
        }
    }
}
