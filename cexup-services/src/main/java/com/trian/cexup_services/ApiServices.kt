package com.trian.cexup_services

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.io.File
import java.io.Reader

class ApiServices(var res:onResponse) {
    interface onResponse{
        fun onResult(success: Boolean,data:JSONObject,message:String)
    }


    init {
        FuelManager.instance.basePath = ServiceUtils.base_url;
    }

    fun get(path:String = "") {
        if(path.equals("")){
            res.onResult(false, JSONObject(),"Path Not Valid -> $path")
            return
        }

        Fuel.get(path).header(Headers.CONTENT_TYPE,"").also {
            Log.i(ServiceUtils.TAG_DEBUG,"get ${path}")
        }.response{_,_,result->
                when(result){
                    is Result.Failure->{
                        res.onResult(false, JSONObject(),"${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
                        TODO("convert data to json first")
                        res.onResult(true,data=JSONObject(),"Success")
                    }
                }

        }
    }
    fun post(path: String="",body: JSONObject){
     if(path.equals("")){
         res.onResult(false, JSONObject(),"Path Not Valid -> $path")
         return
     }
     Fuel.post("").jsonBody(body)
         .also { Log.i(ServiceUtils.TAG_DEBUG,"post ${path}") }
         .response{_,_,result->
             when(result){
                 is Result.Failure->{
                     res.onResult(false, JSONObject(),"${result.getException().message}")
                 }
                 is Result.Success->{
                     val(data,_) = result
                     TODO("convert data to json first")
                     res.onResult(true,data=JSONObject(),"Success")
                 }

            }
         }
    }
    fun postFile(path: String="",body: File){
        if(path.equals("")){
            res.onResult(false, JSONObject(),"Path Not Valid -> $path")
            return
        }
        Fuel.post("").body(body)
            .also { Log.i(ServiceUtils.TAG_DEBUG,"post file ${path}") }
            .response{_,_,result->
                when(result){
                    is Result.Failure->{
                        res.onResult(false, JSONObject(),"${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
                        TODO("convert data to json first")
                        res.onResult(true,data=JSONObject(),"Success")
                    }

                }
            }
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
            .response { _,_,result ->
                when(result){
                    is Result.Failure->{
                        res.onResult(false, JSONObject(),"${result.getException().message}")
                    }
                    is Result.Success->{
                        val(data,_) = result
                        TODO("convert data to json first")
                        res.onResult(true,data=JSONObject(),"Success")
                    }

                }
            }
    }

    fun postMeasurement(measurement: JSONObject,patient:JSONObject,nurse:JSONObject){
        var payload = JSONObject();
        payload.put("nurse",nurse)
        payload.put("member",patient)
        payload.put("data",measurement)
        this.post(ServiceUtils.url_post_measurement,payload)
    }
}

