package core.rest.mock

import core.rest.model.ActionScene
import core.rest.model.Device
import core.rest.model.Scene
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

/*
    private var sceneMock1 = Scene("1", "Lever", "Lever du jour", "#FFC0CB", "1", arrActionScene1)
    private var sceneMock2 = Scene("2", "Manger", "Manger du jour", "#B6DAEA", "1", arrActionScene2)
    private var sceneMock3 = Scene("3", "Regarder", "Regarder du jour", "#F1838D", "1", arrActionScene3)
    private var sceneMock4 = Scene("4", "Coucher", "Coucher du jour", "#CFDA9B", "1", arrActionScene4)

 */
    var arrSceneMock : ArrayList<Scene> = arrayListOf()

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

    val mockSupportedDevice = """[
    {
        "brand": "Philips",
        "devices": [
            {
                "type": "lightbulb",
                "name": "Philips Hue White",
                "manufacturer": "Philips",
                "model": "9290022167",
                "image": "https://images-na.ssl-images-amazon.com/images/I/410xhbf4MRL.jpg",
                "documentation": null
            },
            {
                "type": "statelessProgrammableSwitch",
                "name": "Philips Hue smart button",
                "manufacturer": "Philips",
                "model": "8718699693985",
                "image": "https://www.zigbee2mqtt.io/images/devices/8718699693985.jpg",
                "documentation": null
            }
        ]
    },
    {
        "brand": "SONOFF",
        "devices": [
            {
                "type": "statelessProgrammableSwitch",
                "name": "SONOFF Switch",
                "manufacturer": "SONOFF",
                "model": "SNZB-01",
                "image": "https://www.zigbee2mqtt.io/images/devices/SNZB-01.jpg",
                "documentation": "https://www.zigbee2mqtt.io/devices/SNZB-01.html#pairing"
            },
            {
                "type": "sensor",
                "name": "SONOFF Motion Sensor",
                "manufacturer": "SONOFF",
                "model": "SNZB-03",
                "image": "https://www.zigbee2mqtt.io/images/devices/SNZB-03.jpg",
                "documentation": "https://www.zigbee2mqtt.io/devices/SNZB-03.html#pairing"
            }
        ]
    },
    {
        "brand": "IKEA",
        "devices": [
            {
                "type": "statelessProgrammableSwitch",
                "name": "IKEA Switch",
                "manufacturer": "IKEA",
                "model": "E1743",
                "image": "https://www.zigbee2mqtt.io/images/devices/E1743.jpg",
                "documentation": "https://www.zigbee2mqtt.io/devices/E1743.html#pairing"
            }
        ]
    }
]"""



    val mockDeviceList = """{
    "devices": [
        {
            "_id": "60c612244eba1f0518fb9138",
            "reset": true,
            "name": "Lampe 2",
            "type": "lightbulb",
            "friendly_name": "0x0017880108b21761",
            "background_color": "#57a5f6",
            "is_fav": false,
            "room_name": "Chambre",
            "is_reachable": false,
            "model": "9290022166",
            "is_on": true,
            "manufacturer": "Philips",
            "actions": {
                "state": "ON",
                "brightness": {
                    "min_val": 0,
                    "max_val": 150
                },
                "color_temp": {
                    "min_val": 150,
                    "max_val": 500
                },
                "color": {
                    "hex": ""
                }
            }
        },
        {
            "_id": "60c662c816f48c07d83f57eb",
            "type": "unknown",
            "name": "Hue smart button",
            "friendly_name": "0x001788010802aa00",
            "background_color": "#FF0000"
        },
        {
            "_id": "60c662c816f48c07d83f57ec",
            "type": "unknown",
            "name": "Hue white and color ambiance E26/E27",
            "friendly_name": "0x0017880108a7f55e",
            "background_color": "#FF0000"
        },
        {
            "_id": "60c662c816f48c07d83f57ed",
            "type": "unknown",
            "name": "Smart+ plug",
            "friendly_name": "0x7cb03eaa0a091b5e",
            "background_color": "#FF0000"
        },
        {
            "_id": "60c662c816f48c07d83f57ee",
            "type": "unknown",
            "name": "Motion sensor",
            "friendly_name": "0x00124b0022e9f86d",
            "background_color": "#FF0000"
        },
        {
            "_id": "60c662c816f48c07d83f57ef",
            "type": "unknown",
            "name": "Contact sensor",
            "friendly_name": "0x00124b0022cd2aa4",
            "background_color": "#FF0000"
        }
    ],
    "error": null
}"""

}