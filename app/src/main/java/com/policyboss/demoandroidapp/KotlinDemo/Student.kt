package com.policyboss.demoandroidapp.KotlinDemo

/*
The property of the class can’t be declared inside the secondary constructor.. (eg id here)
This will give an error because here we are declaring a property id of the class in the secondary constructor,
 which is not allowed.

If you want to use some property inside the secondary constructor,
then declare the property inside the class and use it in the secondary constructor:


 */
class Student (var name: String) {

    var id: Int = -1
    init{
        println("Student has got a name as $name")
    }

    constructor(sectionName: String, id: Int) :  this(sectionName){

        this.id = id
    }
//
}