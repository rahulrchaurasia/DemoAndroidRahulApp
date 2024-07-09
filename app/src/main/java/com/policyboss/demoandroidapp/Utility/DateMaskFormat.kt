package com.policyboss.demoandroidapp.Utility

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.policyboss.demoandroidapp.DataModel.BankModel.BankEntity
import java.util.Calendar
import java.util.Locale

import java.util.*

class DateMaskFormat(
    private val separator: String,
    private val dateEditText: EditText,
    private var onLastEntry: ((strData: String) -> Unit)
) : TextWatcher {

    companion object {
        private const val MAX_LENGTH = 8
        private const val MIN_LENGTH = 2
    }

    private var updatedText: String = ""
    private var editing = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No changes needed here
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        if (text.toString() == updatedText || editing) return

        val digits = text.toString().replace("\\D".toRegex(), "")
        val length = digits.length

        if (length <= MIN_LENGTH) {
            updatedText = digits
            return
        }

        if (length > MAX_LENGTH) {
            updatedText = digits.substring(0, MAX_LENGTH)
        }

        val day: String
        val month: String
        val year: String?

        if (length <= 4) {
            day = digits.substring(0, 2)
            month = digits.substring(2)
            updatedText = String.format(Locale.US, "%s%s%s", day, separator, month)
        } else {
            day = digits.substring(0, 2)
            month = digits.substring(2, 4)
            year = digits.substring(4)
            updatedText = String.format(Locale.US, "%s%s%s%s%s", day, separator, month, separator, year)
        }
    }

    override fun afterTextChanged(editable: Editable?) {
        if (editing) return

        editing = true
        editable?.clear()
        editable?.insert(0, updatedText)
        editing = false
        if(editable?.length?:0 == 10 ){
            onLastEntry?.invoke(editable.toString())
        }

    }

   }
