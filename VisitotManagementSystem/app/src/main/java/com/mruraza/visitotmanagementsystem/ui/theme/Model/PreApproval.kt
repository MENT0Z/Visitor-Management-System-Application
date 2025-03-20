package com.mruraza.visitotmanagementsystem.ui.theme.Model

data class PreApprovalRequest(
    val visitorId: Int,
    val employeeId: Int,
    val scheduledStartTime: String,
    val scheduledEndTime: String
)

data class PreApprovalResponse(
    val id: Int,
    val visitorId: Int,
    val employeeId: Int,
    val scheduledStartTime: String,
    val scheduledEndTime: String,
    val qrCode: String,
    val checkedIn: Boolean,
    val status: String,
    val createdAt: String
)
