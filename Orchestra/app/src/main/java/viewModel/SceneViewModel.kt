package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import core.rest.mock.FakeObjectDataService
import core.rest.model.Device
import core.rest.model.Scene

class SceneViewModel : ViewModel() {
    var sceneList : MutableLiveData<List<Scene>> = MutableLiveData()

    init {
        sceneList.value = FakeObjectDataService.getScenes()
    }

    fun getScenes() {
        sceneList.value = FakeObjectDataService.getScenes()
    }

    override fun onCleared() {
        super.onCleared()
    }
}