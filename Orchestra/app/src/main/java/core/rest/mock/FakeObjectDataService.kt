package core.rest.mock

import android.app.Notification
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.example.orchestra.BuildConfig
import core.rest.model.ActionScene
import core.rest.model.Device
import core.rest.model.Scene
import java.util.*
import kotlin.collections.ArrayList

object FakeObjectDataService {
    private var deviceMock1 = Device("1", "Café", "Salon", "#719668","Nescafé", "AXPIY63", "Machine", true, true, true, "1.0")
    private var deviceMock2 = Device("2", "Détécteur", "Cuisine", "#BC5164","Xiaomi", "REPCV20", "Capteur", true, true, true, "1.0")
    private var deviceMock3 = Device("3", "Capteur", "Chambre", "#B6AB47","Xiaomi", "ABFRE03", "Capteur", true, true, true, "1.0")
    private var deviceMock4 = Device("4", "Lumière", "Chambre", "#7BD9C7","Xiaomi", "NVRUD29", "Lampe", true, true, true, "1.0")
    var arrDeviceMock : ArrayList<Device> = arrayListOf(deviceMock1, deviceMock2, deviceMock3, deviceMock4)

    private var actionMock1 = ActionScene("Faire du café")
    private var actionMock2 = ActionScene("Allumer la lumière")
    private var actionMock3 = ActionScene("Eteindre la lumière")
    private var actionMock4 = ActionScene("Augmenter la luminosité")
    private var actionMock5 = ActionScene("Baisser la luminosité")
    var arrActionScene1: ArrayList<ActionScene> = arrayListOf(actionMock1, actionMock2, actionMock5)
    var arrActionScene2: ArrayList<ActionScene> = arrayListOf(actionMock2, actionMock4)
    var arrActionScene3: ArrayList<ActionScene> = arrayListOf(actionMock2, actionMock5)
    var arrActionScene4: ArrayList<ActionScene> = arrayListOf(actionMock3)


    private var sceneMock1 = Scene("1", "Lever", "Lever du jour", "#FFC0CB", "1", arrActionScene1)
    private var sceneMock2 = Scene("2", "Manger", "Manger du jour", "#B6DAEA", "1", arrActionScene2)
    private var sceneMock3 = Scene("3", "Regarder", "Regarder du jour", "#F1838D", "1", arrActionScene3)
    private var sceneMock4 = Scene("4", "Coucher", "Coucher du jour", "#CFDA9B", "1", arrActionScene4)
    var arrSceneMock : ArrayList<Scene> = arrayListOf(sceneMock1, sceneMock2, sceneMock3, sceneMock4)

    fun getDevices() : ArrayList<Device> {
        return arrDeviceMock
    }

    fun addDevice(device : Device) {
        arrDeviceMock.add(device)
    }

    fun getScenes() : ArrayList<Scene> {
        return arrSceneMock
    }

    fun getActions() : ArrayList<ActionScene> {
        return arrayListOf(actionMock1, actionMock2, actionMock3, actionMock4, actionMock5)
    }

    fun addScene(scene : Scene) {
        arrSceneMock.add(scene)
    }
}