package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import viewModel.DeviceViewModel

class SearchDeviceActivity : AppCompatActivity() {

    private lateinit var searchDeviceImageView : ImageView
    private lateinit var searchDeviceTitleTextView : TextView
    private lateinit var searchDeviceDescriptionTextView: TextView
    private lateinit var returnHomeButton : Button

    private var deviceData: HubAccessoryConfiguration? = null
    private var deviceVM: DeviceViewModel? = null
    private var isSuccessfulyAdded = false

    @RequiresApi(Build.VERSION_CODES.O)
    val colorSuccess = Color.argb((1).toFloat(), (0.177152276).toFloat(), (0.669238627).toFloat(), (0.3678025007).toFloat())
    @RequiresApi(Build.VERSION_CODES.O)
    val colorError = Color.argb((1).toFloat(), (1).toFloat(), (0.2390179634).toFloat(), (0.2027955651).toFloat())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_device)

        deviceVM = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceData = intent.getSerializableExtra("CreatedScene") as? HubAccessoryConfiguration

        bind()
        setUpViews()
        setOnClickListenerReturnHome()
    }

    private fun bind() {
        searchDeviceImageView = findViewById(R.id.search_device_iv)
        searchDeviceTitleTextView = findViewById(R.id.search_device_title_tv)
        searchDeviceDescriptionTextView = findViewById(R.id.search_device_description_tv)
        returnHomeButton = findViewById(R.id.search_device_btn)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpViews() {
        if(isSuccessfulyAdded){
            searchDeviceImageView.setImageResource(R.drawable.ic_check)
            searchDeviceTitleTextView.text = getString(R.string.search_device_success_title)
            searchDeviceTitleTextView.setTextColor(colorSuccess)
            searchDeviceDescriptionTextView.text = getString(R.string.search_device_success_description)
            returnHomeButton.text = getString(R.string.search_device_success_btn)
            returnHomeButton.setBackgroundColor(colorSuccess)
        }else{
            searchDeviceImageView.setImageResource(R.drawable.ic_cancel)
            searchDeviceTitleTextView.text = getString(R.string.search_device_error_title)
            searchDeviceTitleTextView.setTextColor(colorError)
            searchDeviceDescriptionTextView.text = getString(R.string.search_device_error_description)
            returnHomeButton.text = getString(R.string.search_device_error_btn)
            returnHomeButton.setBackgroundColor(colorError)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListenerReturnHome() {
        returnHomeButton.setOnClickListener {
            if(isSuccessfulyAdded){
                deviceVM!!.saveDevice(deviceData!!)
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            }else{
                isSuccessfulyAdded = true
                setUpViews()
            }
        }
    }

}