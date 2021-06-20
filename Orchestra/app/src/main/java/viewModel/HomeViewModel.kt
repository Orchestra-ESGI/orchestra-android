package viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.DeviceClient
import core.rest.mock.FakeObjectDataService
import core.rest.model.Device
import core.rest.model.ListSceneToDelete
import core.rest.model.Scene
import core.rest.model.SupportedAccessories
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.ListHubAccessoryConfigurationToDelete

class HomeViewModel: ViewModel() {
    lateinit var context : AppCompatActivity
    var deviceViewModel : DeviceViewModel? = null
    var sceneViewModel : SceneViewModel? = null
    var deviceList : MutableLiveData<List<HubAccessoryConfiguration>> = MutableLiveData()
    var sceneList : MutableLiveData<List<Scene>> = MutableLiveData()

    init {
        deviceViewModel = DeviceViewModel()
        sceneViewModel = SceneViewModel()
    }

    fun getAllDevice() {
        deviceViewModel!!.context = context
        deviceViewModel!!.deviceList.observe(context, Observer {
            deviceList.value = it
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

    fun launchDevice(sceneId : String) {
        sceneViewModel!!.launchScene(sceneId)
    }

    fun deleteDevices(friendlyName : ListHubAccessoryConfigurationToDelete) {
        deviceViewModel!!.context = context
        deviceViewModel!!.deleteDevices(friendlyName)
    }

    fun deleteScenes(scenes : ListSceneToDelete) {
        sceneViewModel!!.context = context
        sceneViewModel!!.deleteScenes(scenes)
    }

    override fun onCleared() {
        super.onCleared()
    }
}