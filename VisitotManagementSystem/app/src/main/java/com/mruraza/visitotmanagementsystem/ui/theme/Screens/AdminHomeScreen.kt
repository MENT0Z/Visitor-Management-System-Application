package com.mruraza.visitotmanagementsystem.ui.theme.Screens

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mruraza.visitotmanagementsystem.R
import com.mruraza.visitotmanagementsystem.TopBar
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Notification
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Visitor
import com.mruraza.visitotmanagementsystem.ui.theme.ViewModels.VisitorViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun adminHomeScreen(modifier: Modifier, viewModel: VisitorViewModel = viewModel(),navController: NavController) {

    val activity = LocalContext.current as? Activity

    BackHandler {
        activity?.finish() // Closes the app cleanly
    }

    val visitors by viewModel.visitors.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchVisitors()
        viewModel.fetchNotificationsForAdmin()
    }



    Scaffold(
        topBar = { TopBar(title = "Admin", isAdmin = true, viewModel = viewModel) },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            items(visitors) { visitor ->
                showVisitorsList(Modifier, visitor, viewModel)
            }
        }
    }


}

@Composable
fun showVisitorsList(
    modifier: Modifier,
    visitors: Visitor,
    viewModel: VisitorViewModel
) {

    var showPopup by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showTimeSelectionDialog by remember { mutableStateOf(false) }
    var whoIsCalling by remember { mutableStateOf("") }

    var status by remember { mutableStateOf(visitors.approvalStatus) }
    var statusColor by remember { mutableStateOf(Color.Gray) }
    var preApproved by remember { mutableStateOf(false) }

    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (preApproved) 140.dp else 120.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp) // Explicit vertical padding
            .clickable { showPopup = true }
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
        ,
        contentAlignment = Alignment.Center
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            visitors.photoUrl.let { base64 ->
                if (base64 != null) {
                    decodeBase64ToBitmap(base64)?.let { imageBitmap ->
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Visitor Photo",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                    }
                }
            }



//            Image(
//                painter = painterResource(id = R.drawable.image1),
//                contentDescription = "Visitor Photo",
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = visitors.fullName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (preApproved || visitors.has_qr==true) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(Pre-Approved)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Blue
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = visitors.purposeOfVisit,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Status: ${status}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(visitors.approvalStatus)
                    )
                }

                if (preApproved && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                    Text(
                        text = "Time Window: $startTime - $endTime",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    val newstart_time = formatToISO8601(startTime)
                    val newend_time = formatToISO8601(endTime)
                    viewModel.approveVisitor(visitors.id,1,newstart_time,newend_time)
                }
            }
        }

        if (showPopup) {
            AlertDialog(
                onDismissRequest = { showPopup = false },
                title = { Text(text = "Update Status") },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {


                        visitors?.photoUrl.let { base64 ->
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
                        Text("Full Name: ${visitors?.fullName}", fontWeight = FontWeight.Bold)
                        Text("Contact Info: ${visitors?.contactInfo}")
                        Text("Purpose: ${visitors?.purposeOfVisit}")
                        Text("Host Employee: ${visitors?.hostEmployee}")
                        Text("Company Name: ${visitors?.companyName}")

                        Spacer(modifier = Modifier.height(16.dp))

                        // Status Field
                        Text(
                            text = "Status: $status",
                            fontWeight = FontWeight.Bold,
                            color = when (status) {
                                "ACCEPTED" -> Color.Green
                                "REJECTED" -> Color.Red
                                else -> Color.Black
                            }
                        )

                        // Display QR code if available
                        if (visitors?.has_qr == true) {
                            visitors?.qr?.let { base64 ->
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





                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Chip(
                                text = "Approve",
                                //color = Color.Green,
                                onClick = {
                                    viewModel.updateVisitorStatus(visitors.id,"APPROVED")
                                    status = "APPROVED"
                                    statusColor = Color.Green
                                    preApproved = false
                                    showPopup = false
                                }
                            )

                            Chip(
                                text = "Reject",
                                //color = Color.Red,
                                onClick = {
                                    viewModel.updateVisitorStatus(visitors.id,"REJECTED")
                                    status = "REJECTED"
                                    statusColor = Color.Red
                                    preApproved = false
                                    showPopup = false
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Chip(
                            text = "Pre-Approve",
                            //color = Color.Blue,
                            onClick = {
                                showPopup = false
                                showTimeSelectionDialog = true
                            }
                        )
                    }
                },
                confirmButton = {

                }
            )
        }

        if (showTimeSelectionDialog) {
            AlertDialog(
                onDismissRequest = { showTimeSelectionDialog = false },
                title = { Text(text = "Set Time Window") },
                text = {
                    Column {
                        Chip(
                            text = if (startTime.isEmpty()) "Set Start Time" else "Start: $startTime",
                            //color = Color.Blue,
                            onClick = {
                                whoIsCalling = "start"
                                showTimeSelectionDialog = false
                                showTimePicker = true
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Chip(
                            text = if (endTime.isEmpty()) "Set End Time" else "End: $endTime",
                            //color = Color.Blue,
                            onClick = {
                                whoIsCalling = "end"
                                showTimeSelectionDialog = false
                                showTimePicker = true
                            },
                            enabled = startTime.isNotEmpty()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showTimeSelectionDialog = false
                            if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
                                preApproved = true
                                status = "APPROVED"
                                statusColor = Color.Blue
                            }
                        }
                    ) {
                        Text("Done")
                    }
                }
            )
        }

        if (showTimePicker) {
            TimePickerDialog(
                onTimeSelected = { time ->
                    if (whoIsCalling == "start") {
                        startTime = time
                        whoIsCalling = ""
                        showTimeSelectionDialog = true
                    } else if (whoIsCalling == "end") {
                        endTime = time
                        whoIsCalling = ""
                        showTimeSelectionDialog = true
                    }
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

@Composable
fun Chip(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            //.background(if (enabled) color else Color.Gray)
            .clickable(enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun TimePickerDialog(onTimeSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = android.app.TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour, minute, true
    )

    DisposableEffect(Unit) {
        timePickerDialog.show()
        onDispose { timePickerDialog.dismiss() }
    }
}


@Composable
fun NotificationPopup(
    notifications: List<Pair<String, String>>, // List of (Message, Timestamp)
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp) // Fixed width
            .height(300.dp) // Fixed height
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .shadow(8.dp)
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = "Notifications",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )

            Divider()

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(notifications) { (message, timestamp) ->
                    NotificationItem(message.trim(), timestamp.trim())
                }
            }
        }
    }
}

@Composable
fun NotificationItem(message: String, timestamp: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Reduce excessive spacing
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = message,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp)) // Small spacing
            Text(
                text = timestamp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
        Divider() // To separate items
    }
}


fun getStatusColor(status: String): Color {
    return when (status) {
        "APPROVED" -> Color.Green
        "REJECTED" -> Color.Red
        else -> Color.Gray
    }
}

fun formatToISO8601(timeInput: String): String {
    val currentDate = LocalDate.now()  // Get the current date
    val parsedTime = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm")) // Parse input time
    return "$currentDate" + "T" + parsedTime.toString() + ":00" // Convert to ISO format
}

