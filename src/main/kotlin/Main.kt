import algorithm.GetMAX
import algorithm.SchedulingAlgorithm
import plot.SimulationStatsPlotter
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.Task
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters
import kotlin.math.pow

fun main() {
    val results = List(6) {
        run(GetMAX, averageTaskIntervalDelta = 0.0, averageTaskSizeDelta = 0.0, loadMultiplier = it + 1)
    }

    val stats = results.map { it.second.stats }
    SimulationStatsPlotter.plot(stats)
}

fun <T : Task> run(
    schedulingAlgorithm: SchedulingAlgorithm<T>,
    averageTaskIntervalDelta: Double,
    averageTaskSizeDelta: Double,
    loadMultiplier: Int
): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 30
    val taskDataGenerator = TaskDataGenerator(
        randomSeed = 23,
        totalTaskCount = 5000,
        phaseCount = 4,
        taskMaxNumberOfWantedNodes = 25,
        smallTaskAverageSize = 50.0,
        bigTaskAverageSize = 120.0,
        bigLoadAverageTaskInterval = 50.0 / 2.0.pow(loadMultiplier - 1),
        smallLoadAverageTaskInterval = 150.0 / 2.0.pow(loadMultiplier - 1),
        averageTaskIntervalDelta = averageTaskIntervalDelta,
        averageTaskSizeDelta = averageTaskSizeDelta
    )
    val taskDataGeneratorOutput = taskDataGenerator.generate()
    val simulationExecutor = SimulationExecutor(schedulingAlgorithm, availableNodesNumber)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}