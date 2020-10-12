package co.studycode.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import co.studycode.runningapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.view.*

@AndroidEntryPoint
class AboutFragment :Fragment(R.layout.fragment_about){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.apply {
            webViewClient =WebViewClient()
            webView.loadUrl("file:///android_asset/privacy_policy.html")
        }
        webView.settings.apply {
            javaScriptEnabled = true
        }
    }


}