package id.dompetku.pro

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class BankNotificationListener : NotificationListenerService() {

    private val supportedPackages = setOf(
        "com.bca",
        "id.bmri.livin",
        "id.co.bri.brimo",
        "com.gojek.app",
        "ovo.id",
        "id.dana"
    )

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val pkg = sbn?.packageName ?: return
        if (!supportedPackages.contains(pkg)) return

        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        val combined = "$title: $text"

        if (combined.contains("Rp", ignoreCase = true) || combined.contains("QRIS", ignoreCase = true)) {
            val intent = Intent("id.dompetku.pro.BANK_NOTIF")
            intent.putExtra("notif_text", combined)
            sendBroadcast(intent)
        }
    }
}
