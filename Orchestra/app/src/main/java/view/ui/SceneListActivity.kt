package view.ui

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.mock.FakeObjectDataService
import core.rest.model.Device
import core.rest.model.Scene
import view.adapter.DeviceAdapter
import view.adapter.SceneAdapter


class SceneListActivity : AppCompatActivity() {

    private lateinit var scenesRecyclerView: RecyclerView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var sceneAdapter : SceneAdapter
    private lateinit var deviceAdapter : DeviceAdapter

    var deviceList : ArrayList<Device> = ArrayList()
    var sceneList : ArrayList<Scene> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene_list)

        devicesRecyclerView = findViewById(R.id.list_device_rv)
        devicesRecyclerView.layoutManager = GridLayoutManager(this, 3)

        scenesRecyclerView = findViewById(R.id.list_scene_rv)
        scenesRecyclerView.layoutManager = GridLayoutManager(this, 2)

        deviceAdapter = DeviceAdapter()
        sceneAdapter = SceneAdapter()

        deviceList = FakeObjectDataService.getDevices()
        sceneList = FakeObjectDataService.getScenes()

        deviceAdapter.deviceList = deviceList
        sceneAdapter.sceneList = sceneList

        scenesRecyclerView.adapter = sceneAdapter
        devicesRecyclerView.adapter = deviceAdapter

        /*
        val sections: ArrayList<SectionedGridRecyclerViewAdapter.Section> = ArrayList()

        sections.add(SectionedGridRecyclerViewAdapter.Section(0, "Mes objets"))
        //sections.add(SectionedGridRecyclerViewAdapter.Section(4, "Mes scènes"))
        val arrSection = arrayOfNulls<SectionedGridRecyclerViewAdapter.Section>(sections.size)
        val mSectionedAdapter = SectionedGridRecyclerViewAdapter(
            this,
            R.layout.activity_scene_list,
            R.id.scene_list_title_tv,
            scenesRecyclerView,
            sceneAdapter
        )
        mSectionedAdapter.setSections(sections.toArray(arrSection))
        scenesRecyclerView.adapter = mSectionedAdapter

         */

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_user_params)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.scene_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            Toast.makeText(this, "Hello", Toast.LENGTH_LONG)
            Log.d("Pass", "Pass")
            true
        }

        R.id.scene_list_add_btn -> {
            val createSceneIntent = Intent(this, CreateSceneActivity::class.java)
            startActivityForResult(createSceneIntent, 1)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            if (resultCode === RESULT_OK) {
                val sceneDetail = data?.getSerializableExtra("CreatedScene") as? Scene
                sceneDetail?.let {
                    sceneList.add(it)
                }
                sceneAdapter.sceneList = sceneList
            }
        }
    }
}