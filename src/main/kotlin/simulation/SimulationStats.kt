package simulation

data class SimulationStats(
    val averageProcessingTime: Double,
    val averageResponseTime: Double,
    val averageDelayTime: Double,
    val averageLoad: Double,
    val averageTaskInterval: Double,
    val averageTaskSize: Double,
    val taskIntervalCoefficientOfVariation: Double,
    val taskSizeCoefficientOfVariation: Double
)