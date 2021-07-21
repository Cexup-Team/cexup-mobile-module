package com.trian.cexup_services

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.result.Result
import com.trian.data_storage.models.*
import com.trian.data_storage.room.entity.Measurement
import com.trian.data_storage.room.entity.Nurse
import com.trian.data_storage.room.entity.Patient
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class ApiServices {
    /**
     * callback impl response
     * @param success
     * @param data
     * @param message
     *
     * **/
    interface onResponse{
        fun onResult(success: Boolean,data:JSONObject?,message:String)
    }
    /**
     *
     * **/
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
    /**
     *
     *
     * **/
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
                                message =  "Success")
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

                            val patient = Patient(1,
                                user.getString("user_id"),
                                user.getString("type"),
                                user.getString("no_type"),
                                user.getString("doctor_id"),
                                user.getString("speciality_id"),
//                                user.getString("hospital"),
                                user.getString("hospital_active"),
                                user.getString("name"),
                                user.getString("username"),
                                user.getString("gender"),
                                user.getString("email"),
                                user.getString("phone_number"),
                                user.getString("address"),
                                user.getString("thumb")
                            )
                            users.add(patient)
                        }
                        patientResponse?.onResult(true,data=users,message = "Success")
                    }

                }
            }

        }
    }
    /*
    * send measurement consumer
    * */
    fun sendMeasurementKt( measurement:Measurement, patient: Patient, onResponse:(success: Boolean,data:JSONObject?,message:String)->Unit
    ){
        val jsonString =
            """
            {
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateSingleMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.headers}")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurementKt(measurement:List<Measurement>,patient: Patient,onResponse: (success: Boolean,data:JSONObject?,message:String)->Unit){
        val jsonString =
            """
            {
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateListMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"$jsonString")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurement(measurement:Measurement,patient: Patient,onResponse: onResponse){
        val jsonString =
            """
            {
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateSingleMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.headers}")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse.onResult(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse.onResult(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurement(measurement:List<Measurement>,patient: Patient,onResponse: onResponse){
        val jsonString =
            """
            {
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateListMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"$jsonString")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse.onResult(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse.onResult(false,null,"${result.getException().message}")
                }

                }
            }
    }

    /*
    * send measurement consumer
    * */

    fun sendMeasurementCorporateKt( measurement:Measurement, patient: Patient,nurse: Nurse, onResponse:(success: Boolean,data:JSONObject?,message:String)->Unit
    ){
        val jsonString =
            """
            {
                "nurse":{
                    "type":"${nurse.type}",
                    "no_type":"${nurse.no_type}",
                    "name":"${nurse.name}",
                    "email":"${nurse.email}",
                    "gender":"${nurse.gender}",
                    "phone_number":"${nurse.phoneNumber}",
                    "address":"${nurse.address}"
                },
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateSingleMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.headers}")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurementCorporateKt(measurement:List<Measurement>,patient: Patient,nurse: Nurse,onResponse: (success: Boolean,data:JSONObject?,message:String)->Unit){
        val jsonString =
            """
            {"nurse":{
                    "type":"${nurse.type}",
                    "no_type":"${nurse.no_type}",
                    "name":"${nurse.name}",
                    "email":"${nurse.email}",
                    "gender":"${nurse.gender}",
                    "phone_number":"${nurse.phoneNumber}",
                    "address":"${nurse.address}"
                    },
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateListMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"$jsonString")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurementCorporate(measurement:Measurement,patient: Patient,nurse: Nurse,onResponse: onResponse){
        val jsonString =
            """
            {
            "nurse":{
                    "type":"${nurse.type}",
                    "no_type":"${nurse.no_type}",
                    "name":"${nurse.name}",
                    "email":"${nurse.email}",
                    "gender":"${nurse.gender}",
                    "phone_number":"${nurse.phoneNumber}",
                    "address":"${nurse.address}"
                },
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateSingleMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"${request.headers}")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse.onResult(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse.onResult(false,null,"${result.getException().message}")
                }

                }
            }
    }

    fun sendMeasurementCorporate(measurement:List<Measurement>,patient: Patient,nurse: Nurse,onResponse: onResponse){
        val jsonString =
            """
            {
                "nurse":{
                    "type":"${nurse.type}",
                    "no_type":"${nurse.no_type}",
                    "name":"${nurse.name}",
                    "email":"${nurse.email}",
                    "gender":"${nurse.gender}",
                    "phone_number":"${nurse.phoneNumber}",
                    "address":"${nurse.address}"
                },
                "member":{
                    "type":"${patient.type}",
                    "no_type":"${patient.no_type}",
                    "name":"${patient.name}",
                    "email":"${patient.email}",
                    "gender":"${patient.gender}",
                    "phone_number":"${patient.phone_number}",
                    "address":"${patient.address}"
                },
                "data":[
                    ${ServiceUtils().populateListMeasurement(measurement)}
                ]
            }
        """.trimIndent()
        FuelManager.instance.basePath = ServiceUtils.base_measurement


        Fuel.post(ServiceUtils.url_post_measurement)
            .header("Content-Type","application/json")
            .body(jsonString)
            .responseString{
                    request, response, result ->
                when(result){
                    is Result.Success->{
                        val(data,_) = result
                        Log.e(ServiceUtils.TAG_DEBUG,"$jsonString")
                        data?.let {
                            Log.e(ServiceUtils.TAG_DEBUG,"${JSONObject(it)}")
                            onResponse.onResult(true,JSONObject(it),"")
                        }
                    }is Result.Failure->{
                    Log.e(ServiceUtils.TAG_DEBUG,"${result.getException().message}")
                    onResponse.onResult(false,null,"${result.getException().message}")
                }

                }
            }
    }

}


