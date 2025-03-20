package com.mruraza.visitotmanagementsystem.ui.theme.Screens

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.SimpleTopBar
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Visitor
import com.mruraza.visitotmanagementsystem.ui.theme.Model.VisitorRequest
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorRegisterViewModel
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun VisitorHomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        topBar = { SimpleTopBar("User Registration") }
    ) { innerPadding ->
        VisitorForm(
            modifier = modifier.padding(innerPadding),
            navController = navController
        )
    }
}



@Composable
fun VisitorForm(modifier: Modifier, viewmodel: VisitorRegisterViewModel = viewModel(),navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hostEmployee by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var photoBase64 by remember { mutableStateOf<String?>(null) }

    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Camera launcher must be remembered
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val byteArrayOutputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val base64String =
                    android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
                photoBase64 = base64String
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            isError = showError && fullName.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contactInfo,
            onValueChange = { contactInfo = it },
            label = { Text("Email/(Phone No)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            isError = showError && contactInfo.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = showError && password.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = purpose,
            onValueChange = { purpose = it },
            label = { Text("Purpose of Visit") },
            isError = showError && purpose.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = hostEmployee,
            onValueChange = { hostEmployee = it },
            label = { Text("Host Employee") },
            isError = showError && hostEmployee.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            isError = showError && companyName.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                launcher.launch()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Capture Photo")
        }

        if (photoBase64 != null) {
            Text(
                text = "Photo Captured",
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
        }

        val visitor by viewmodel.visitor.observeAsState()

        LaunchedEffect(visitor?.id) {
            visitor?.id?.let { visitorId ->
                navController.navigate("VisitorWaitingScreen/$visitorId")
                Toast.makeText(context, "Form Submitted!", Toast.LENGTH_SHORT).show()
            }
        }

        Button(
            onClick = {
                showError = true
                if (fullName.isNotBlank() && contactInfo.isNotBlank() && password.isNotBlank() &&
                    purpose.isNotBlank() && hostEmployee.isNotBlank() &&
                    companyName.isNotBlank() && photoBase64 != null
                ) {
                    val visitorRequest = VisitorRequest(
                        fullName,
                        contactInfo,
                        purpose,
                        hostEmployee,
                        companyName,
                        password,
                        null,
                        null,
                        photoUrl = photoBase64!!
                    )
                    viewmodel.registerVisitor(visitorRequest)
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

    }
}
