package com.mruraza.visitotmanagementsystem.ui.theme.Model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("notificationId") val notificationId: Int,
    @SerializedName("send_to") val sendTo: Int,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int?, // Nullable
    @SerializedName("notificationTime") val notificationTime: String
)