package com.example.Wickie

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

class Validation (list: Array<EditText>){
    private lateinit var pattern: Pattern;
    private lateinit var matcher: Matcher;
    val DATE_PATTERN =
        "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";
    private lateinit var list: Array<EditText>
    init {
        this.list = list
    }

    fun validateEmpty(array : Array<EditText>): Boolean {
        for (item in array) {
            if(item.text.toString().isEmpty()) {
                item.error = "This field is missing"
                return false
            }
        }
        return true
    }



    fun validateClaim(array : Array<EditText>): Boolean {
        if (validateEmpty(array)) {
            return true

        }
        return false
    }
}