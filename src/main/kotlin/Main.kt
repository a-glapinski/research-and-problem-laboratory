import algorithm.GetMAX
import simulation.SimulationExecutor
import simulation.SimulationResult
import task.TaskDataGenerator

fun main() {
    val results = List(100) {
        run(averageTaskIntervalDelta = it.toDouble())
    }
    println(results)
}

fun run(averageTaskIntervalDelta: Double): SimulationResult {
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
    val tasks = taskDataGenerator.generate()

    val simulationExecutor = SimulationExecutor(GetMAX, availableNodesNumber)
    return simulationExecutor.execute(tasks)
}