package viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.SceneClient
import core.rest.model.Automation
import core.rest.model.ListSceneToDelete
import core.rest.model.Scene
import core.utils.SingleLiveEvent

class SceneViewModel : ViewModel() {
    lateinit var context : AppCompatActivity
    var sceneList : SingleLiveEvent<List<Scene>> = SingleLiveEvent()
    var automationList : SingleLiveEvent<List<Automation>> = SingleLiveEvent()
    var sceneService : SceneClient = SceneClient

    fun getScenes() {
        sceneService.sceneList.observe(context, Observer {
            sceneList.value = it
        })
        sceneService.getAllScene(context)
    }

    fun getAllAutomation() {
        sceneService.automationList.observe(context, Observer {
            automationList.value = it
        })
        sceneService.getAllAutomation(context)
    }

    fun saveScene(scene : Scene) {
        sceneService.addScene(scene, context)
    }

    fun launchScene(sceneId : String) {
        sceneService.launchScene(sceneId, context)
    }

    fun launchAutomation(automationId : String) {
        sceneService.launchAutomation(automationId, context)
    }

    fun deleteScenes(scenes : ListSceneToDelete) {
        sceneService.deleteScenes(scenes, context)
    }

    fun updateScene(scene : Scene) {
        sceneService.updateScene(scene, context)
    }

    fun saveAutomation(automation: Automation) {
        sceneService.saveAutomation(automation, context)
    }

    fun updateAutomation(automation: Automation) {
        sceneService.updateAutomation(automation, context)
    }

    fun deleteAutomations(idList : HashMap<String, List<String>>) {
        sceneService.deleteAutomations(idList, context)
    }
}