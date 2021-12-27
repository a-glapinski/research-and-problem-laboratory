import algorithm.GetMAX
import plot.SimulationStatsPlotter
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters

fun main() {
    val results = List(20) {
        run(averageTaskIntervalDelta = it.toDouble())
    }.toMap()
//    File("results.json").writeText(results.toJson())

    val stats = results.values.map { it.stats }
    SimulationStatsPlotter.plot(stats)
}

fun run(averageTaskIntervalDelta: Double): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 30
    val taskDataGenerator = TaskDataGenerator(
        randomSeed = 23,
        taskCount = 100,
        taskMaxNumberOfWantedNodes = 25,
        smallTaskAverageProcessingTime = 50.0,
        bigTaskAverageProcessingTime = 120.0,
        bigLoadAverageTaskInterval = 25.0,
        smallLoadAverageTaskInterval = 70.0,
        averageTaskIntervalDelta = averageTaskIntervalDelta
    )
    val taskDataGeneratorOutput = taskDataGenerator.generate()

    val simulationExecutor = SimulationExecutor(GetMAX, availableNodesNumber)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}