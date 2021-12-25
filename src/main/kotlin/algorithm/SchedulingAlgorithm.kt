package algorithm

import simulation.SimulationStatsCalculator
import task.Task
import task.TaskDefinition

interface SchedulingAlgorithm<T : Task> {
    fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double = 1.05): List<T>
    val statsCalculator: SimulationStatsCalculator<T>
}