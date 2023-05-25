package com.policyboss.demoandroidapp.DesignPattern.Faactory



interface Shape {
    fun draw()
}
class Circle : Shape {
    override fun draw() {
        println("Drawing Circle")
    }
}


class Square : Shape {
    override fun draw() {
        println("Drawing Square")
    }
}


class ShapeFactory {

    fun getShape(shapeType: String): Shape? {
        return when (shapeType) {
            "circle" -> Circle()
            "square" -> Square()
            else -> null
        }
    }
}