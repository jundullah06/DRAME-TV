
package com.nuvio.app.core.license

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val LicenseGreen = Color(0xFF14583E)
private val LicenseGold = Color(0xFFD7B65D)
private val LicenseCream = Color(0xFFF7F5EC)

@Composable
fun DrameLicenseScreen(
    onLicenseActivated: () -> Unit
) {
    var licenseKey by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LicenseCream)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "DRAME TV",
                color = LicenseGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "ACTIVATION DE VOTRE LICENCE",
                color = LicenseGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Entrez votre clé d'activation pour accéder à DRAME TV.",
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            BasicTextField(
                value = licenseKey,
                onValueChange = { licenseKey = it.uppercase() },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = LicenseGreen,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (licenseKey.isEmpty()) {
                            Text(
                                "DTV-XXXX-XXXX-XXXX",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = {
                    if (licenseKey.isBlank() || isLoading) return@Button

                    scope.launch {
                        isLoading = true
                        message = "Vérification de votre licence..."

                        val result = DrameLicenseService.activate(licenseKey)

                        isLoading = false
                        message = result.message

                        if (result.valid) {
                            onLicenseActivated()
                        }
                    }
                },
                enabled = licenseKey.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LicenseGreen,
                    contentColor = Color.White
                )
            ) {
                Text(
                    if (isLoading) "VÉRIFICATION..." else "ACTIVER MA LICENCE",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = if (message.contains("impossible", true) ||
                        message.contains("erreur", true) ||
                        message.contains("invalide", true)
                    ) {
                        Color.Red
                    } else {
                        LicenseGreen
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Licence mensuelle : 1 000 FCFA",
                color = LicenseGreen,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Une licence est valable pour un seul ordinateur.",
                color = Color.DarkGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

