package viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.DeviceClient
import core.rest.model.*
import core.rest.model.hubConfiguration.Device

class DeviceViewModel : ViewModel() {
    lateinit var context : AppCompatActivity
    var deviceList : MutableLiveData<List<Device>> = MutableLiveData()
    var supportedAccessorieList : MutableLiveData<List<SupportedAccessories>> = MutableLiveData()
    var roomList : MutableLiveData<List<Room>> = MutableLiveData()
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