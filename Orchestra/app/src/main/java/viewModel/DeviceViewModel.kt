package viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.DeviceClient
import core.rest.model.*
import core.rest.model.hubConfiguration.Device
import core.utils.SingleLiveEvent

class DeviceViewModel : ViewModel() {
    lateinit var context : AppCompatActivity
    var deviceList : SingleLiveEvent<List<Device>> = SingleLiveEvent()
    var supportedAccessorieList : SingleLiveEvent<List<SupportedAccessories>> = SingleLiveEvent()
    var roomList : SingleLiveEvent<List<Room>> = SingleLiveEvent()
    val deviceService = DeviceClient

    fun getDevices() {
        deviceService.deviceList.observe(context, Observer {
            deviceList.value = it
        })
        deviceService.getAllDevices(context)
    }

    fun getSupportedAccessories() {
        deviceService.supportedDevices.observe(context, Observer {
            supportedAccessorieList.value = it
        })
        deviceService.getSupportedAccessories(context)
    }

    fun saveDevice(device : Device) {
        deviceService.saveDevice(device, context)
    }

    fun resetDevice() {
        deviceService.resetDevice(context)
    }

    fun sendDeviceAction(actions : ActionsToSet) {
        deviceService.sendDeviceAction(actions, context)
    }

    fun deleteDevices(friendlyName : HashMap<String, List<String>>) {
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