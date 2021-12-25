package simulation

import algorithm.SchedulingAlgorithm
import task.TaskDefinition

class SimulationExecutor(
    val availableNodesNumber: Int,
    val C: Double = 1.05
) {
    fun execute(algorithm: SchedulingAlgorithm, tasks: List<TaskDefinition>): SimulationResult =
        algorithm.run(tasks, availableNodesNumber, C)
}