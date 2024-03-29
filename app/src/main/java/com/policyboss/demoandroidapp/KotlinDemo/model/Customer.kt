package com.policyboss.demoandroidapp.KotlinDemo.model

data class Customer(val name: String, val orders: List<Order>)
data class Order(val amount: Int)