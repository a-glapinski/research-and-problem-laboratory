import algorithm.GetMAX
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters

fun main() {
    val results = List(100) {
        run(averageTaskIntervalDelta = it.toDouble())
    }.toMap()
    println(results)
}

fun run(averageTaskIntervalDelta: Double): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 5
    val taskDataGenerator = TaskDataGenerator(
        taskCount = 100,
        taskMaxNumberOfWantedNodes = 30,
        smallTaskAverageProcessingTime = 50.0,
        bigTaskAverageProcessingTime = 250.0,
        bigLoadAverageTaskInterval = 125.0,
        smallLoadAverageTaskInterval = 250.0,
        averageTaskIntervalDelta = averageTaskIntervalDelta,
        randomSeed = 23
    )
    val taskDataGeneratorOutput = taskDataGenerator.generate()

    val simulationExecutor = SimulationExecutor(GetMAX, availableNodesNumber)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}