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
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Approval
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorViewModel


@Composable
fun AdminRegistration(modifier: Modifier = Modifier,viewmodel:VisitorViewModel = viewModel(),navController: NavController) {
    SimpleTopBar("Admin Login")
    var contactInfo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val admininfo by viewmodel.admin.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Admin Registration", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

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
                if (contactInfo.isBlank() || password.isBlank()) {
                    errorMessage = "All fields are required!"
                } else {
                    errorMessage = ""
                    viewmodel.addAdmin(Approval(adminName = name, password = password, contactInfo = contactInfo));
                    //if(admininfo == null){Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()}
                }
            },
        ) {
            Text(text = "Register")
        }
        LaunchedEffect(admininfo) { // 🔹 React when isValid changes
            if(admininfo != null){
                navController.navigate("adminHomeScreen")
                Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
