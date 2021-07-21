package com.trian.data_storage.models

import com.google.gson.Gson

data class BloodOxygenModel(var spo2:Float = 0f, var hr:Float =0f)
data class BloodPressureModel(var systole:Float =0f, var diastole:Float = 0f, var pulse:Float=0f)
data class HeightModel(var height:Float = 0f,var method:String = "automatic")
data class WeightModel (var  weight:Float =0f, var method:String = "automatic")
data class BMIModel(var weight: Float =0f,var height: Float=0f,var bmi:Float=0f)
data class WaistModel(var waist:Float =0f,var method:String = "automatic")
data class TemperatureModel(var temperature:Float =0f,var method: String ="asutomatic")
data class RespirationModel(var breathPerminute:Int);
data class SleepModel(var hours:Int,var minutes:Int,var lightSleepCount:Int,var deepSleepCount:Int)
data class HeartRateModel(var hrperminute:Float =0f)

data class StethoscopeModel(var position: String, var filename: String, var dot: String)


data class ResultMeasurement(var name:String, var value:Any?)

fun BloodPressureModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("systole",this.systole))
    list.add(ResultMeasurement("diastole",this.diastole))
    list.add(ResultMeasurement("hr",this.pulse))
    return gson.toJson(list)
}
fun RespirationModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("respiration",this.breathPerminute))
    return gson.toJson(list)
}
fun HeartRateModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("hr",this.hrperminute))
    return gson.toJson(list)
}
fun BloodOxygenModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("hr",this.hr))
    list.add(ResultMeasurement("spo2",this.spo2))
    return gson.toJson(list)
}

fun HeightModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("height",this.height))

    return gson.toJson(list)
}
fun WeightModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("weight",this.weight))

    return gson.toJson(list)
}
fun BMIModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("height",this.height))
    list.add(ResultMeasurement("weight",this.weight))
    list.add(ResultMeasurement("bmi",this.bmi))

    return gson.toJson(list)
}
fun WaistModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("waist",this.waist))
    return gson.toJson(list)
}

fun TemperatureModel.toResult():String{
    var list = mutableListOf<ResultMeasurement>()
    val gson:Gson = Gson()
    list.add(ResultMeasurement("temperature",this.temperature))
    return gson.toJson(list)
}