package com.policyboss.demoandroidapp.Utility.ExtensionFun

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import com.policyboss.demoandroidapp.Utility.PlateNumberFormatter




//fun EditText.addPlateNumberFormatter() {
//    this.addTextChangedListener(object : TextWatcher {
//        private var isFormatting = false
//        private var previousText = ""
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            previousText = s.toString()
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//        override fun afterTextChanged(s: Editable?) {
//            if (isFormatting) return
//
//            val currentText = s.toString().uppercase().replace("-", "")
//            val formattedText = StringBuilder()
//
//            if (currentText.isNotEmpty()) {
//                // Dynamically add hyphens as user types
//                for (i in currentText.indices) {
//                    when (i) {
//                        0 -> formattedText.append(currentText[i]) // M
//                        1 -> formattedText.append(currentText[i]).append("-") // H-
//                        2 -> formattedText.append(currentText[i]) // 0
//                        3 -> formattedText.append(currentText[i]).append("-") // 2-
//                        4 -> formattedText.append(currentText[i]) // F
//                        5 -> formattedText.append(currentText[i]).append("-") // N-
//                        else -> formattedText.append(currentText[i]) // 8600
//                    }
//                }
//            }
//
//            // Prevent infinite loops while formatting
//            if (currentText != previousText) {
//                isFormatting = true
//                this@addPlateNumberFormatter.setText(formattedText)
//                this@addPlateNumberFormatter.setSelection(formattedText.length)
//                isFormatting = false
//            }
//        }
//    })
//}

fun EditText.addPlateNumberFormatter() {
    this.addTextChangedListener(object : TextWatcher {
        private var isFormatting = false
        private var previousText = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            previousText = s.toString()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isFormatting) return

            val currentText = s.toString().uppercase().replace("-", "")
            val formattedText = StringBuilder()

            if (currentText.isNotEmpty()) {
                for (i in currentText.indices) {
                    when (i) {
                        0 -> formattedText.append(currentText[i]) // M
                        1 -> formattedText.append(currentText[i]).append("-") // MH-
                        2 -> formattedText.append(currentText[i]) // 0
                        3 -> formattedText.append(currentText[i]).append("-") // 02-
                        4 -> formattedText.append(currentText[i]) // F
                        5 -> formattedText.append(currentText[i]).append("-") // FN-
                        else -> formattedText.append(currentText[i]) // Remaining numbers
                    }
                }
            }

            // Apply only if there's a change
            if (formattedText.toString() != previousText) {
                isFormatting = true
                this@addPlateNumberFormatter.setText(formattedText)
                this@addPlateNumberFormatter.setSelection(
                    minOf(formattedText.length, formattedText.length)
                )
                isFormatting = false
            }
        }
    })
}





