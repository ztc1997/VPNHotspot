package be.mygod.vpnhotspot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import be.mygod.vpnhotspot.net.Routing
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers
import java.io.IOException

class SettingsPreferenceFragment : PreferenceFragmentCompatDividers() {
    private val customTabsIntent by lazy {
        CustomTabsIntent.Builder().setToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimary)).build()
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_settings)
        findPreference("service.clean").setOnPreferenceClickListener {
            if (Routing.clean() == null) Toast.makeText(context!!, R.string.root_unavailable, Toast.LENGTH_SHORT).show()
            else LocalBroadcastManager.getInstance(context!!).sendBroadcastSync(Intent(App.ACTION_CLEAN_ROUTINGS))
            true
        }
        findPreference("misc.logcat").setOnPreferenceClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, Runtime.getRuntime().exec(arrayOf("logcat", "-d"))
                                .inputStream.bufferedReader().readText())
                startActivity(Intent.createChooser(intent, getString(R.string.abc_shareactionprovider_share_with)))
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
            true
        }
        findPreference("misc.source").setOnPreferenceClickListener {
            customTabsIntent.launchUrl(activity, Uri.parse("https://github.com/Mygod/VPNHotspot"))
            true
        }
        findPreference("misc.donate").setOnPreferenceClickListener {
            customTabsIntent.launchUrl(activity, Uri.parse("https://mygod.be/donate/"))
            true
        }
    }
}
