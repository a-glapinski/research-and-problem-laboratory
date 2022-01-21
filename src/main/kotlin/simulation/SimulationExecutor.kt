package simulation

import algorithm.SchedulingAlgorithm
import task.Task
import task.TaskDefinition

class SimulationExecutor<T : Task>(
    val algorithm: SchedulingAlgorithm<T>,
    val availableNodesNumber: Int,
    val C: Double = 1.05
) {
    fun execute(tasks: List<TaskDefinition>): SimulationResult {
        val processedTasks = algorithm.run(tasks, availableNodesNumber, C)
        return simulation.SimulationResult(
            processedTasks = processedTasks,
            stats = algorithm.statsCalculator.calculate(processedTasks, availableNodesNumber)
        )
    }
}