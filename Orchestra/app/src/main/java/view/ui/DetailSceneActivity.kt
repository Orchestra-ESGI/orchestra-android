package view.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Scene
import view.adapter.DetailSceneActionsAdapter

class DetailSceneActivity : AppCompatActivity() {

    private lateinit var detailSceneName : TextView
    private lateinit var detailSceneDescription : TextView
    private lateinit var deviceSceneRecyclerView: RecyclerView
    private lateinit var detailSceneAdapter : DetailSceneActionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_scene)

        detailSceneName = findViewById(R.id.detail_scene_title_tv)
        detailSceneDescription = findViewById(R.id.detail_scene_description_tv)
        deviceSceneRecyclerView = findViewById(R.id.detail_scene_actions_rv)

        detailSceneAdapter = DetailSceneActionsAdapter()

        deviceSceneRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
                deviceSceneRecyclerView.context,
                LinearLayoutManager.VERTICAL
        )
        deviceSceneRecyclerView.addItemDecoration(dividerItemDecoration)

        val sceneDetail = intent.getSerializableExtra("DetailScene") as? Scene

        detailSceneName.text = sceneDetail!!.title
        detailSceneName.setTextColor(Color.parseColor(sceneDetail.backgroundColor))
        detailSceneDescription.text = sceneDetail.sceneDescription

        detailSceneAdapter.detailSceneActions = sceneDetail.actions

        deviceSceneRecyclerView.adapter = detailSceneAdapter
    }
}