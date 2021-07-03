package viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.ListHubAccessoryConfigurationToDelete

class HomeViewModel: ViewModel() {
    lateinit var context : AppCompatActivity
    var deviceViewModel : DeviceViewModel? = null
    var sceneViewModel : SceneViewModel? = null
    var deviceList : MutableLiveData<List<HubAccessoryConfiguration>> = MutableLiveData()
    var sceneList : MutableLiveData<List<Scene>> = MutableLiveData()
    var automationList : MutableLiveData<List<Automation>> = MutableLiveData()
    var roomList : MutableLiveData<List<Room>> = MutableLiveData()
    var apiError : MutableLiveData<ApiError> = MutableLiveData()

    init {
        deviceViewModel = DeviceViewModel()
        sceneViewModel = SceneViewModel()
    }

    fun getAllDevice() {
        deviceViewModel!!.context = context
        deviceViewModel!!.deviceList.observe(context, Observer {
            deviceList.value = it
        })
        deviceViewModel!!.apiError.observe(context, Observer {
            apiError.value = it
        })
        deviceViewModel!!.getDevices()
    }

    fun getAllScene() {
        sceneViewModel!!.context = context
        sceneViewModel!!.sceneList.observe(context, Observer {
            sceneList.value = it
        })
        sceneViewModel!!.getScenes()
    }

    fun getAllAutomation() {
        sceneViewModel!!.context = context
        sceneViewModel!!.automationList.observe(context, Observer {
            automationList.value = it
        })
        sceneViewModel!!.getAllAutomation()
    }

    fun launchDevice(sceneId : String) {
        sceneViewModel!!.launchScene(sceneId)
    }

    fun launchAutomation(automationId : String) {
        sceneViewModel!!.launchAutomation(automationId)
    }

    fun deleteDevices(friendlyName : ListHubAccessoryConfigurationToDelete) {
        deviceViewModel!!.context = context
        deviceViewModel!!.deleteDevices(friendlyName)
    }

    fun deleteScenes(scenes : ListSceneToDelete) {
        sceneViewModel!!.context = context
        sceneViewModel!!.deleteScenes(scenes)
    }

    fun deleteAutomations(idList : HashMap<String, List<String>>) {
        sceneViewModel!!.context = context
        sceneViewModel!!.deleteAutomations(idList)
    }

    fun getAllRooms() {
        deviceViewModel!!.context = context
        deviceViewModel!!.roomList.observe(context, Observer {
            roomList.value = it
        })
        deviceViewModel!!.getAllRoom()
    }

    fun addRoom(room : String) {
        deviceViewModel!!.context = context
        deviceViewModel!!.addRoom(room)
    }

    override fun onCleared() {
        super.onCleared()
    }
}