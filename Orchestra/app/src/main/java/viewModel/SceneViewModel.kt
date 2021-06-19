package viewModel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.SceneClient
import core.rest.mock.FakeObjectDataService
import core.rest.model.Device
import core.rest.model.ListSceneToDelete
import core.rest.model.Scene

class SceneViewModel : ViewModel() {
    lateinit var context : AppCompatActivity
    var sceneList : MutableLiveData<List<Scene>> = MutableLiveData()
    var sceneService : SceneClient = SceneClient

    init {
        // sceneList.value = FakeObjectDataService.getScenes()
    }

    fun getScenes() {
        // sceneList.value = FakeObjectDataService.getScenes()
        sceneService.sceneList.observe(context, Observer {
            sceneList.value = it
        })
        sceneService.getAllScene()
    }

    fun saveScene(scene : Scene) {
        sceneService.addScene(scene)
    }

    fun deleteScenes(scenes : ListSceneToDelete) {
        sceneService.deleteScenes(scenes)
    }

    override fun onCleared() {
        super.onCleared()
    }
}