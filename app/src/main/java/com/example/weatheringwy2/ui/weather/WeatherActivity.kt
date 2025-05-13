package com.example.weatheringwy2.ui.weather


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.weatheringwy2.ui.place.MyPlaceViewModel
import com.example.weatheringwy2.R
import com.example.weatheringwy2.databinding.ActivityWeatherBinding
import com.example.weatheringwy2.logic.model.Place
import com.example.weatheringwy2.ui.place.MyPlaceFragment
import kotlin.collections.ArrayList

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this).get(MyPlaceViewModel::class.java) }
    lateinit var placeFragmentViewPager2Adapter : PlaceFragmentViewPager2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //加载布局
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // 实现沉浸式布局1
//        val decorView = window.decorView
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE // 让系统的Activity延展到状态栏，但这时状态栏应该还是盖住了activity
        window.statusBarColor = Color.TRANSPARENT // 让状态栏透明
        // 还需要让xml设置android:fitsSystemWindows="true"，让页面往下padding一点
        enableEdgeToEdge(); // 实现沉浸式布局2
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(
//                systemBars.left,
//                systemBars.top,
//                systemBars.right,
//                systemBars.bottom
//            )
//            insets
//        }

        //给ViewPager需要的一些东西初始化
        val placeFragmentList = ArrayList<Place>()
        placeFragmentViewPager2Adapter = PlaceFragmentViewPager2Adapter(placeFragmentList,this)
        binding.viewPager.adapter = placeFragmentViewPager2Adapter
        binding.viewPager.offscreenPageLimit = 1

        //观察数据库中的所有元素，如果变化了，我们对应更改我们的Fragment，实际上这里存在一些问题，就是增删与我们的实际情况不符
        viewModel.refreshResult.observe(this) { places ->
            viewModel.myPlaceList.clear()
            viewModel.myPlaceList.addAll(places)
            placeFragmentViewPager2Adapter.placeFragmentList = places
            Log.d("lxl", "${placeFragmentViewPager2Adapter.idSet}")
//            for (place in places) {
//                Log.d("lxl", "place:${place}")
//                placeFragmentList.add(place)
//            }

            placeFragmentViewPager2Adapter.notifyDataSetChanged()
        }

        //刷新一下，让myPlaceList中有数据,进而更新各个fragment
        viewModel.refresh()

        //这里由于异步所以myPlaceList为空 Log.d("lxl", "${viewModel.myPlaceList}")
        /*暂时找不到上一次存放的位置，先设置显示第一页*/
        binding.viewPager.setCurrentItem(0,true)


        //页面切换时候执行的逻辑，主要是保存一下页面作为当前页面，此外
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.saveSharedPreferencesPlace(placeFragmentViewPager2Adapter.placeFragmentList[position])
                //这种方法用不了，我猜测是因为此时fragment还没创建好

                //这样通知我的常用列表，数据变了，重新修改每一项的内容，把原来不是当前页的变为当前页
                val myPlaceFragment = supportFragmentManager.findFragmentById(R.id.myPlaceFragment) as MyPlaceFragment
                myPlaceFragment.getAdapter().notifyDataSetChanged()
                super.onPageSelected(position)
            }
        })

    }

    //换页，主要是外部切换页面的时候需要用
    fun changePage(position:Int){
        binding.viewPager.setCurrentItem(position,true)
    }

    // 打开左边的导航栏
    fun openDrawer(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    // 关闭左边的导航栏
    fun closeDrawer(){
        binding.drawerLayout.closeDrawers()
    }

    // 禁止用户输入
    fun changeUserInputEnabled(flag:Boolean){
        binding.viewPager.isUserInputEnabled = flag
    }
}

//给左边的drawLayout加上关闭时间，也就是说，关闭的时候要观赏输入栏，但是现在已经不输入东西了不需要加了
/*drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

    override fun onDrawerOpened(drawerView: View) {}

    override fun onDrawerClosed(drawerView: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
    override fun onDrawerStateChanged(newState: Int) {}
})*/