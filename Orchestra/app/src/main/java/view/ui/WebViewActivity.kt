package view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.orchestra.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val myWebView: WebView = findViewById(R.id.webview)

        val url = intent.getStringExtra("URL")

        if(url != null) {
            myWebView.loadUrl(url)
        } else {
            myWebView.loadUrl("https://www.google.fr")
        }
    }
}