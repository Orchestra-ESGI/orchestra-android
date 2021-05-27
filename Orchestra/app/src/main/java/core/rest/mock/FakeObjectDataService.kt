package core.rest.mock

import android.app.Notification
import android.graphics.Color
import com.example.orchestra.BuildConfig
import core.rest.model.ActionScene
import core.rest.model.Device
import core.rest.model.Scene

object FakeObjectDataService {
    var arrDeviceMock : ArrayList<Device> = ArrayList()
    private var deviceMock1 = Device("1", "Café", "Salon", "#719668","Nescafé", "AXPIY63", "Machine", true, true, true, "1.0")
    private var deviceMock2 = Device("2", "Détécteur", "Cuisine", "#BC5164","Xiaomi", "REPCV20", "Capteur", true, true, true, "1.0")
    private var deviceMock3 = Device("3", "Capteur", "Chambre", "#B6AB47","Xiaomi", "ABFRE03", "Capteur", true, true, true, "1.0")
    private var deviceMock4 = Device("4", "Lumière", "Chambre", "#7BD9C7","Xiaomi", "NVRUD29", "Lampe", true, true, true, "1.0")

    private var actionMock1 = ActionScene("Faire du café")
    private var actionMock2 = ActionScene("Allumer la lumière")
    private var actionMock3 = ActionScene("Eteindre la lumière")
    private var actionMock4 = ActionScene("Augmenter la luminosité")
    private var actionMock5 = ActionScene("Baisser la luminosité")
    var arrActionScene1: ArrayList<ActionScene> = arrayListOf(actionMock1, actionMock2, actionMock5)
    var arrActionScene2: ArrayList<ActionScene> = arrayListOf(actionMock2, actionMock4)
    var arrActionScene3: ArrayList<ActionScene> = arrayListOf(actionMock2, actionMock5)
    var arrActionScene4: ArrayList<ActionScene> = arrayListOf(actionMock3)

    var arrSceneMock : ArrayList<Scene> = ArrayList()
    private var sceneMock1 = Scene("1", "Lever", "Lever du jour", "#FFC0CB", "1", arrActionScene1)
    private var sceneMock2 = Scene("2", "Manger", "Manger du jour", "#B6DAEA", "1", arrActionScene2)
    private var sceneMock3 = Scene("3", "Regarder", "Regarder du jour", "#F1838D", "1", arrActionScene3)
    private var sceneMock4 = Scene("4", "Coucher", "Coucher du jour", "#CFDA9B", "1", arrActionScene4)


    fun getDevices() : ArrayList<Device> {
        arrDeviceMock.add(deviceMock1)
        arrDeviceMock.add(deviceMock2)
        arrDeviceMock.add(deviceMock3)
        arrDeviceMock.add(deviceMock4)

        return arrDeviceMock
    }

    fun getScenes() : ArrayList<Scene> {
        arrSceneMock.add(sceneMock1)
        arrSceneMock.add(sceneMock2)
        arrSceneMock.add(sceneMock3)
        arrSceneMock.add(sceneMock4)

        return arrSceneMock
    }

    fun getActions() : ArrayList<ActionScene> {
        return arrayListOf(actionMock1, actionMock2, actionMock3, actionMock4, actionMock5)
    }

    fun addScene(scene : Scene) {
        arrSceneMock.add(scene)
    }
}