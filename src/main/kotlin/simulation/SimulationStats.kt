package simulation

data class SimulationStats(
    val averageLoad: Double,
    val averageProcessingTime: Double,
    val taskIntervalCoefficientOfVariation: Double
)