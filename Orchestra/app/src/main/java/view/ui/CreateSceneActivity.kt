package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.mock.FakeObjectDataService
import core.rest.model.ActionScene
import core.rest.model.Scene
import utils.OnItemClicked
import view.adapter.DetailSceneActionsAdapter
import view.adapter.SceneAdapter
import view.adapter.ShuffleColorAdapter
import kotlin.random.Random

class CreateSceneActivity : AppCompatActivity(), OnItemClicked {

    private lateinit var titleTextView: TextView
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var chooseColorTitleTextView: TextView
    private lateinit var shuffleColorButton: ImageView
    private lateinit var sceneColorsRecyclerView: RecyclerView
    private lateinit var descriptionTitleTextView: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var addActionTextView: TextView
    private lateinit var listActionRecyclerView: RecyclerView

    private lateinit var sceneColorsAdapter : ShuffleColorAdapter
    private lateinit var listActionAdapter : DetailSceneActionsAdapter

    private lateinit var sceneColorsHashMap : MutableMap<Int, Boolean>
    private lateinit var sceneColors : ArrayList<Int>
    private var actionList : ArrayList<ActionScene> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_scene)

        bind()
        generateBackGroundColor()
        setUpRv()
        setUpShuffleColor()
        setUpAddActions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_scene_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_scene_add_btn -> {
            val intent = Intent()
            intent.putExtra("CreatedScene", retrieveData())
            setResult(RESULT_OK, intent)
            finish()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun generateBackGroundColor() {
        this.sceneColorsHashMap = mutableMapOf()
        this.sceneColors = ArrayList()
        val size = 5
        var randomColor : Int = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        this.sceneColorsHashMap[randomColor] = true
        this.sceneColors.add(randomColor)
        for (i in 1..size) {
            randomColor = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            this.sceneColorsHashMap[randomColor] = false
            this.sceneColors.add(randomColor)
        }
    }

    private fun bind() {
        titleTextView = findViewById(R.id.create_scene_new_scene_tv)
        nameTitleTextView = findViewById(R.id.create_scene_name_tv)
        nameEditText = findViewById(R.id.create_scene_name_et)
        chooseColorTitleTextView = findViewById(R.id.create_scene_choose_color_tv)
        shuffleColorButton = findViewById(R.id.create_scene_shuffle_color_iv)
        sceneColorsRecyclerView = findViewById(R.id.create_scene_colors_rv)
        descriptionTitleTextView = findViewById(R.id.create_scene_description_tv)
        descriptionEditText = findViewById(R.id.create_scene_description_et)
        addActionTextView = findViewById(R.id.create_scene_add_action_tv)
        listActionRecyclerView = findViewById(R.id.create_scene_list_action_rv)
    }

    private fun setUpRv() {

        sceneColorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sceneColorsAdapter = ShuffleColorAdapter(this)
        sceneColorsAdapter.colorListMap = sceneColorsHashMap
        sceneColorsAdapter.colorList = sceneColors
        sceneColorsRecyclerView.adapter = sceneColorsAdapter

        listActionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listActionAdapter = DetailSceneActionsAdapter()
        listActionAdapter.detailSceneActions = actionList
        listActionRecyclerView.adapter = listActionAdapter
    }

    private fun setUpShuffleColor() {
        shuffleColorButton.setOnClickListener {
            this.sceneColorsHashMap.clear()
            this.sceneColors.toMutableList().clear()
            generateBackGroundColor()
            redrawShuffleColors()
        }
    }

    private fun setUpAddActions() {
        val listOfAction = FakeObjectDataService.getActions().map {
            it.title
        }
        val listOfActionToCharSequence = listOfAction.toTypedArray<CharSequence>()

        addActionTextView.setOnClickListener {
            val alertDialog: AlertDialog = this@CreateSceneActivity.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    this.setTitle(R.string.create_scene_list_action_description)
                            .setItems(listOfActionToCharSequence) { _, which ->
                                val selected = listOfActionToCharSequence[which]
                                actionList.add(ActionScene(selected.toString()))
                                listActionAdapter.detailSceneActions = actionList
                            }
                }
                builder.create()
            }
            alertDialog.show()
        }
    }

    private fun retrieveData() : Scene {
        val id = Random.nextInt()
        val name = nameEditText.text
        val description = descriptionEditText.text
        val backgroundColor = sceneColorsHashMap.filterValues { it }.keys.first()
        // TO CHANGE
        val idUser = Random.nextInt()

        return Scene(id.toString(), name.toString(), description.toString(), backgroundColor.toString(), idUser.toString(), actionList)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun colorClicked(color: Int, position: Int) {
        Log.d("ClickedColor", "$color - $position")
        var oldSelectedColor = sceneColorsHashMap.filterValues {
            it
        }
        if (oldSelectedColor.size == 1) {
            Log.d("ClickedColor", oldSelectedColor.keys.first().toString())
            sceneColorsHashMap.replace(oldSelectedColor.keys.first(), false)
            sceneColorsHashMap.replace(color, true)
            redrawShuffleColors()
        }
    }

    private fun redrawShuffleColors() {
        sceneColorsRecyclerView.adapter = null;
        sceneColorsRecyclerView.layoutManager = null;
        sceneColorsAdapter.colorListMap = sceneColorsHashMap
        sceneColorsAdapter.colorList = sceneColors
        sceneColorsRecyclerView.adapter = sceneColorsAdapter
        sceneColorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }
}