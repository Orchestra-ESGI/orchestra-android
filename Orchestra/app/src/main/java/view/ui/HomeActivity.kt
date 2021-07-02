package view.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kaopiz.kprogresshud.KProgressHUD
import core.rest.model.Scene
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.Room
import view.adapter.DeviceAdapter
import view.adapter.SceneAdapter
import viewModel.HomeViewModel
import java.io.Serializable


class HomeActivity : AppCompatActivity() {

    private lateinit var sceneTitle : TextView
    private lateinit var noDataTitle : TextView
    private lateinit var scenesRecyclerView: RecyclerView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var roomChipGroup: ChipGroup
    private lateinit var sceneAdapter : SceneAdapter
    private lateinit var deviceAdapter : DeviceAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var loader: KProgressHUD

    private lateinit var deviceList : List<HubAccessoryConfiguration>
    private lateinit var sceneList : List<Scene>

    private var deviceLoaded : Boolean = false
    private var sceneLoaded : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        roomChipGroup = findViewById(R.id.home_room_filter_chip_group)
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
            deviceList = it
            deviceAdapter.deviceList = deviceList
            setUpRoomFilter()
            deviceLoaded = true
            checkLoaded()
            sceneAdapter.deviceList = deviceList
            loader.dismiss()
        })
        homeViewModel.sceneList.observe(this, Observer {
            sceneList = it
            sceneAdapter.sceneList = sceneList
            sceneLoaded = true
            checkLoaded()
        })
    }

    private fun setUpProfilBtn() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_settings)
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
        val listOfAction: List<String> = listOf(getString(R.string.home_add_new_device), getString(R.string.home_add_new_scene), getString(R.string.home_add_new_room), getString(R.string.home_add_new_automatisation))
        val listOfActionToCharSequence = listOfAction.toTypedArray<CharSequence>()

        val createSceneIntent = Intent(this, CreateSceneActivity::class.java)
        val args = Bundle()
        args.putSerializable("ARRAYLIST", deviceAdapter.deviceList as Serializable)
        createSceneIntent.putExtra("BUNDLE", args)

        AlertDialog.Builder(this)
            .setItems(listOfActionToCharSequence) { dialog, which ->
                when(listOfActionToCharSequence[which]) {
                    getString(R.string.home_add_new_device) -> {
                        val supportedDeviceIntent = Intent(this, SupportedAccessoriesListActivity::class.java)
                        startActivity(supportedDeviceIntent)
                        dialog.dismiss()
                    }
                    getString(R.string.home_add_new_scene) -> {
                        startActivityForResult(createSceneIntent, 1)
                        dialog.dismiss()
                    }
                    getString(R.string.home_add_new_room) -> {
                        alertForCreateNewRoom()
                        dialog.dismiss()
                    }
                    getString(R.string.home_add_new_automatisation) -> {
                        createSceneIntent.putExtra("isAutomatisation", true)
                        startActivityForResult(createSceneIntent, 1)
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

        dialogBuilder.setPositiveButton(getString(R.string.home_automatisation_alert_create)) { _, _ ->
            if (editText.text.isNotEmpty()) {
                homeViewModel.addRoom(editText.text.toString())
            } else {
                Toast.makeText(this, getString(R.string.home_automatisation_empty_field_toast), Toast.LENGTH_SHORT).show()
            }
        }
        .setNegativeButton(getString(R.string.home_automatisation_alert_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        .show()

    }

    private fun setUpRoomFilter() {
            val listRoom = deviceList.map { device -> device.room }.distinct()
            val chipAll = Chip(roomChipGroup.context)
            var chipSelected: Chip = chipAll
            val defaultColor = chipAll.chipBackgroundColor

            chipAll.text = getString(R.string.home_room_filter_all)
            roomFilterSelected(chipAll)
            roomChipGroup.addView(chipAll)

            listRoom.forEach { room ->
                val chip = Chip(roomChipGroup.context)
                chip.text = room?.name
                roomFilterUnselected(chip, defaultColor)
                roomChipGroup.addView(chip)
            }

            roomChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip: Chip? = findViewById(checkedId)
                if(chip != null) {
                    if(chip != chipSelected) {
                        roomFilterUnselected(chipSelected, defaultColor)
                        filterRoom(room = chip.text.toString())
                        roomFilterSelected(chip)
                        chipSelected = chip
                    }

                }
            }
    }

    private fun roomFilterSelected(chip: Chip) {
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
        chip.typeface = Typeface.DEFAULT_BOLD
        chip.setChipBackgroundColorResource(R.color.app_primary_red)
        chip.isSelected = true
        chip.isCheckable = true
        chip.isClickable = true
        chip.isCheckedIconVisible = false
    }

    private fun roomFilterUnselected(chip: Chip, defaultColor: ColorStateList?) {
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
        chip.typeface = Typeface.DEFAULT_BOLD
        chip.chipBackgroundColor = defaultColor
        chip.isSelected = false
        chip.isCheckable = true
        chip.isClickable = true
        chip.isCheckedIconVisible = false
    }

    private fun filterRoom(room : String) {
        if (room == getString(R.string.home_room_filter_all)) {
            deviceAdapter.deviceList = deviceList
            sceneAdapter.sceneList = sceneList
        } else {
            val filterDeviceList = deviceList.filter { device -> device.room?.name == room }
            var filterSceneList = ArrayList<Scene>()

            deviceAdapter.deviceList = filterDeviceList

            val mapFilteredDeviceFriendlyName = filterDeviceList.map { device -> device.friendly_name }
            sceneList.forEach { scene ->
                val deviceExistInScene = scene.devices.firstOrNull { device -> mapFilteredDeviceFriendlyName.contains(device.friendly_name) }
                if(deviceExistInScene != null) filterSceneList.add(scene)
            }

            sceneAdapter.sceneList = filterSceneList
        }
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

