package com.trian.cexup_services

import com.google.gson.Gson

class ServiceUtils {
    companion object{
        const val url_login="";
        const val base_url="https://cexup.com/c-api/public/api";
        const val url_get_patient_by_id = "/get-info-user?user_id="
        const val url_get_nurse_by_id = "/admin/nurse"
        const val url_get_all_patient = "/get/patient"
        const val url_get_data_temp_device = "/get-data-tmp?device_code="
        const val url_post_measurement =  "/consumer/record-measurement"
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


        const val TYPE_WEIGHT = 1
        const val TYPE_HEIGHT = 2
        const val TYPE_WAIST = 3
        const val TYPE_BPM = 4
        const val TYPE_TEMPERATURE = 5
        const val TYPE_PERSONALTEMPERATURE = 9
        const val TYPE_MULTIPARAMETER = 10
        const val TYPE_STETHOSCOPE = 6
        const val TYPE_SPO2 = 7
        const val TYPE_USG = 8
        const val TYPE_BPLIST = 11
        const val TYPE_DAILY = 12
        const val TYPE_ECGLIST = 13
        const val TYPE_OXIMETER = 14
        const val TYPE_PEDOMETER = 15
        const val TYPE_SLEEP = 16
        const val TYPE_THERMO = 17


        const val NAME_HEIGHT = "HEIGHT"
        const val NAME_WEIGHT = "WEIGHT"
        const val NAME_WAIST = "WAIST"
        const val NAME_BPM = "BPM"
        const val NAME_TEMPERATURE = "TEMPERATURE"
        const val NAME_PERSONALTEMPERATURE = "PERSONAL TEMPERATURE"

        const val NAME_STETHOSCOPE = "STETHOSCOPE"
        const val NAME_SPO2 = "SPO2"
        const val NAME_USG = "USG"

        const val TEST_MANUAL = "manual"
        const val TEST_AUTO = "automatic"

        const val POSITION_SELECT = "SELECT"
        const val POSITION_MANUAL = "MANUAL"
        const val POSITION_AUTO = "AUTO"

        const val JANTUNG = "HEART"
        const val PARU = "LUNG"
        const val PERUT = "STOMACH"
        const val KEY_MESSAGE_LOADING = "ONLOADING"

        var isPlaying = false

        const val FOLDER_CEXUP = "/CexupDevice"
        const val KEY_NAME_FILE = "file_name"
        const val DIR_USG = "CexupDevice/USG"

       fun jsonUtils() = Gson()
    }
    fun toJson(data:Any):String{
        val  gson = Gson();
        return gson.toJson(data)
    }
}


