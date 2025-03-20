package com.mruraza.visitotmanagementsystem.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.R
import com.mruraza.visitotmanagementsystem.SimpleTopBar

@Composable
fun askAdmiOrUser(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.teal_500))) {
        // TopBar at the top
        SimpleTopBar("Let's Get Started")

        // Box containing the buttons
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.teal_900))
                .padding(horizontal = 32.dp, vertical = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("AdminLogin")
                    },
                ) {
                    Text(text = "Admin")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("UserLogin")
                    },
                ) {
                    Text(text = "User")
                }
            }
        }
    }
}
