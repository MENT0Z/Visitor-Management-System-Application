package com.mruraza.visitotmanagementsystem.ui.theme.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.visitotmanagementsystem.ui.theme.Model.Visitor
import com.mruraza.visitotmanagementsystem.ui.theme.Model.VisitorRequest
import com.mruraza.visitotmanagementsystem.ui.theme.Model.VisitorResponse
import com.mruraza.visitotmanagementsystem.ui.theme.objects.RetrofitVisitorClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitorRegisterViewModel :ViewModel() {
    private val _visitor = MutableLiveData<Visitor>()
    val visitor: LiveData<Visitor> get() = _visitor

    fun registerVisitor(visitorRequest: VisitorRequest) {
        RetrofitVisitorClient.instance.registerVisitor(visitorRequest)
            .enqueue(object : Callback<Visitor> {
                override fun onResponse(call: Call<Visitor>, response: Response<Visitor>) {
                    if (response.isSuccessful) {
                        _visitor.postValue(response.body())
                        Log.d("VisitorRegistrationVM", "Visitor registered: ${response.body()}")
                    } else {
                        Log.e("VisitorRegistrationVM", "Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Visitor>, t: Throwable) {
                    Log.e("VisitorRegistrationVM", "API Call Failed", t)
                }
            })
    }

    private val _visitorStatus = MutableLiveData<String>()
    val visitorStatus: LiveData<String> get() = _visitorStatus

    fun getVisitorStatus(visitorId: Int) {
        RetrofitVisitorClient.instance.getVisitorStatus(visitorId)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        _visitorStatus.postValue(response.body())
                        Log.d("VisitorStatusVM", "Visitor Status: ${response.body()}")
                    } else {
                        Log.e("VisitorStatusVM", "Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("VisitorStatusVM", "API Call Failed", t)
                }
            })
    }


    private val _singlevisitor = MutableLiveData<Visitor>()
    val singlevisitor: LiveData<Visitor> get() = _singlevisitor

    fun getVisitorById(visitorId: Int) {
        RetrofitVisitorClient.instance.getVisitorById(visitorId)
            .enqueue(object : Callback<Visitor> {
                override fun onResponse(call: Call<Visitor>, response: Response<Visitor>) {
                    if (response.isSuccessful) {
                        _singlevisitor.postValue(response.body())
                        Log.d("VisitorFetchVM", "Visitor Fetched: ${response.body()}")
                    } else {
                        Log.e("VisitorFetchVM", "Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Visitor>, t: Throwable) {
                    Log.e("VisitorFetchVM", "API Call Failed", t)
                }
            })
    }




    private val _isValidVisitor = MutableLiveData<Boolean>()
    val isValidVisitor: LiveData<Boolean> get() = _isValidVisitor

    fun checkValidUser(contactInfo: String, password: String) {
        Log.d("mru",contactInfo + "  "+ password)
        RetrofitVisitorClient.instance.checkValidUser(contactInfo, password)
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



    private val _visitorByContact = MutableLiveData<Visitor?>()
    val visitorByContact: LiveData<Visitor?> get() = _visitorByContact

    fun fetchVisitorByContact(contactInfo: String) {
        RetrofitVisitorClient.instance.getVisitorByContact(contactInfo)
            .enqueue(object : Callback<Visitor> {
                override fun onResponse(call: Call<Visitor>, response: Response<Visitor>) {
                    if (response.isSuccessful) {
                        _visitorByContact.value = response.body()
                    } else {
                        _visitorByContact.value = null
                        Log.e("VisitorAPI", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Visitor>, t: Throwable) {
                    _visitorByContact.value = null
                    Log.e("VisitorAPI", "API Call Failed: ${t.message}")
                }
            })
    }
}