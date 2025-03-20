package com.mruraza.visitotmanagementsystem.ui.theme.interfaces

import com.mruraza.visitotmanagementsystem.ui.theme.Model.Approval
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Notification
import com.mruraza.visitotmanagementsystem.ui.theme.Model.PreApprovalRequest
import com.mruraza.visitotmanagementsystem.ui.theme.Model.PreApprovalResponse
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Visitor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitorApi {
    @GET("/visitors") // Update this to match your actual endpoint path
    fun getAllVisitors(): Call<List<Visitor>>


    //    @PUT("approvals/visitors/{id}/status")
    //    fun updateVisitorStatus(
    //        @Path("id") visitorId: Int,
    //        @Query("status") status: String
    //    ): Call<String>

    @PUT("approvals/visitors/{id}/status")
    fun updateVisitorStatus(
        @Path("id") visitorId: Int,
        @Query("status") status: String
    ): Call<ResponseBody> // Change from String to ResponseBody


    @GET("visitors/notification/0")
    fun getAllNotificationForAdmin(): Call<List<Notification>>


    // this is for user side
    @GET("visitors/notification/user/{id}")
    fun getAllNotificationForUser(
        @Path("id") userId: Int
    ): Call<List<Notification>>


    @POST("pre-approvals/approve")
    fun approvePreApproval(
        @Body request: PreApprovalRequest
    ): Call<PreApprovalResponse>

    @GET("/approvals/valid")
    fun checkValidAdmin(
        @Query("contact_info") contactInfo: String,
        @Query("password") password: String
    ): Call<Boolean>

    @POST("approvals/addAdmin")
    fun addAdmin(@Body approval: Approval): Call<Approval>

}