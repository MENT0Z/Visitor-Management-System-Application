package com.mruraza.visitotmanagementsystem.ui.theme.Screens

import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.SimpleTopBar
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorViewModel


@Composable
fun AdminLogin(
    modifier: Modifier = Modifier,
    viewmodel: VisitorViewModel = viewModel(),
    navController: NavController
) {
    SimpleTopBar("Admin Login")
    var contactInfo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val isValid by viewmodel.isValidVisitor.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Admin Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

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
                viewmodel.checkValidAdmin(contactInfo, password)
                isValid?.let { valid ->
                    if (!valid) {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = contactInfo.isNotEmpty() && password.isNotEmpty()
        ) {
            Text("Login")
        }
        LaunchedEffect(isValid) { // 🔹 React when isValid changes
            isValid?.let { valid ->
                if (valid) {
                    navController.navigate("adminHomeScreen")
                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("AdminRegistration")
            }
        ) {
            Text("Register As Admin")
        }

    }
}
