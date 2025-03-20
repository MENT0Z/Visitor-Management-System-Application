package com.mruraza.visitotmanagementsystem.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.R
import kotlinx.coroutines.delay

@Composable
fun LandingPage(modifier: Modifier = Modifier, navController: NavController) {
    // Delay before navigating to askAdminOrUser
    LaunchedEffect(Unit) {
        delay(2000) // Show this screen for 2 seconds
        navController.navigate("askAdminOrUser") {
            popUpTo("LandingPage") { inclusive = true } // Remove LandingPage from backstack
        }
    }

    // UI of the Landing Page
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White), // Background color
        contentAlignment = Alignment.Center // Centers content in the Box
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_landing_page),
            contentDescription = "Visitor Photo",
            modifier = Modifier.size(250.dp) // Adjust size as needed
        )
    }
}
