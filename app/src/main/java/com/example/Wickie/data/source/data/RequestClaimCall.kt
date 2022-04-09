package com.example.Wickie.data.source.data

/*
* This class is used as return object for claims.
* */
class RequestClaimCall {
    var status = 0
    var message : String = "No Message"
    var claimDetail: Claim?  = null
    var claimArray : ArrayList<Claim> = ArrayList<Claim>()
    var claimTotal : Double? = null
}