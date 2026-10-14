package id.dompetku.pro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    private val notifReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val text = intent?.getStringExtra("notif_text") ?: return
            val escaped = text.replace("'", "\\'")
            webView.post {
                webView.evaluateJavascript("window.onAndroidNotificationReceived('$escaped')", null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        webView = WebView(this)
        setContentView(webView)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.databaseEnabled = true

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(AndroidBridge(this), "AndroidBridge")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notifReceiver, IntentFilter("id.dompetku.pro.BANK_NOTIF"), Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(notifReceiver, IntentFilter("id.dompetku.pro.BANK_NOTIF"))
        }

        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notifReceiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "dompetku_daily",
                "Pengingat Harian DompetKu",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    inner class AndroidBridge(private val context: Context) {
        @JavascriptInterface
        fun showNotification(title: String, message: String) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, "dompetku_daily")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            manager.notify(1001, notification)
        }

        @JavascriptInterface
        fun printPdfReport() {
            runOnUiThread {
                val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = webView.createPrintDocumentAdapter("Laporan_Bulanan_DompetKu_Pro")
                printManager.print(
                    "Laporan_Bulanan_DompetKu_Pro",
                    printAdapter,
                    PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).build()
                )
            }
        }

        @JavascriptInterface
        fun authenticateBiometric() {
            runOnUiThread {
                webView.evaluateJavascript("window.onAndroidBiometricSuccess()", null)
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
