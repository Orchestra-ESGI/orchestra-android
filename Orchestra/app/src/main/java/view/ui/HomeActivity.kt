package view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import view.adapter.DeviceAdapter
import view.adapter.SceneAdapter
import viewModel.DeviceViewModel
import viewModel.HomeViewModel
import viewModel.SceneViewModel


class HomeActivity : AppCompatActivity() {

    private lateinit var scenesRecyclerView: RecyclerView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var sceneAdapter : SceneAdapter
    private lateinit var deviceAdapter : DeviceAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sceneViewModel: SceneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene_list)

        bind()
        setUpDeviceRv()
        setUpSceneRv()
        setUpObserver()
        setUpProfilBtn()
        homeViewModel.getAllDevice()
    }

    private fun bind() {
        devicesRecyclerView = findViewById(R.id.list_device_rv)
        scenesRecyclerView = findViewById(R.id.list_scene_rv)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.context = this
        sceneViewModel = ViewModelProviders.of(this).get(SceneViewModel::class.java)
    }

    private fun setUpDeviceRv() {
        devicesRecyclerView.layoutManager = GridLayoutManager(this, 3)
        deviceAdapter = DeviceAdapter()
        devicesRecyclerView.adapter = deviceAdapter
        deviceAdapter.homeVM = homeViewModel
    }

    private fun setUpSceneRv() {
        scenesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        sceneAdapter = SceneAdapter()
        scenesRecyclerView.adapter = sceneAdapter
    }

    private fun setUpObserver() {
        homeViewModel.deviceList.observe(this, Observer {
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

        R.id.scene_list_refresh -> {
            homeViewModel.getAllDevice()
            true
        }

        R.id.scene_list_add_btn -> {
            val listOfAction: List<String> = listOf("Ajouter un objet", "Ajouter une scène")
            val listOfActionToCharSequence = listOfAction.toTypedArray<CharSequence>()

            val alertDialog: AlertDialog = this@HomeActivity.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    this.setItems(listOfActionToCharSequence) { _, which ->
                        val selected = listOfActionToCharSequence[which]
                        if (selected == "Ajouter un objet") {
                            val supportedDeviceIntent = Intent(context, SupportedAccessoriesListActivity::class.java)
                            startActivity(supportedDeviceIntent)
                        } else {
                            val createSceneIntent = Intent(context, CreateSceneActivity::class.java)
                            startActivityForResult(createSceneIntent, 1)
                        }
                    }
                }
                builder.create()
            }
            alertDialog.show()
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

    override fun onBackPressed() {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }


}