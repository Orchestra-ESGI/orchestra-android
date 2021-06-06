package view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.mock.FakeObjectDataService
import core.rest.model.Device
import core.rest.model.Scene
import view.adapter.DeviceAdapter
import view.adapter.SceneAdapter
import viewModel.DeviceViewModel
import viewModel.SceneViewModel


class SceneListActivity : AppCompatActivity() {

    private lateinit var scenesRecyclerView: RecyclerView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var sceneAdapter : SceneAdapter
    private lateinit var deviceAdapter : DeviceAdapter
    private lateinit var deviceViewModel : DeviceViewModel
    private lateinit var sceneViewModel: SceneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene_list)

        bind()
        setUpDeviceRv()
        setUpSceneRv()
        setUpObserver()
        setUpProfilBtn()
    }

    private fun bind() {
        devicesRecyclerView = findViewById(R.id.list_device_rv)
        scenesRecyclerView = findViewById(R.id.list_scene_rv)
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        sceneViewModel = ViewModelProviders.of(this).get(SceneViewModel::class.java)
    }

    private fun setUpDeviceRv() {
        devicesRecyclerView.layoutManager = GridLayoutManager(this, 3)
        deviceAdapter = DeviceAdapter()
        devicesRecyclerView.adapter = deviceAdapter
    }

    private fun setUpSceneRv() {
        scenesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        sceneAdapter = SceneAdapter()
        scenesRecyclerView.adapter = sceneAdapter
    }

    private fun setUpObserver() {
        deviceViewModel.deviceList.observe(this, Observer {
            deviceAdapter.deviceList = it
        })
        sceneViewModel.sceneList.observe(this, Observer {
            sceneAdapter.sceneList = it
        })
    }

    private fun setUpProfilBtn() {
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
                sceneViewModel.getScenes()
            }
        }
    }


}