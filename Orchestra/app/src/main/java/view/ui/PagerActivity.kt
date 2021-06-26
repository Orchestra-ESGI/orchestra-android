package view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.orchestra.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import view.adapter.PagerAdapter

class PagerActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager2
    private lateinit var tabDots : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        viewPager = findViewById(R.id.pager_view_pager)
        tabDots = findViewById(R.id.pager_tab_layout)
        viewPager.adapter = PagerAdapter(this)
        TabLayoutMediator(tabDots, viewPager) { tab, position ->

        }.attach()
    }
}