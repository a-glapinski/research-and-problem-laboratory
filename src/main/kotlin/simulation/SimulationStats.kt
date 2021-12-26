package simulation

data class SimulationStats(
    val averageResponseTime: Double,
    val averageProcessingTime: Double,
    val averageDelayTime: Double,
    val averageLoad: Double,
    val averageTaskInterval: Double,
    val taskIntervalCoefficientOfVariation: Double,
    val taskSizeCoefficientOfVariation: Double
)