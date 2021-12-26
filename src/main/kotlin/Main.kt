import algorithm.GetMAX
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters

fun main() {
    val results = List(1000) {
        run(averageTaskIntervalDelta = it.toDouble() / 50)
    }.toMap()
    println(results)
}

fun run(averageTaskIntervalDelta: Double): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 25
    val taskDataGenerator = TaskDataGenerator(
        taskCount = 100,
        taskMaxNumberOfWantedNodes = 25,
        smallTaskAverageProcessingTime = 50.0,
        bigTaskAverageProcessingTime = 100.0,
        bigLoadAverageTaskInterval = 30.0,
        smallLoadAverageTaskInterval = 70.0,
        averageTaskIntervalDelta = averageTaskIntervalDelta,
        randomSeed = 23
    )
    val taskDataGeneratorOutput = taskDataGenerator.generate()

    val simulationExecutor = SimulationExecutor(GetMAX, availableNodesNumber)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}