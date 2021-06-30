package view.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import com.kaopiz.kprogresshud.KProgressHUD
import view.adapter.DeviceAdapter
import view.adapter.SceneAdapter
import viewModel.HomeViewModel
import java.io.Serializable


class HomeActivity : AppCompatActivity() {

    private lateinit var sceneTitle : TextView
    private lateinit var noDataTitle : TextView
    private lateinit var scenesRecyclerView: RecyclerView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var sceneAdapter : SceneAdapter
    private lateinit var deviceAdapter : DeviceAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var loader: KProgressHUD

    private var deviceLoaded : Boolean = false
    private var sceneLoaded : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        bind()
        init()
        setUpDeviceRv()
        setUpSceneRv()
        setUpObserver()
        setUpProfilBtn()
        homeViewModel.getAllDevice()
        homeViewModel.getAllScene()
    }

    private fun bind() {
        sceneTitle = findViewById(R.id.scene_list_tv)
        noDataTitle = findViewById(R.id.home_no_device_tv)
        devicesRecyclerView = findViewById(R.id.list_device_rv)
        scenesRecyclerView = findViewById(R.id.list_scene_rv)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.context = this
    }

    private fun init() {
        title = ""

       loader = KProgressHUD.create(this)
       loader.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
    }

    private fun setUpDeviceRv() {
        devicesRecyclerView.layoutManager = GridLayoutManager(this, 3)
        deviceAdapter = DeviceAdapter()
        deviceAdapter.homeVM = homeViewModel
        devicesRecyclerView.adapter = deviceAdapter
    }

    private fun setUpSceneRv() {
        scenesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        sceneAdapter = SceneAdapter()
        sceneAdapter.homeVM = homeViewModel
        scenesRecyclerView.adapter = sceneAdapter
    }

    private fun setUpObserver() {
        homeViewModel.deviceList.observe(this, Observer {
            deviceAdapter.deviceList = it
            deviceLoaded = true
            checkLoaded()
            sceneAdapter.deviceList = deviceAdapter.deviceList
            loader.dismiss()
        })
        homeViewModel.sceneList.observe(this, Observer {
            sceneAdapter.sceneList = it
            sceneLoaded = true
            checkLoaded()
        })
    }

    private fun setUpProfilBtn() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_user_params)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun checkLoaded() {
        if(sceneLoaded && deviceLoaded) {
            if(deviceAdapter.deviceList?.isEmpty() == true) {
                sceneTitle.visibility = View.GONE
                devicesRecyclerView.visibility = View.GONE
                scenesRecyclerView.visibility = View.GONE
                noDataTitle.visibility = View.VISIBLE
            } else {
                noDataTitle.visibility = View.GONE
                sceneTitle.visibility = View.VISIBLE
                devicesRecyclerView.visibility = View.VISIBLE
                scenesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.scene_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        android.R.id.home -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }

        R.id.scene_list_refresh -> {
            homeViewModel.getAllDevice()
            homeViewModel.getAllScene()
            loader.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show()
            deviceAdapter.notifyDataSetChanged()
            sceneAdapter.notifyDataSetChanged()
            true
        }

        R.id.scene_list_add_btn -> {
            alertForCreations()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun alertForCreations() {
        val listOfAction: List<String> = listOf(getString(R.string.home_add_new_device), getString(R.string.home_add_new_scene), getString(R.string.home_add_new_room))
        val listOfActionToCharSequence = listOfAction.toTypedArray<CharSequence>()

        AlertDialog.Builder(this)
            .setItems(listOfActionToCharSequence) { dialog, which ->
                when(listOfActionToCharSequence[which]) {
                    getString(R.string.home_add_new_device) -> {
                        val supportedDeviceIntent = Intent(this, SupportedAccessoriesListActivity::class.java)
                        startActivity(supportedDeviceIntent)
                        dialog.dismiss()
                    }
                    getString(R.string.home_add_new_scene) -> {
                        val createSceneIntent = Intent(this, CreateSceneActivity::class.java)
                        val args = Bundle()
                        args.putSerializable("ARRAYLIST", deviceAdapter.deviceList as Serializable)
                        createSceneIntent.putExtra("BUNDLE", args)
                        startActivityForResult(createSceneIntent, 1)
                    }
                    getString(R.string.home_add_new_room) -> {
                        alertForCreateNewRoom()
                    }
                }
            }
            .show()
    }

    private fun alertForCreateNewRoom() {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_view_add_room, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.home_add_new_room))
            .setView(view)

        val editText = view.findViewById<EditText>(R.id.custom_view_add_room_et)
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }
        editText.filters = arrayOf(filter)

        dialogBuilder.setPositiveButton("Créer") { _, _ ->
            if (editText.text.isNotEmpty()) {
                homeViewModel.addRoom(editText.text.toString())
            } else {
                Toast.makeText(this, "Veuillez remplir le champ", Toast.LENGTH_SHORT).show()
            }

        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                homeViewModel.getAllScene()
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