package algorithm

import round
import simulation.SimulationStatsCalculator
import task.Task
import task.TaskDefinition

interface SchedulingAlgorithm<T : Task> {
    val statsCalculator: SimulationStatsCalculator<T>
    fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double = 1.05): List<T>

    fun T.calculateProcessingTime(allocatedNodesNumber: Int, C: Double, remainingRatio: Double = 1.0): Double {
        val sequentialProcessingTime = (taskSize.sequentialTime * remainingRatio) + C * (allocatedNodesNumber - 1)
        val parallelProcessingTime = (taskSize.parallelTime * remainingRatio) / allocatedNodesNumber
        return (sequentialProcessingTime + parallelProcessingTime).round(2)
    }
}