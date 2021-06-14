package com.trian.cexup_services

import com.google.gson.Gson
import java.util.*

class ServiceUtils {
    companion object{
        var url_login="";
        var base_url="";
        var url_get_patient_by_id = "/api/get-info-user?user_id="
        var url_get_nurse_by_id = "/rsgk-cexup-api/public/api/admin/nurse"
        var url_get_all_patient = "/rsgk-cexup-api/public/api/get/patient"
        var url_get_data_temp_device = "/api-device/public/api/get-data-tmp?device_code="
        var url_post_measurement =  "/api-device/public/api/record-measurement"
        var url_login_nurse ="/rsgk-cexup-api/public/api/login/nurse"
        var url_login_nurse_with_qr = "/rsgk-cexup-api/public/api/login/qrCode"
        var TAG_DEBUG = "Cexup Service"

       fun jsonUtils() = Gson()
    }
}


