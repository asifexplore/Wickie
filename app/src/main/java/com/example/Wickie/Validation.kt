package com.example.Wickie

import android.view.View
import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
*   Validation will be responsible for validating the inputs
*
* Functions Within:
*  ==========================================================================
* Function Name: validateEmpty
* Function Purpose: check if the any of the inputs is empty
* Function Arguments: array (Array<EditText>)
* Results:
*         Success: Able to proceed
*         Failed: Will indicate error if the corresponding field is empty
*---------------------------------------------------
* Function Name: validateClaim
* Function Purpose: check if inputs required for claims are met
* Function Arguments: no argument
* Results:
*         Success: Able to proceed
*         Failed: Will indicate error if the corresponding input requirement not met
*---------------------------------------------------
*/

class Validation (){

    fun validateEmptyEditText(array : Array<EditText>): Boolean {
        for (item in array) {
            if(item.text.toString().isEmpty()) {
                item.error = "This field is missing"
                return false
            }
        }
        return true
    }

    fun validateLogin(username: EditText, password: EditText) : Boolean {
        val loginInputs = arrayOf<EditText>(username, password)
        if (validateEmptyEditText(loginInputs))
        {
            return true
        }
        return false
    }
}