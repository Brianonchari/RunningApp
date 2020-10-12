package co.studycode.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import co.studycode.runningapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_terms.*

@AndroidEntryPoint
class TermsFragment: Fragment(R.layout.fragment_terms){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        terms_webview.apply {
            webViewClient = WebViewClient()
            terms_webview.loadUrl("file:///android_asset/terms_and_conditions.html")
        }
        terms_webview.settings.apply {
            javaScriptEnabled = true
        }
    }
}