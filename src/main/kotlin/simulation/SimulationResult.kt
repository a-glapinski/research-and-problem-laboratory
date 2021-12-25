package simulation

import task.Task

data class SimulationResult(
    val processedTasks: List<Task>,
    val stats: SimulationStats
)
