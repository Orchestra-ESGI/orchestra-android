package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import core.rest.mock.FakeObjectDataService
import core.rest.model.Device

class DeviceViewModel : ViewModel() {
    var deviceList : MutableLiveData<List<Device>> = MutableLiveData()

    init {
        deviceList.value = FakeObjectDataService.getDevices()
    }

    fun getDevices() {
        deviceList.value = FakeObjectDataService.getDevices()
    }

    override fun onCleared() {
        super.onCleared()
    }
}