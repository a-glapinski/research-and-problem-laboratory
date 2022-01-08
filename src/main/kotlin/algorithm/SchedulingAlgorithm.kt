package algorithm

import round
import simulation.SimulationStatsCalculator
import task.Task
import task.TaskDefinition

interface SchedulingAlgorithm<T : Task> {
    val statsCalculator: SimulationStatsCalculator<T>
    fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double = 1.05): List<T>

    fun T.calculateProcessingTime(allocatedNodesNumber: Int, C: Double, processingDone: Double = 0.0): Double {
        val seqentialFraction = taskSize.sequentialTime/taskSize.totalTime
        val parallelFraction = taskSize.parallelTime/taskSize.totalTime
        val sequentialProcessingTime = (taskSize.sequentialTime - seqentialFraction * processingDone) + C * (allocatedNodesNumber - 1)
        val parallelProcessingTime = (taskSize.parallelTime - parallelFraction * processingDone) / allocatedNodesNumber
        return (sequentialProcessingTime + parallelProcessingTime).round(2)
    }
}