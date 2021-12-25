package simulation

import task.Task

interface SimulationStatsCalculator<T : Task> {
    fun calculate(processedTasks: List<T>, availableNodesNumber: Int): SimulationStats
}