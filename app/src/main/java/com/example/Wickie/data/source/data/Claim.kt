package com.example.Wickie.data.source.data
import java.io.Serializable

class Claim(var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
            var imgUrl : String?, var createdDate : String?, var claimDate : String?
) : Serializable {}