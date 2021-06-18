package com.trian.cexup_services

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.result.Result
import com.trian.data_storage.room.entity.Measurement
import com.trian.data_storage.room.entity.Nurse
import com.trian.data_storage.room.entity.Patient
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.Reader


class ApiServices {
    interface onResponse{
        fun onResult(success: Boolean,data:JSONObject?,message:String)
    }
    interface AuthResponse{
        fun onResult(success: Boolean,data:Nurse?,token:String?,message: String)
    }
    interface PatientResponse{
        fun onResult(success: Boolean,data: List<Patient>?,message: String)
    }
    private var response:onResponse? = null;

    init {
        FuelManager.instance.basePath = ServiceUtils.base_url;
    }
    fun utils():ServiceUtils{
        return ServiceUtils();
    }

    fun addListener(onResponse: onResponse):ApiServices{
        this.response = onResponse;
        return ApiServices()
    }

    fun login(username:String,password:String, authResponse: AuthResponse){

        Fuel.upload(ServiceUtils.url_login_nurse)
            .also { Log.i(ServiceUtils.TAG_DEBUG,"login nurse") }
            .add(InlineDataPart(username,name = "username"),InlineDataPart(password,name = "password"))
            .responseString {
                    request,_,result->
                when(result){
                    is Result.Failure->{
                        Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}");

                        authResponse?.onResult(false, null,"","${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
//                        Log.e(ServiceUtils.TAG_DEBUG,"${data.toString()}")

                        data?.let {
                            var jsonObject:JSONObject = JSONObject(it).getJSONObject("user");
                            var token:String = JSONObject(it).getString("access_token");

                            authResponse?.onResult(true,data= Nurse(0,
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("gender"),
                                jsonObject.getString("phone_number"),
                                jsonObject.getString("type"),
                                jsonObject.getString("no_type"),
                                jsonObject.getString("address")
                                ),token=token,
                                "Success")
                        }
                    }
                }
            }

    }
    fun getAllPatient(token: String?,patientResponse: PatientResponse) {
        Fuel.get(ServiceUtils.url_get_all_patient).header(Headers.AUTHORIZATION,"Bearer $token").also {
            Log.i(ServiceUtils.TAG_DEBUG,"get all patient")
        }.responseString{request,_,result->
            when(result){
                is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}");
                    patientResponse?.onResult(false, null,"${result.getException().message}")
                }
                is Result.Success->{
                    val(data,_) = result
                    data?.let {
                        var jsonArray:JSONArray = JSONObject(it).getJSONArray("data")
                        var users = mutableListOf<Patient>()
                        for (i in 0 until jsonArray.length()){
                            val user = jsonArray.getJSONObject(i)

                            val patient = Patient(
                                user.getInt("user_id"),
                                user.getString("name"),
                                user.getString("email"),
                                user.getString("gender"),
                                user.getString("phone_number"),
                                user.getString("address"),
                                user.getString("type"),
                                user.getString("no_type"))
                            users.add(patient)
                        }
                        patientResponse?.onResult(true,data=users,"Success")
                    }

                }
            }

        }
    }
    fun get(path:String = "") {
        if(path.equals("")){
            response?.onResult(false, JSONObject(),"Path Not Valid -> $path")

            return
        }


        Fuel.get(path).header(Headers.CONTENT_TYPE,"").also {
            Log.i(ServiceUtils.TAG_DEBUG,"get ${path}")
        }.responseString{request,_,result->
                when(result){
                    is Result.Failure->{
                        Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}");
                        response?.onResult(false, JSONObject(),"${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${data.toString()}")
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.toString()}")
                        var json:JSONObject = JSONObject();
                        data?.let {
                            response?.onResult(true,data=JSONObject(it),"Success")
                        }

                    }
                }

        }
    }
    fun get(path: String,onResponse: onResponse){
        this.response = onResponse;
        this.get(path)
    }
    fun post(path: String="",body: JSONObject){
        Log.e(ServiceUtils.TAG_DEBUG,path)
     if(path.equals("")){
         response?.onResult(false, JSONObject(),"Path Not Valid -> $path")
         return
     }
     Fuel.post(path)
         .jsonBody(body)
         .also { Log.i(ServiceUtils.TAG_DEBUG,"post ${path}") }
         .responseString{request,_,result->
             when(result){
                 is Result.Failure->{
                     response?.onResult(false, JSONObject(),"${result.getException().message}")
                 }
                 is Result.Success->{
                     val(data,_) = result
                     Log.e(ServiceUtils.TAG_DEBUG,"${data.toString()}")
                     Log.e(ServiceUtils.TAG_DEBUG,"${request.toString()}")
                     data?.let {
                         response?.onResult(true,data=JSONObject(it),"Success")
                     }

                 }

            }
         }
    }
    fun post(path: String,body: JSONObject,onResponse: onResponse){
        this.response = onResponse;
        this.post(path,body)
    }
    fun postFile(path: String="",body: File){
        if(path.equals("")){
            response?.onResult(false, JSONObject(),"Path Not Valid -> $path")
            return
        }
        Fuel.post("").body(body)
            .also { Log.i(ServiceUtils.TAG_DEBUG,"post file ${path}") }
            .responseString{request,_,result->
                when(result){
                    is Result.Failure->{
                        response?.onResult(false, JSONObject(),"${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${data.toString()}")
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.toString()}")
                       data?.let {
                           response?.onResult(true,data= JSONObject(it),"Success")

                       }

                    }

                }
            }
    }
    fun postFile(path: String="",body: File,onResponse: onResponse){
        this.response = onResponse;
        this.postFile(path,body)
    }

    fun postStethoscopeMeasurement(fileImage:File,measurement: String,patient:String,nurse:String,device_id:String,test_method:String,type_device:String,name_device:String,created_at:String){
        Fuel.upload(ServiceUtils.base_url+ServiceUtils.url_post_measurement)
            .add(
                FileDataPart(fileImage, name = "file", filename="contents.json"),
                InlineDataPart(nurse,name = "nurse"),
                InlineDataPart(patient,name = "member"),
                InlineDataPart(measurement,name = "result"),
                InlineDataPart(device_id,name = "device_id"),
                InlineDataPart(test_method,name="test_method"),
                InlineDataPart(type_device,name = "type"),
                InlineDataPart(name_device,name = "name"),
                InlineDataPart(created_at,name = "created_at")
            )
            .responseString { request,_,result ->
                when(result){
                    is Result.Failure->{
                        response?.onResult(false, JSONObject(),"${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${data.toString()}")
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.toString()}")
                        data?.let {
                            response?.onResult(true,data=JSONObject(it),"Success")
                        }


                    }

                }
            }
    }
    fun postStethoscopeMeasurement(fileImage:File,measurement: String,patient:String,nurse:String,device_id:String,test_method:String,type_device:String,name_device:String,created_at:String,onResponse: onResponse) {
        this.response = response
        this.postStethoscopeMeasurement(fileImage,measurement,patient,nurse,device_id,test_method,type_device,name_device,created_at)
    }


    fun sendMeasurement(measurement:List<Measurement>,patient: Patient,nurse: Nurse){

        Fuel.upload(ServiceUtils.url_post_measurement,Method.POST)
            .add(
                InlineDataPart(utils().toJson(patient),name = "member"),
                InlineDataPart(utils().toJson(nurse),name = "nurse"),
                InlineDataPart(utils().toJson(measurement),name = "data")
            ).responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        data?.let {
                            this.response?.onResult(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    this.response?.onResult(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurement(measurement:List<Measurement>,patient: Patient,nurse: Nurse,onResponse: onResponse){

        Fuel.upload(ServiceUtils.url_post_measurement,Method.POST)
            .add(
                InlineDataPart(utils().toJson(patient),name = "member"),
                InlineDataPart(utils().toJson(nurse),name = "nurse"),
                InlineDataPart(utils().toJson(measurement),name = "data")
            ).responseString{
                request, response, result ->
                    when(result){
                        is Result.Success->{
                            val(data,_) = result
                            data?.let {
                                onResponse.onResult(true,JSONObject(it),"")
                            }
                        }is Result.Failure->{
                            onResponse.onResult(false,null,"${result.getException().message}")
                        }

            }
            }
    }
}


