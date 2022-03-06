package com.example.Wickie.data.source.data

class RequestClaimCall {
    var status = 0
    var message : String = "No Message"
    var claimDetail: Claim?  = null
    var claimArray : ArrayList<Claim> = ArrayList<Claim>()
    var claimTotal : Int? = null
}