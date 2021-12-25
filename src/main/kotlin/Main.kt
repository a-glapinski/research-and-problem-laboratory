import algorithm.GetMAX
import algorithm.GetMAXTask
import simulation.SimulationExecutor
import task.TaskDataGenerator

fun main() {
    val taskDataGenerator = TaskDataGenerator(
        taskCount = 50,
        taskMaxNumberOfWantedNodes = 10,
        bigTaskProbability = Probability(0.5),
        smallTaskAverageProcessingTime = 5.0,
        bigTaskAverageProcessingTime = 25.0,
        bigLoadAverageTaskInterval = 2.0,
        smallLoadAverageTaskInterval = 5.0,
        // small load or big load?
        bigLoadAverageTaskIntervalDelta = 2.0,
        bigLoadProbability = Probability(0.5),
        randomSeed = 23
    )

    val tasks = taskDataGenerator.generate()
    // tasks.forEach(::println)
    val availableNodesNumber = 5
    val simulationExecutor = SimulationExecutor(availableNodesNumber)
    val result = simulationExecutor.execute(GetMAX, tasks)
    result.processedTasks.forEach(::println)

    val getMAXTasks = result.processedTasks.map { it as GetMAXTask }
    val load = calculateGetMAXLoad(getMAXTasks, availableNodesNumber)
    println(load)
}

fun calculateGetMAXLoad(processedTasks: List<GetMAXTask>, availableNodesNumber: Int): Double {
    val start = processedTasks.minOf { it.appearedAt }
    val end = processedTasks.maxOf { it.processingEndedAt!! }
    val simulationDuration = end - start

    val processingTime = processedTasks.sumOf { it.taskSize.parallelTime }
    return processingTime / (simulationDuration * availableNodesNumber)
}