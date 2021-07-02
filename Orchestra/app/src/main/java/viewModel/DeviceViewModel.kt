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
    var roomList : MutableLiveData<List<Room>> = MutableLiveData()
    var apiError : MutableLiveData<ApiError> = MutableLiveData()
    val deviceService = DeviceClient

    fun getDevices() {
        deviceService.deviceList.observe(context, Observer {
            deviceList.value = it
        })
        deviceService.apiError.observe(context, Observer {
            apiError.value = it
        })
        deviceService.getAllDevices(context)
    }

    fun getSupportedAccessories() {
        deviceService.supportedDevices.observe(context, Observer {
            supportedAccessorieList.value = it
        })
        deviceService.getSupportedAccessories(context)
    }

    fun saveDevice(device : HubAccessoryConfiguration) {
        deviceService.saveDevice(device, context)
    }

    fun resetDevice() {
        deviceService.resetDevice(context)
    }

    fun sendDeviceAction(actions : ActionsToSet) {
        deviceService.sendDeviceAction(actions, context)
    }

    fun deleteDevices(friendlyName : ListHubAccessoryConfigurationToDelete) {
        deviceService.deleteDevices(friendlyName, context)
    }

    fun getAllRoom() {
        deviceService.roomList.observe(context, Observer {
            roomList.value = it
        })
        deviceService.getAllRoom(context)
    }

    fun addRoom(room : String) {
        deviceService.addRoom(context, room)
    }
}