import algorithm.GetMAX
import algorithm.ParallelIfPossible
import algorithm.SchedulingAlgorithm
import plot.SimulationStatsPlotter
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.Task
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters
import task.TaskDataGeneratorOutput
import kotlin.math.pow

fun main() {
    val generatedData = List(30) {
        generateData(averageTaskIntervalDelta = it.toDouble(), averageTaskSizeDelta = 0.0, loadMultiplier = 1)
    }

    val resultsGetMax = generatedData.map { run(GetMAX, it) }
    val resultsParallelIfPossible = generatedData.map { run(ParallelIfPossible, it) }

    val statsGetMax = resultsGetMax.map { it.second.stats }
    val statsParallelIfPossible = resultsParallelIfPossible.map { it.second.stats }

    SimulationStatsPlotter.plot(statsGetMax, statsParallelIfPossible)
}

private fun generateData(
    averageTaskIntervalDelta: Double,
    averageTaskSizeDelta: Double,
    loadMultiplier: Int
): TaskDataGeneratorOutput {
    val taskDataGenerator = TaskDataGenerator(
        randomSeed = 23,
        totalTaskCount = 5000,
        phaseCount = 4,
        bigTaskMaxNumberOfWantedNodesRange = 5..10,
        smallTaskMaxNumberOfWantedNodesRange = 1..5,
        smallTaskAverageSize = 50.0,
        bigTaskAverageSize = 120.0,
        bigLoadAverageTaskInterval = 50.0 / 2.0.pow(loadMultiplier - 1),
        smallLoadAverageTaskInterval = 150.0 / 2.0.pow(loadMultiplier - 1),
        averageTaskIntervalDelta = averageTaskIntervalDelta,
        averageTaskSizeDelta = averageTaskSizeDelta
    )
    return taskDataGenerator.generate()
}

private fun <T : Task> run(
    schedulingAlgorithm: SchedulingAlgorithm<T>,
    taskDataGeneratorOutput: TaskDataGeneratorOutput
): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 15
    val simulationExecutor = SimulationExecutor(schedulingAlgorithm, availableNodesNumber, C = 1.05)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}