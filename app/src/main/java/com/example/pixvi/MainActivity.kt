package com.example.pixvi

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.webkit.WebView.HitTestResult
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_main)
        web.settings.javaScriptEnabled = true
        web.webViewClient = WebViewClient()
        web.settings.domStorageEnabled = true
        registerForContextMenu(web);
        web.loadUrl("https://lab.getloli.com/pixiv-viewer")

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==event?.keyCode){
            if (web.canGoBack()){
                web.goBack()
                return true
            }
        }
        return false
    }

    override fun onCreateContextMenu(
        contextMenu: ContextMenu,
        view: View?,
        contextMenuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo)
        val webViewHitTestResult: HitTestResult = web.getHitTestResult()
        if (webViewHitTestResult.type == HitTestResult.IMAGE_TYPE ||
            webViewHitTestResult.type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE
        ) {
            contextMenu.setHeaderTitle("啊啦你就这么想下载我吗？")
            contextMenu.add(0, 1, 0, "是的")
                .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        val DownloadImageURL = webViewHitTestResult.extra
                        if (URLUtil.isValidUrl(DownloadImageURL)) {
                            val request = DownloadManager.Request(Uri.parse(DownloadImageURL))
                            request.allowScanningByMediaScanner()
                            //设置图片的保存路径
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                .mkdir();
                            request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                UUID.randomUUID().toString() + ".png"
                            )
                            val downloadManager =
                                getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            downloadManager.enqueue(request)
                            Toast.makeText(this@MainActivity, "啊啦啊啦真是可爱呢", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@MainActivity, "啊啦就这点觉悟吗", Toast.LENGTH_LONG).show()
                        }
                        return false
                    }
                })
        }
    }


}