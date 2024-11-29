package com.example.lab4

data class ClickerState(
    val cookieCount: Long = 0,
    val cookiesPerSecond: Double = 0.0,
    val cookiesPerMin: Double = 0.0,
    val totalTime: Int = 0,
    val buildingList: List<Building> = emptyList()
)
