package com.example.mbanking.viewModel

import android.util.Log
import com.example.mbanking.database.model.User

class RegisterControl {

    companion object {
        var registerControl = RegisterControl()
    }

    fun isUserInformationInsertedCorrectly(user : User) : Boolean {
        return isTextFieldCorrectlyInserted(user.firstName) &&
                isTextFieldCorrectlyInserted(user.secondName) &&
                isPinCorrectlyInserted(user.pin)
    }


    // Methods for name and surname check

    private fun isTextFieldCorrectlyInserted(text : String) : Boolean{
        return isTextFieldsLengthAcceptable(text) &&
                areTextFieldsCharsCorrect(text) &&
                isThereLetterInString(text)
    }

    private fun isTextFieldsLengthAcceptable(str : String) : Boolean {
        return (str.isNotEmpty()) && (str.length < 31)
    }

    private fun areTextFieldsCharsCorrect(str : String) : Boolean{
        return str.areOnlyLettersOrSpace()
    }

    private fun String.areOnlyLettersOrSpace() = all { it.isLetter() || it == ' ' }


    private fun isThereLetterInString(text: String): Boolean {

        for (char in text)
            if (char in 'A'..'Z' || char in 'a'..'z')
                return true

        return false
    }

    // For Pin

    private fun isPinCorrectlyInserted(text : String) : Boolean{
        return isPinLengthAcceptable(text) && isNumeric(text)
    }

    private fun isPinLengthAcceptable(str : String) : Boolean {
        return (str.length >= 4) && (str.length <= 6)
    }

    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }



}