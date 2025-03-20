package com.mruraza.visitotmanagementsystem.ui.theme.Screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.R
import com.mruraza.visitotmanagementsystem.SimpleTopBar
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorRegisterViewModel


@Composable
fun UserLogin(
    modifier: Modifier = Modifier.background(colorResource(id = R.color.teal_900)),
    viewModel: VisitorRegisterViewModel = viewModel(),
    navController: NavController
) {
    SimpleTopBar("User Login")
    var contactInfo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val isValid by viewModel.isValidVisitor.observeAsState()
    val visitor by viewModel.visitorByContact.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "User Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contactInfo,
            onValueChange = { contactInfo = it },
            label = { Text("Email / Phone") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.checkValidUser(contactInfo, password)
            },
            enabled = contactInfo.isNotEmpty() && password.isNotEmpty()
        ) {
            Text("Login")
        }

        // 🔹 React when isValid changes
        LaunchedEffect(isValid) {
            if (isValid == true) {
                viewModel.fetchVisitorByContact(contactInfo) // Fetch visitor details
            } else if (isValid == false) {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 Navigate only when visitor data is available
        LaunchedEffect(visitor) {
            visitor?.let {
                navController.navigate("VisitorWaitingScreen/${it.id}")
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
            }
        }
        Button(
            onClick = {
                navController.navigate("VisitorHomeScreen")
            }
        ) {
            Text("Register As User")
        }
    }
}
