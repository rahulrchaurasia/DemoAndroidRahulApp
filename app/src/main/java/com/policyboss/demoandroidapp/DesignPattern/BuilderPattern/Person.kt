package com.policyboss.demoandroidapp.DesignPattern.BuilderPattern

class Person private constructor(
    val firstName: String?,
    val lastName: String?,
    val age: Int,
    val gender: String?
) {
    class Builder {
        private var firstName: String? = null
        private var lastName: String? = null
        private var age: Int = 0
        private var gender: String? = null
        fun setFirstName(firstName: String) = apply { this.firstName = firstName }
        fun setLastName(lastName: String) = apply { this.lastName = lastName }
        fun setAge(age: Int) = apply { this.age = age }
        fun setGender(gender: String) = apply { this.gender = gender }


        fun build() = Person(firstName, lastName, age, gender)
    }
}
