package com.mruraza.visitotmanagementsystem.ui.theme.Model

data class Visitor(
    val id: Int,
    val fullName: String,
    val contactInfo: String,
    val purposeOfVisit: String,
    val hostEmployee: String,
    val companyName: String,
    val password:String,
    val checkInTime: String,
    val checkOutTime: String?,
    val photoUrl: String,
    val approvalStatus: String,
    val has_qr: Boolean,
    val qr: String?
)

data class VisitorRequest(
    val fullName: String,
    val contactInfo: String,
    val purposeOfVisit: String,
    val hostEmployee: String,
    val companyName: String,
    val password:String,
    val checkInTime: String?,
    val checkOutTime: String?,
    val photoUrl: String
)

data class VisitorResponse(
    val isValid: Boolean
)