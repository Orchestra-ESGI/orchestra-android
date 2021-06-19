package viewModel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.DeviceClient
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.ListHubAccessoryConfigurationToDelete

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
        deviceService.sendDeviceAction(actions)
    }

    fun deleteDevices(friendlyName : ListHubAccessoryConfigurationToDelete) {
        deviceService.deleteDevices(friendlyName)
    }
}