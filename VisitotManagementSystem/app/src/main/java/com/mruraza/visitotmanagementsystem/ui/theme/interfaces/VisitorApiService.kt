package com.mruraza.visitotmanagementsystem.ui.theme.interfaces

import com.mruraza.visitotmanagementsystem.ui.theme.Model.Visitor
import com.mruraza.visitotmanagementsystem.ui.theme.Model.VisitorRequest
import com.mruraza.visitotmanagementsystem.ui.theme.Model.VisitorResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitorApiService {
    @POST("visitors/register")
    fun registerVisitor(@Body visitor: VisitorRequest): Call<Visitor>

    @GET("visitors/{id}/status")
    fun getVisitorStatus(@Path("id") visitorId: Int): Call<String>

    @GET("visitors/{id}")
    fun getVisitorById(@Path("id") visitorId: Int): Call<Visitor>

    @GET("/visitors/valid")
    fun checkValidUser(
        @Query("contact_info") contactInfo: String,
        @Query("password") password: String
    ): Call<Boolean>

    @GET("/visitors/findByContact")
    fun getVisitorByContact(@Query("contactInfo") contactInfo: String): Call<Visitor>

}