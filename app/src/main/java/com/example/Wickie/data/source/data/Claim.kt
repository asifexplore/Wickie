package com.example.Wickie.data.source.data

import java.io.Serializable

class Claim: Serializable {
    var claim_id : Int?
        get(){
            return claim_id
        }
        set(value) {
            claim_id = value
        }
    var claims_spend_date : String?
        get() {
            return claims_spend_date
        }
        set(value) {
            claims_spend_date = value
        }
    var claims_created_date : String?
        get() {
            return claims_created_date
        }
        set(value) {
            claims_created_date = value
        }

    var claims_amount : Double?
        get() {
            return claims_amount
        }
        set(value) {
            claims_amount = value
        }


    var claims_type : String?
        get() {
            return claims_type
        }
        set(value) {
            claims_type = value
        }

    var claims_reason : String?
        get() {
            return claims_reason
        }
        set(value) {
            claims_reason = value
        }

    var claims_imageURI : Int?
        get() {
            return claims_imageURI
        }
        set(value) {
            claims_imageURI = value
        }
}