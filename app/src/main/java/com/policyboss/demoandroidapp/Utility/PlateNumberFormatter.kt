package com.policyboss.demoandroidapp.Utility

object PlateNumberFormatter {
    fun formatPlateNumber(input: String): String {
        // Remove all non-alphanumeric characters
        val cleanedInput = input.replace("[^A-Za-z0-9]".toRegex(), "")
        if (cleanedInput.length < 10) return cleanedInput // Return unformatted if incomplete.

        return buildString {
            append(cleanedInput.substring(0, 2)) // MH
            append("-")
            append(cleanedInput.substring(2, 4)) // 02
            append("-")
            append(cleanedInput.substring(4, 6)) // FN
            append("-")
            append(cleanedInput.substring(6))   // 8600
        }
    }
}
