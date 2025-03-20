package com.mruraza.visitotmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.AdminLogin
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.AdminRegistration
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.LandingPage
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.NotificationPopup
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.UserLogin
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.VisitorHomeScreen
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.VisitorWaitingScreen
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.adminHomeScreen
import com.mruraza.visitotmanagementsystem.ui.theme.Screens.askAdmiOrUser
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorViewModel
import com.mruraza.visitotmanagementsystem.ui.theme.VisitotManagementSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //top_bar("Visitor")
            val navController = rememberNavController()
            NavHost(navController= navController, startDestination = "LandingPage", builder = {
                composable("LandingPage"){ LandingPage(Modifier,navController)}
                composable("UserLogin"){ UserLogin(Modifier, navController = navController)}
                composable("AdminLogin"){ AdminLogin(Modifier,navController = navController)}
                composable("AdminRegistration"){ AdminRegistration(Modifier,navController = navController)}
                composable("VisitorHomeScreen"){ VisitorHomeScreen(Modifier,navController = navController)}
                composable("VisitorWaitingScreen"+"/{visitorId}"){
                    val visitorId = it.arguments?.getString("visitorId")?.toIntOrNull()
                    if (visitorId != null) {
                        VisitorWaitingScreen(Modifier, visitorId = visitorId ,navController = navController)
                    }
                }
                composable("adminHomeScreen"){ adminHomeScreen(Modifier,navController = navController)}
                composable("askAdminOrUser"){ askAdmiOrUser(Modifier,navController)}
            })
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    title: String,
    isAdmin: Boolean,
    visitorId: Int? = null,
    viewModel: VisitorViewModel = viewModel()
) {
    var showNotifications by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Observe notifications based on user type
    val totNotification by viewModel.notifications.observeAsState(emptyList())
    val totUserNotifications by viewModel.userNotifications.observeAsState(emptyList())

    val notificationsList = remember(totNotification, totUserNotifications) {
        if (isAdmin) {
            totNotification.map { notification ->
                notification.message to notification.notificationTime
            }
        } else {
            totUserNotifications.map { notification ->
                notification.message to notification.notificationTime
            }
        }
    }

    // Fetch notifications once when component is composed
    LaunchedEffect(Unit) {
        if (isAdmin) {
            viewModel.fetchNotificationsForAdmin()
        } else {
            visitorId?.let { viewModel.fetchNotificationsForUser(it) }
        }
    }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.teal_700),
            titleContentColor = colorResource(id = R.color.black),
        ),
        title = {
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        actions = {
            IconButton(onClick = { showNotifications = !showNotifications }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification"
                )
            }
            DropdownMenu(
                expanded = showNotifications,
                onDismissRequest = { showNotifications = false },
                modifier = Modifier.width(200.dp).height(300.dp)
            ) {
                NotificationPopup(
                    notifications = notificationsList,
                    onDismiss = { showNotifications = false }
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SimpleTopBar(title: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.teal_700),
            titleContentColor = colorResource(id = R.color.black),
        ),
        title = {
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        scrollBehavior = scrollBehavior
    )
}


//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//fun top_bar(name: String, viewModel: VisitorViewModel = viewModel()) {
//    var showNotifications by remember { mutableStateOf(false) }
//    val isAdmin by remember { mutableStateOf(false) }
//    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
//
//    // Observe notifications outside of click listener
//    val tot_notification by viewModel.notifications.observeAsState(emptyList())
//    var notificationsList = mutableListOf<Pair<String, String>>()
//    if(!isAdmin){
//        viewModel.fetchNotificationsForUser(7)
//    }
//    val tot_user_notifications by viewModel.userNotifications.observeAsState(emptyList())
//    VisitotManagementSystemTheme {
//        Scaffold(
//            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//            topBar = {
//                CenterAlignedTopAppBar(
//                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                        containerColor = colorResource(id = R.color.teal_700),
//                        titleContentColor = colorResource(id = R.color.black),
//                    ),
//                    title = {
//                        Text(
//                            "Visitor",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    },
//                    actions = {
//                        IconButton(onClick = {
//                            showNotifications = !showNotifications
//                            if (isAdmin) {
//                                viewModel.fetchNotificationsForAdmin()
//                                // Convert notifications into a recomposable list
//                                notificationsList = tot_notification.map { notification ->
//                                    Pair(
//                                        notification.message.toString().trim(),
//                                        notification.notificationTime.toString().trim()
//                                    )
//                                }.toMutableList()
//                            }else{
//                                viewModel.fetchNotificationsForUser(7)
//                                notificationsList = tot_user_notifications.map { notification ->
//                                    Pair(
//                                        notification.message.toString().trim(),
//                                        notification.notificationTime.toString().trim()
//                                    )
//                                }.toMutableList()
//                            }
//                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Notifications,
//                                contentDescription = "Notification"
//                            )
//                        }
//                        if (showNotifications) {
//                            DropdownMenu(
//                                expanded = showNotifications,
//                                onDismissRequest = { showNotifications = false },
//                                modifier = Modifier
//                                    .width(200.dp)
//                                    .height(300.dp)
//                            ) {
//                                NotificationPopup(
//                                    notifications = notificationsList,
//                                    onDismiss = { showNotifications = false }
//                                )
//                            }
//                        }
//                    },
//                    scrollBehavior = scrollBehavior,
//                )
//            },
//        ) { innerPadding ->
//            //adminHomeScreen(Modifier.padding(innerPadding))
//            //VisitorHomeScreen(Modifier.padding(innerPadding))
//            //VisitorWaitingScreen(Modifier.padding(innerPadding), visitorId = 11)
//            //UserLogin(Modifier.padding(innerPadding))
//            //AdminRegistration(Modifier.padding(innerPadding))
//            //AdminLogin(Modifier.padding(innerPadding))
//            //LandingPage(Modifier.padding(innerPadding))
//            //askAdmiOrUser(Modifier.padding(innerPadding))
//        }
//    }
//}



