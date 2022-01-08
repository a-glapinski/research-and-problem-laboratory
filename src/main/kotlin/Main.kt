import algorithm.GetMAX
import algorithm.ParallelIfPossible
import algorithm.SchedulingAlgorithm
import plot.SimulationStatsPlotter
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.Task
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters
import kotlin.math.pow

fun main() {
    val algorithm = GetMAX
    val results = List(35) {
        run(algorithm, averageTaskIntervalDelta = 0.0, averageTaskSizeDelta = it.toDouble(), loadMultiplier = 1)
    }

    val stats = results.map { it.second.stats }
    SimulationStatsPlotter.plot(stats, title = algorithm::class.simpleName!!)
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