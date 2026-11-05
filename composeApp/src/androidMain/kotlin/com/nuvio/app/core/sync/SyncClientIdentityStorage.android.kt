
package com.nuvio.app.core.sync

import android.content.Context
import android.content.SharedPreferences

actual object SyncClientIdentityStorage {
    private const val preferencesName = "nuvio_sync_client_identity"
    private const val clientIdKey = "client_instance_id"
    private const val licenseKey = "drame_license_key"

    private var preferences: SharedPreferences? = null

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(
            preferencesName,
            Context.MODE_PRIVATE
        )
    }

    actual fun loadClientId(): String? =
        preferences?.getString(clientIdKey, null)

    actual fun saveClientId(clientId: String) {
        preferences?.edit()?.putString(clientIdKey, clientId)?.apply()
    }

    actual fun loadLicenseKey(): String? =
        preferences?.getString(licenseKey, null)

    actual fun saveLicenseKey(licenseKey: String) {
        preferences?.edit()?.putString(this.licenseKey, licenseKey)?.apply()
    }

    actual fun clearLicenseKey() {
        preferences?.edit()?.remove(licenseKey)?.apply()
    }
}
