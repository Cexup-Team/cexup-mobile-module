package com.trian.cexup_services

import com.google.gson.Gson

class ServicesConverter {

    companion object{
        @JvmStatic
        fun <T> toData(data:Any,clazz:Class<T>){
            val gson:Gson = Gson()

        }
    }
}