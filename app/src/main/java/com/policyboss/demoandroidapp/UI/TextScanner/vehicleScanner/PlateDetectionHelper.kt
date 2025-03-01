package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner

import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.text.Text
import com.policyboss.demoandroidapp.Constant


/**
 * Helper class for vehicle license plate detection and processing
 */
class PlateDetectionHelper {

    companion object {
        private val TAG = "PlateDetectionHelper"
        
        // List of valid Indian state codes for validation
        private val validStateCodes = setOf(
            "AP", "AR", "AS", "BR", "CG", "CH", "DD", "DL", "DN", "GA",
            "GJ", "HP", "HR", "JH", "JK", "KA", "KL", "LA", "LD", "MH",
            "ML", "MN", "MP", "MZ", "NL", "OD", "PB", "PY", "RJ", "SK",
            "TN", "TR", "TS", "UK", "UP", "WB"
        )
        
        /**
         * Extracts vehicle number from detected text
         */
        fun extractVehicleNumber(text: String): String? {
            Log.d(Constant.TAG, "Input text for extraction: $text")

            // Clean the text by removing all non-alphanumeric characters
            val cleanText = text.replace("[^A-Za-z0-9]".toRegex(), "").uppercase()
            Log.d(Constant.TAG, "Cleaned text (special chars removed): $cleanText")

            // Look for standard vehicle number patterns
            val vehicleNumberPattern = Regex("[A-Z]{2}\\d{1,2}[A-Z]{1,3}\\d{1,4}")
            val matches = vehicleNumberPattern.findAll(cleanText).toList()

            // First check for plates that start with valid state codes
            for (match in matches) {
                val potentialPlate = match.value
                if (potentialPlate.length >= 2) {
                    val stateCode = potentialPlate.substring(0, 2)
                    if (validStateCodes.contains(stateCode)) {
                        Log.d(Constant.TAG, "Found match with valid state code: $potentialPlate")
                        return potentialPlate
                    }
                }
            }

            // Fix for missing first letter
            if (cleanText.length >= 2) {
                val firstChar = cleanText[0]
                // Check if the first character is a valid second letter of a state code
                for (stateCode in validStateCodes) {
                    if (stateCode[1] == firstChar) {
                        // Try prepending the first letter of the state code
                        val correctedText = stateCode[0] + cleanText
                        val correctedMatches = vehicleNumberPattern.findAll(correctedText).toList()
                        if (correctedMatches.isNotEmpty()) {
                            val potentialPlate = correctedMatches.first().value
                            Log.d(Constant.TAG, "Corrected plate by adding state prefix: $potentialPlate")
                            return potentialPlate
                        }
                    }
                }
            }

            // If no matches found with valid state codes, return the first match
            if (matches.isNotEmpty()) {
                Log.d(Constant.TAG, "No valid state code found, returning first match: ${matches.first().value}")
                return matches.first().value
            }

            // Try a more lenient pattern as last resort
            val simplifiedPattern = Regex("[A-Z0-9]{8,11}")
            val simpleMatches = simplifiedPattern.findAll(cleanText).toList()
            if (simpleMatches.isNotEmpty()) {
                val potentialPlate = simpleMatches.first().value
                Log.d(Constant.TAG, "Found potential plate with simplified pattern: $potentialPlate")
                return potentialPlate
            }

            Log.d(Constant.TAG, "No vehicle number pattern detected")
            return null
        }

        /**
         * Validates if the extracted text is a proper vehicle number
         */
        fun isValidVehicleNumber(text: String): Boolean {
            // Clean the input before validation - remove all non-alphanumeric characters
            val cleanText = text.replace("[^A-Za-z0-9]".toRegex(), "").uppercase()
            Log.d(Constant.TAG, "Validating vehicle number (cleaned): $cleanText")

            // Indian vehicle numbers are typically 9-11 characters long
            if (cleanText.length !in 9..11) {
                Log.d(Constant.TAG, "Invalid length: ${cleanText.length} characters (must be 9-11)")
                return false
            }

            // Check if the license plate starts with a valid state code
            val stateCode = cleanText.substring(0, 2)
            if (!validStateCodes.contains(stateCode)) {
                Log.d(Constant.TAG, "Invalid state code: $stateCode")
                return false
            }

            // Updated regex to match the structure of Indian vehicle numbers
            val regex = "^[A-Z]{2}\\d{1,2}[A-Z]{1,3}\\d{4}$".toRegex()

            val isValid = regex.matches(cleanText)

            // Debug logging to see if the regex is matching
            if (!isValid) {
                Log.d(Constant.TAG, "Regex match failed for: $cleanText")

                // Break down the components to see which part fails
                val remainingText = cleanText.substring(2)
                val digitsCount = remainingText.count { it.isDigit() }
                val lettersCount = remainingText.count { it.isLetter() }

                Log.d(Constant.TAG, "Structure breakdown - State: $stateCode, Remaining Digits: $digitsCount, Remaining Letters: $lettersCount")
            } else {
                Log.d(Constant.TAG, "Vehicle number validated successfully")
            }

            return isValid
        }

        /**
         * Formats vehicle number by removing all non-alphanumeric characters
         */
        fun formatVehicleNumber(vehicleNumber: String): String {
            // Remove all non-alphanumeric characters and convert to uppercase
            return vehicleNumber.replace("[^A-Za-z0-9]".toRegex(), "").uppercase()
        }

        /**
         * Gets license plate text from the detection rectangle
         * Focus on up to 2 lines max - optimal for standard Indian plates
         */
        fun getLicensePlateText(visionText: Text, detectionRectangle: Rect): String {
            val plateTextLines = mutableListOf<String>()
            val candidateLines = mutableListOf<Pair<String, Float>>()

            // Process blocks of text
            for (block in visionText.textBlocks) {
                // Process each line in the block
                for (line in block.lines) {
                    val boundingBox = line.boundingBox ?: continue

                    // Check if this line is within or intersects our detection rectangle
                    if (isTextInDetectionZone(boundingBox, detectionRectangle)) {
                        // Calculate overlap percentage to prioritize text
                        val overlapScore = calculateOverlapScore(boundingBox, detectionRectangle)

                        // Add to candidates with overlap score
                        candidateLines.add(Pair(line.text, overlapScore))
                    }
                }
            }

            // Sort by overlap score (higher score first)
            candidateLines.sortByDescending { it.second }

            // Take the top 2 lines maximum
            val maxLines = minOf(2, candidateLines.size)
            for (i in 0 until maxLines) {
                plateTextLines.add(candidateLines[i].first)
            }

            // Join the lines with a newline character
            return plateTextLines.joinToString("\n")
        }

        /**
         * Calculate a score based on overlap and position within the detection zone
         * Higher score = better match for license plate
         */
        private fun calculateOverlapScore(textBox: Rect, detectionZone: Rect): Float {
            val intersection = Rect()
            if (!intersection.setIntersect(textBox, detectionZone)) {
                return 0f
            }

            // Calculate overlap percentage
            val intersectionArea = intersection.width() * intersection.height()
            val textBoxArea = textBox.width() * textBox.height()
            val overlapPercentage = intersectionArea.toFloat() / textBoxArea.toFloat()

            // Calculate distance from center (normalized to 0-1, where 0 is center)
            val textCenterX = textBox.centerX()
            val textCenterY = textBox.centerY()
            val zoneCenterX = detectionZone.centerX()
            val zoneCenterY = detectionZone.centerY()

            val maxPossibleDistance = Math.sqrt(
                Math.pow(detectionZone.width() / 2.0, 2.0) +
                        Math.pow(detectionZone.height() / 2.0, 2.0)
            ).toFloat()

            val actualDistance = Math.sqrt(
                Math.pow((textCenterX - zoneCenterX).toDouble(), 2.0) +
                        Math.pow((textCenterY - zoneCenterY).toDouble(), 2.0)
            ).toFloat()

            val normalizedDistance = actualDistance / maxPossibleDistance
            val proximityScore = 1f - normalizedDistance

            // Weight proximity higher than pure overlap
            return (overlapPercentage * 0.4f) + (proximityScore * 0.6f)
        }

        /**
         * Determines if text is in the detection zone based on overlap percentage
         */
        private fun isTextInDetectionZone(textBox: Rect, detectionZone: Rect): Boolean {
            // Check if the text is completely inside the detection zone
            if (detectionZone.contains(textBox)) {
                return true
            }

            // Check for significant overlap (>50% of the text box area)
            val intersection = Rect()
            if (intersection.setIntersect(textBox, detectionZone)) {
                val intersectionArea = intersection.width() * intersection.height()
                val textBoxArea = textBox.width() * textBox.height()

                return intersectionArea > (textBoxArea * 0.3)
            }

            return false
        }
    }
}