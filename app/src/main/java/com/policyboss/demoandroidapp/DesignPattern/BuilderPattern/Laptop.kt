package com.policyboss.demoandroidapp.DesignPattern.BuilderPattern


// Donot Refer IT , Refer Laptop Demo
class Laptop(builder: Builder) {
     val processor: String = builder.processor
     val ram: String = builder.ram
    private val battery: String = builder.battery
    private val screenSize: String = builder.screenSize

    // Builder class
    class Builder(processor: String) {
        var processor: String = processor // this is necessary

        // optional features
        var ram: String = "2GB"
        var battery: String = "5000MAH"
        var screenSize: String = "15inch"



//        fun setRam(ram: String): Builder {
//            this.ram = ram
//            return this
//        }


        /**********************************************************************************************************/
        //                     APPLY
        // Note :-- apply runs on the object reference, but with simply passes it as the argument
        // Here instead of Builder we can  use 'apply' it give the object reference ie Builder here
        /**********************************************************************************************************/

        fun setRam(ram: String) = apply {
            this.ram = ram
            return this
        }

        fun setBattery(battery: String): Builder {
            this.battery = battery
            return this
        }

        fun setScreenSize(screenSize: String): Builder {
            this.screenSize = screenSize
            return this
        }


        fun create(): Laptop {
            return Laptop(this)
        }
    }
}
