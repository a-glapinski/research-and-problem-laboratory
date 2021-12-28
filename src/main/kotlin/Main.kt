import algorithm.GetMAX
import algorithm.SchedulingAlgorithm
import plot.SimulationStatsPlotter
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.Task
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters

fun main() {
    val results = List(20) {
        run(GetMAX, averageTaskIntervalDelta = it.toDouble(), averageTaskSizeDelta = 0.0)
    }
//    File("results.json").writeText(results.toJson())

    val stats = results.map { it.second.stats }
    SimulationStatsPlotter.plot(stats)
}

fun <T : Task> run(
    schedulingAlgorithm: SchedulingAlgorithm<T>,
    averageTaskIntervalDelta: Double,
    averageTaskSizeDelta: Double
): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 30
    val taskDataGenerator = TaskDataGenerator(
        randomSeed = 23,
        totalTaskCount = 100,
        phaseCount = 4,
        taskMaxNumberOfWantedNodes = 25,
        smallTaskAverageSize = 50.0,
        bigTaskAverageSize = 120.0,
        bigLoadAverageTaskInterval = 25.0,
        smallLoadAverageTaskInterval = 70.0,
        averageTaskIntervalDelta = averageTaskIntervalDelta,
        averageTaskSizeDelta = averageTaskSizeDelta
    )
    val taskDataGeneratorOutput = taskDataGenerator.generate()
    val simulationExecutor = SimulationExecutor(schedulingAlgorithm, availableNodesNumber)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}