package com.trian.cexup_services

import com.google.gson.Gson
import com.trian.data_storage.models.*
import com.trian.data_storage.room.entity.Measurement
import org.json.JSONArray

class ServiceUtils {
    companion object{
        const val url_login="";
        const val base_url="http://app.cexup.com/api";
        const val base_measurement ="http://device.cexup.com/api"
        const val url_get_patient_by_id = "/get-info-user?user_id="
        const val url_get_nurse_by_id = "/admin/nurse"
        const val url_get_all_patient = "/get/patient"
        const val url_get_data_temp_device = "/get-data-tmp?device_code="
        const val url_post_measurement =  "/v2/consumer/record-measurement"
        const val POST_MEASUREMENT_RSGK = "/api-device/public/api/corporate/record-measurement"
        const val url_login_nurse ="/login/nurse"
        const val url_login_nurse_with_qr = "/login/qrCode"


        const val DEFAULT_TOKEN = "qwertyuiop"
        const val MEASUREMENT = "POST_DATA"
        const val TAG_DEBUG = "Cexup Service"

        const val SDK_ID_BPM = "N7jbSW#j7YrFd~sE"
        const val SDK_ID_THERMOMETER = "1XYXF1bm8ZyEM81k"
        const val SDK_ID_WEIGHT = "1??8xz5Zpt6cbw5z"
        const val MAC_BPM = "5D:6C:7E:8F"
        const val MAC_TEMPERATURE = "18:7A:93:C0:7F:64"
        const val MAC_SPO2 = "5D:6C:7E:8H"
        const val MAC_WEIGHT = "5D:6C:7E:8I"
        const val MAC_STETHOSCOPE = "5D:6C:7E:8J"
        const val MAC_HEIGHT = "001-20210101-0001"
        const val MAC_WAIST = "02-20210201-0001" //"002-20210101-0001";

        const val MAC_ECG = "CA:CB:54:57:F8:D2" // "D4:09:1E:AD:9D:4B";

        const val MAC_USG = "CB:CA:52:57:F8:G(" // "D4:09:1E:AD:9D:4B";

        const val MAC_MULTIPARAMETER = "FD:4B:C3:20:9C:0C"
        const val MAC_MULTIPARAMETER2 = "FC:E3:52:FD:41:E6"

        const val name_multiparamater = "Checkme" // 1569";

        const val name_multiparameter2 = "Checkme" //1582


        const val TYPE_TEMPERATURE = 1
        const val TYPE_HEARTRATE = 2
        const val TYPE_BPM = 3
        const val TYPE_BMI = 4
        const val TYPE_HEIGHT = 5
        const val TYPE_WEIGHT = 6
        const val TYPE_WAIST = 7
        const val TYPE_SPO2 = 8
        const val TYPE_STETHOSCOPE = 9
        const val TYPE_ECG = 10
        const val TYPE_PEDOMETER = 11
        const val TYPE_SLEEP = 12
        const val TYPE_RESPIRATION = 13


        fun jsonUtils() = Gson()
    }
    fun toJson(data:Any):String{
        val  gson = Gson();
        return gson.toJson(data)
    }

    fun populateListMeasurement(measurements: List<Measurement>):String{
        var jsonArray = """"""

        measurements.forEachIndexed { index, measurement ->
            if(index > 0){
                jsonArray += ","
            }
          jsonArray += (populateSingleMeasurement(measurement,measurement.type))


        }
        return jsonArray.trimIndent()
    }
    fun populateSingleMeasurement(measurement: Measurement, type: Int?):String{
        when(type){
            TYPE_TEMPERATURE->{
                val temp = jsonUtils().fromJson(measurement.result, TemperatureModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"temperature",
                                "value":"${temp.temperature}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_HEARTRATE->{
                val temp = jsonUtils().fromJson(measurement.result, HeartRateModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"hr",
                                "value":"${temp.hrperminute}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_BPM->{
                val temp = jsonUtils().fromJson(measurement.result, BloodPressureModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"systole",
                                "value":"${temp.systole}"
                            },
                            {
                                "name":"diastole",
                                "value":"${temp.diastole}"
                            },
                            {
                                "name":"hr",
                                "value":"${temp.pulse}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_BMI->{
                val temp = jsonUtils().fromJson(measurement.result, BMIModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"height",
                                "value":"${temp.height}"
                            },
                            {
                                "name":"weight",
                                "value":"${temp.weight}"
                            },
                            {
                                "name":"bmi",
                                "value":"${temp.bmi}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_HEIGHT->{
                val temp = jsonUtils().fromJson(measurement.result, HeightModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"height",
                                "value":"${temp.height}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_WEIGHT->{
                val temp = jsonUtils().fromJson(measurement.result, WeightModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"weight",
                                "value":"${temp.weight}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_WAIST->{
                val temp = jsonUtils().fromJson(measurement.result, WaistModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"waist",
                                "value":"${temp.waist}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_SPO2->{
                val temp = jsonUtils().fromJson(measurement.result, BloodOxygenModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"spo2",
                                "value":"${temp.spo2}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_RESPIRATION->{
                val temp = jsonUtils().fromJson(measurement.result, RespirationModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"respiration",
                                "value":"${temp.breathPerminute}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_SLEEP->{
                val temp = jsonUtils().fromJson(measurement.result, SleepModel::class.java)
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[
                            {
                                "name":"lightsleep",
                                "value":"${temp.lightSleepCount}"
                            },
                            {
                                "name":"deepsleep",
                                "value":"${temp.deepSleepCount}"
                            },
                            {
                                "name":"sleepduration",
                                "value":"${temp.hours}.${temp.minutes}"
                            }
                        ]
                    }
                """.trimIndent()
            }
            TYPE_STETHOSCOPE->{
                return  """
                    {
                        "device_id":"${measurement.device_id}",
                        "type":"${measurement.type}",
                        "asset":"${measurement.result}",
                        "created_at":"${measurement.created_at}",
                        "test_method":"${measurement.test_method}",
                        "result":[]
                    }
                """.trimIndent()
            }
            else->{ return """""".trimIndent()}
        }
    }
}


