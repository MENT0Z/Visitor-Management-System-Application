package com.mruraza.visitotmanagementsystem.ui.theme.Screens

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.TopBar
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorRegisterViewModel

@Composable
fun VisitorWaitingScreen(
    modifier: Modifier = Modifier,
    viewModel: VisitorRegisterViewModel = viewModel(),
    visitorId: Int,
    navController: NavController
) {

    val activity = LocalContext.current as? Activity

    BackHandler {
        activity?.finish() // Closes the app cleanly
    }

    Scaffold(
        topBar = { TopBar(title = "Visitor", isAdmin = false, visitorId = visitorId) },
    ) { innerPadding ->
        modifier.padding(innerPadding)
    }
    // Fetch visitor details
    LaunchedEffect(Unit) {
        viewModel.getVisitorById(visitorId)
        viewModel.getVisitorStatus(visitorId)
    }

    val visitorData by viewModel.singlevisitor.observeAsState()
    val visitorStatus by viewModel.visitorStatus.observeAsState(visitorData?.approvalStatus.toString())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

            // Display visitor photo
        visitorData?.photoUrl.let { base64 ->
            if (base64 != null) {
                decodeBase64ToBitmap(base64)?.let { imageBitmap ->
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Visitor Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }
            }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visitor details
            Text("Full Name: ${visitorData?.fullName}", fontWeight = FontWeight.Bold)
            Text("Contact Info: ${visitorData?.contactInfo}")
            Text("Purpose: ${visitorData?.purposeOfVisit}")
            Text("Host Employee: ${visitorData?.hostEmployee}")
            Text("Company Name: ${visitorData?.companyName}")

            Spacer(modifier = Modifier.height(16.dp))

            // Status Field
            Text(
                text = "Status: $visitorStatus",
                fontWeight = FontWeight.Bold,
                color = when (visitorStatus) {
                    "ACCEPTED" -> Color.Green
                    "REJECTED" -> Color.Red
                    else -> Color.Black
                }
            )

            // Display QR code if available
            if (visitorData?.has_qr == true) {
                visitorData?.qr?.let { base64 ->
                    decodeBase64ToBitmap(base64)?.let { imageBitmap ->
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Visitor QR",
                            modifier = Modifier
                                .size(120.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Refresh Button
            Button(
                onClick = { viewModel.getVisitorById(visitorId)
                    viewModel.getVisitorStatus(visitorId) }
            ) {
                Text("Refresh")
            }
        }
    }


// Function to decode Base64 to Bitmap
fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}
