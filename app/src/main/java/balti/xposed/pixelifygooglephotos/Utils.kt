package balti.xposed.pixelifygooglephotos

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log
import android.widget.Toast
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat


/**
 * Utilities class for various functions.
 */
class Utils {

    /**
     * Used to force close an app.
     *
     * Uses root to stop an application.
     *
     * Tried my level best to use xposed API to force stop the application,
     * but it kept throwing error that XposedHelpers not found. No idea why.
     */
    fun forceStopPackage(packageName: String, context: Context){
        try {
            Toast.makeText(context, R.string.killing_please_wait, Toast.LENGTH_SHORT).show()
            Runtime.getRuntime().exec("su").apply {
                BufferedWriter(OutputStreamWriter(this.outputStream)).run {
                    this.write("am force-stop $packageName\n")
                    this.write("exit\n")
                    this.flush()
                }
            }
        } catch (e: Exception){
            Toast.makeText(context, R.string.failed_to_stop_package, Toast.LENGTH_SHORT).show()
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            }
            context.startActivity(intent)
        }

        /*
        * This could should have theoretically worked:
        *
        * val activityManager: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        * XposedHelpers.callMethod(activityManager, "forceStopPackage", packageName)
        */
    }

    /**
     * Launch an app.
     */
    fun openApplication(packageName: String, context: Context){
        try {
            val pm = context.packageManager
            val launchIntent = pm.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }
        catch (e: Exception){
            Toast.makeText(context, R.string.failed_to_launch_package, Toast.LENGTH_SHORT).show()
        }
    }

}