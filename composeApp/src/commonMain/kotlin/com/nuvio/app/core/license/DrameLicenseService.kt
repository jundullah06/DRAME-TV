
package com.nuvio.app.core.license

import com.nuvio.app.core.network.createApiHttpClient
import com.nuvio.app.core.sync.SyncClientIdentity
import com.nuvio.app.core.sync.SyncClientIdentityStorage
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull

data class DrameLicenseResult(
    val valid: Boolean,
    val message: String,
    val expiresAt: String? = null
)

object DrameLicenseService {

    suspend fun validateStoredLicense(): DrameLicenseResult {
        val storedKey = SyncClientIdentityStorage.loadLicenseKey()
            ?.takeIf { it.isNotBlank() }
            ?: return DrameLicenseResult(
                false,
                "Aucune licence enregistrée."
            )

        return activate(storedKey)
    }

    suspend fun activate(licenseKey: String): DrameLicenseResult {
        val normalizedKey = licenseKey.trim().uppercase()

        if (normalizedKey.isBlank()) {
            return DrameLicenseResult(false, "Veuillez saisir votre clé.")
        }

        val client: HttpClient = createApiHttpClient()

        return try {
            val response: HttpResponse = client.post(DrameLicenseConfig.API_URL) {
                contentType(ContentType.Application.Json)
                header("apikey", DrameLicenseConfig.ANON_KEY)

                setBody(
                    buildJsonObject {
                        put("license_key", normalizedKey)
                        put("device_id", SyncClientIdentity.currentClientId())
                    }.toString()
                )
            }

            val json = try {
                Json.parseToJsonElement(response.bodyAsText()).jsonObject
            } catch (_: Exception) {
                null
            }

            val valid = json
                ?.get("valid")
                ?.jsonPrimitive
                ?.booleanOrNull ?: false

            val message = json
                ?.get("message")
                ?.jsonPrimitive
                ?.contentOrNull
                ?: json
                    ?.get("error")
                    ?.jsonPrimitive
                    ?.contentOrNull
                ?: "Erreur de vérification."

            val expiresAt = json
                ?.get("expires_at")
                ?.jsonPrimitive
                ?.contentOrNull

            val isValid = response.status.isSuccess() && valid

            if (isValid) {
                SyncClientIdentityStorage.saveLicenseKey(normalizedKey)
            }

            DrameLicenseResult(
                valid = isValid,
                message = message,
                expiresAt = expiresAt
            )

        } catch (_: Exception) {
            DrameLicenseResult(
                false,
                "Impossible de contacter le serveur. Vérifiez votre connexion."
            )
        } finally {
            client.close()
        }
    }
}
