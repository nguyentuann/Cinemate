package vn.tutorial.cinemate

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import vn.tutorial.cinemate.data.local.LocalStorage
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var localStorage: LocalStorage

    override fun onCreate() {
        super.onCreate()
        getFcmTokenIfNeeded()
    }

    private fun getFcmTokenIfNeeded() {

        val firebaseToken = localStorage.getFirebaseToken()

        if (firebaseToken == null) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    localStorage.saveFirebaseToken(token)
                }
            }
        }
    }
}

