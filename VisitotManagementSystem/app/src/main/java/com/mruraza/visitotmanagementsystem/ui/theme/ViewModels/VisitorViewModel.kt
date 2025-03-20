package com.mruraza.visitotmanagementsystem.ui.theme.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Approval
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Notification
import com.mruraza.visitotmanagementsystem.ui.theme.Model.PreApprovalRequest
import com.mruraza.visitotmanagementsystem.ui.theme.Model.PreApprovalResponse
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Visitor
import com.mruraza.visitotmanagementsystem.ui.theme.objects.RetrofitClient
import com.mruraza.visitotmanagementsystem.ui.theme.objects.RetrofitVisitorClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitorViewModel : ViewModel() {
    private val _visitors = MutableLiveData<List<Visitor>>()
    val visitors: LiveData<List<Visitor>> get() = _visitors

    fun fetchVisitors() {
        RetrofitClient.instance.getAllVisitors().enqueue(object : Callback<List<Visitor>> {
            override fun onResponse(call: Call<List<Visitor>>, response: Response<List<Visitor>>) {
                if (response.isSuccessful) {
                    _visitors.postValue(response.body())
                    Log.d("VisitorViewModel", "Visitors fetched successfully: ${response.body()}")
                } else {
                    Log.d("VisitorViewModel", "Response failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Visitor>>, t: Throwable) {
                Log.e("VisitorViewModel", "Error fetching visitors", t)
            }
        })
    }


    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    fun updateVisitorStatus(visitorId: Int, status: String) {
        RetrofitClient.instance.updateVisitorStatus(visitorId, status)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val message = response.body()!!.string() // Convert ResponseBody to string
                        Log.d("VisitorViewModel", message) // Log the response message
                        _updateSuccess.postValue(true)
                    } else {
                        Log.e(
                            "VisitorViewModel",
                            "Failed to update visitor status: ${response.errorBody()?.string()}"
                        )
                        _updateSuccess.postValue(false)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("VisitorViewModel", "Error updating visitor status", t)
                    _updateSuccess.postValue(false)
                }
            })
    }


    // for the notification to admin
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    fun fetchNotificationsForAdmin() {
        RetrofitClient.instance.getAllNotificationForAdmin().enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if (response.isSuccessful) {
                    _notifications.postValue(response.body())
                    Log.d("NotificationViewModel", "Notifications fetched successfully: ${response.body()}")
                } else {
                    Log.e("NotificationViewModel", "Failed to fetch notifications: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                Log.e("NotificationViewModel", "Error fetching notifications", t)
            }
        })
    }

    // for the user all notifications
    private val _userNotifications = MutableLiveData<List<Notification>>()
    val userNotifications: LiveData<List<Notification>> get() = _userNotifications

    fun fetchNotificationsForUser(userId: Int) {
        RetrofitClient.instance.getAllNotificationForUser(userId).enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if (response.isSuccessful) {
                    _userNotifications.postValue(response.body())
                    Log.d("UserNotificationVM", "Notifications fetched for user $userId: ${response.body()}")
                } else {
                    Log.e("UserNotificationVM", "Failed to fetch notifications for user $userId: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                Log.e("UserNotificationVM", "Error fetching notifications for user $userId", t)
            }
        })
    }




    // pre approve
    private val _approvalResponse = MutableLiveData<PreApprovalResponse?>()
    val approvalResponse: LiveData<PreApprovalResponse?> get() = _approvalResponse

    private val _approvalSuccess = MutableLiveData<Boolean>()
    val approvalSuccess: LiveData<Boolean> get() = _approvalSuccess

    fun approveVisitor(visitorId: Int, employeeId: Int, startTime: String, endTime: String) {
        val request = PreApprovalRequest(visitorId, employeeId, startTime, endTime)

        RetrofitClient.instance.approvePreApproval(request).enqueue(object : Callback<PreApprovalResponse> {
            override fun onResponse(call: Call<PreApprovalResponse>, response: Response<PreApprovalResponse>) {
                if (response.isSuccessful) {
                    _approvalResponse.postValue(response.body())
                    _approvalSuccess.postValue(true)
                    Log.d("PreApprovalVM", "Approval successful: ${response.body()}")
                } else {
                    _approvalSuccess.postValue(false)
                    Log.e("PreApprovalVM", "Approval failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PreApprovalResponse>, t: Throwable) {
                _approvalSuccess.postValue(false)
                Log.e("PreApprovalVM", "Error approving visitor", t)
            }
        })
    }


    private val _isValidVisitor = MutableLiveData<Boolean>()
    val isValidVisitor: LiveData<Boolean> get() = _isValidVisitor

    fun checkValidAdmin(contactInfo: String, password: String) {
        Log.d("mru",contactInfo + "  "+ password)
        RetrofitClient.instance.checkValidAdmin(contactInfo, password)
            .enqueue(object : Callback<Boolean> {  // 🔹 Expect Boolean directly
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        _isValidVisitor.postValue(response.body() ?: false)
                        Log.d("VisitorViewModel", "Validation Result: ${response.body()}")
                    } else {
                        Log.e("VisitorViewModel", "Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("VisitorViewModel", "API Call Failed", t)
                }
            })
    }



    private val _admin = MutableLiveData<Approval>()
    val admin: LiveData<Approval> get() = _admin

    fun addAdmin(approval: Approval) {
        RetrofitClient.instance.addAdmin(approval).enqueue(object : Callback<Approval> {
            override fun onResponse(call: Call<Approval>, response: Response<Approval>) {
                if (response.isSuccessful) {
                    _admin.postValue(response.body())
                    Log.d("ApprovalViewModel", "Admin Added: ${response.body()}")
                } else {
                    Log.e("ApprovalViewModel", "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Approval>, t: Throwable) {
                Log.e("ApprovalViewModel", "API Call Failed", t)
            }
        })
    }

}

