package com.example.a2

sealed interface Transport

object Scooter : Transport

enum class FrameMaterial {
    ALUMINUM, STEEL, CARBON
}

data class BicycleWheel(val diameter: Int)

data class Bicycle(
    val brand: String,
    val frontWheel: BicycleWheel,
    val backWheel: BicycleWheel,
    val frameMaterial: FrameMaterial
) : Transport

enum class FuelType {
    DIESEL, FUEL_92, FUEL_95, FUEL_98, FUEL_100
}

data class InternalCombustionEngine(val volume: Double, val fuelType: FuelType)

enum class WheelDiskType {
    CAST, FORGED, STAMPED
}

enum class AutopilotType {
    YANDEX, TESLA
}

data class CarWheel(
    val diameter: Int,
    val brand: String,
    val diskType: WheelDiskType
)

sealed class Car(
    val wheels: List<CarWheel>
) : Transport {
    init {
        require(wheels.size == 4) { "Автомобиль должен иметь 4 колеса" }
    }
}

class CombustionCar(
    val engine: InternalCombustionEngine,
    wheels: List<CarWheel>,
    val steeringWheel: Boolean
) : Car(wheels)

class ElectricCar(
    val electricMotor: String,
    wheels: List<CarWheel>,
    val autopilot: AutopilotType
) : Car(wheels)


fun main() {};