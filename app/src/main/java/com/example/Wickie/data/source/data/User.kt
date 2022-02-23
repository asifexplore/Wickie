package com.example.Wickie.data.source.data
import java.io.Serializable

class User : Serializable {
    var user_id : Int?
        get(){
            return user_id
        }
        set(value) {
            user_id = value
        }

    var user_name : String?
        get(){
            return user_name
        }
        set(value) {
            user_name = value
        }
    var user_email : String?
        get(){
            return user_email
        }
        set(value) {
            user_email = value
        }
    var user_pw : String?
        get(){
            return user_pw
        }
        set(value) {
            user_pw = value
        }
    var user_dob : String?
        get(){
            return user_dob
        }
        set(value) {
            user_dob = value
        }
}