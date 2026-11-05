
package com.nuvio.app.core.sync

import com.nuvio.app.core.storage.DesktopStorage

internal actual object SyncClientIdentityStorage {
    private val store = DesktopStorage.store("nuvio_sync_client_identity")

    actual fun loadClientId(): String? =
        store.getString("client_instance_id")

    actual fun saveClientId(clientId: String) {
        store.putString("client_instance_id", clientId)
    }

    actual fun loadLicenseKey(): String? =
        store.getString("drame_license_key")

    actual fun saveLicenseKey(licenseKey: String) {
        store.putString("drame_license_key", licenseKey)
    }

    actual fun clearLicenseKey() {
        store.remove("drame_license_key")
    }
}
