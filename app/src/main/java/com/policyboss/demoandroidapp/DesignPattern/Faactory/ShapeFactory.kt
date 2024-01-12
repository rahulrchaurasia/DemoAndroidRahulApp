package com.policyboss.demoandroidapp.DesignPattern.Faactory

import android.util.Log
import com.policyboss.demoandroidapp.Constant


interface Shape {
    fun draw()
}
class Circle : Shape {
    override fun draw() {
        Log.d(Constant.TAG,"Drawing Circle")
    }
}


class Square : Shape {
    override fun draw() {
        Log.d(Constant.TAG,"Drawing Square")
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