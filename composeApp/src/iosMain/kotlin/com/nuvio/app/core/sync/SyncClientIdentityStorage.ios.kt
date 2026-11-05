
package com.nuvio.app.core.sync

import platform.Foundation.NSUserDefaults

actual object SyncClientIdentityStorage {
    private const val clientIdKey = "client_instance_id"
    private const val licenseKey = "drame_license_key"

    actual fun loadClientId(): String? =
        NSUserDefaults.standardUserDefaults.stringForKey(clientIdKey)

    actual fun saveClientId(clientId: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            clientId,
            forKey = clientIdKey
        )
    }

    actual fun loadLicenseKey(): String? =
        NSUserDefaults.standardUserDefaults.stringForKey(licenseKey)

   actual fun saveLicenseKey(licenseKey: String) {
    NSUserDefaults.standardUserDefaults.setObject(
        licenseKey,
        forKey = this.licenseKey
    )
}

    actual fun clearLicenseKey() {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(licenseKey)
    }
}
