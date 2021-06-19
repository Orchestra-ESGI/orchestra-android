package viewModel

import android.app.Notification
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import core.rest.client.DeviceClient
import core.rest.mock.FakeObjectDataService
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.HubAccessoryType
import core.rest.services.DeviceService
import kotlin.math.acos

class DeviceViewModel : ViewModel() {
    lateinit var context : AppCompatActivity
    var deviceList : MutableLiveData<List<HubAccessoryConfiguration>> = MutableLiveData()
    var supportedAccessorieList : MutableLiveData<List<SupportedAccessories>> = MutableLiveData()
    val deviceService = DeviceClient

    init {
        // deviceList.value = FakeObjectDataService.getDevices()
        // val hub1 = HubAccessoryConfiguration("Lampe", "Cuisine", "#000000", "", "", true, true, HubAccessoryType.lightbulb, Actions(DeviceState.ON, SliderAction(0, 100, 0), ColorAction("#FF0000"), SliderAction(0, 100, 0)), friendly_name = "x3049defadea" )
        // deviceList.value = listOf<HubAccessoryConfiguration>(hub1)
    }

    fun getDevices() {
        // deviceList.value = FakeObjectDataService.getDevices()
        deviceService.deviceList.observe(context, Observer {
            deviceList.value = it
        })
        deviceService.getAllDevices()
    }

    fun getSupportedAccessories() {
        deviceService.supportedDevices.observe(context, Observer {
            supportedAccessorieList.value = it
        })
        deviceService.getSupportedAccessories()
    }

    fun saveDevice(device : HubAccessoryConfiguration) {
        deviceService.saveDevice(device)
    }

    fun resetDevice() {
        deviceService.resetDevice()
    }

    fun sendDeviceAction(actions : ActionsToSet) {
        Log.d("Test Gson", Gson().toJson(actions))
        deviceService.sendDeviceAction(actions)
    }

    fun deleteDevice(friendlyName : String) {
        deviceService.deleteDevice(friendlyName)
    }

    override fun onCleared() {
        super.onCleared()
    }
}