import algorithm.GetMAX
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.geom.geomSmooth
import jetbrains.letsPlot.ggplot
import simulation.SimulationExecutor
import simulation.SimulationResult
import simulation.SimulationStats
import task.TaskDataGenerator
import task.TaskDataGeneratorInputParameters

fun main() {
    val results = List(20) {
        run(averageTaskIntervalDelta = it.toDouble())
    }.toMap()
//    File("results.json").writeText(results.toJson())

    val stats = results.values.map { it.stats }
    plot(stats)
}

fun run(averageTaskIntervalDelta: Double): Pair<TaskDataGeneratorInputParameters, SimulationResult> {
    val availableNodesNumber = 30
    val taskDataGenerator = TaskDataGenerator(
        taskCount = 100,
        taskMaxNumberOfWantedNodes = 25,
        smallTaskAverageProcessingTime = 50.0,
        bigTaskAverageProcessingTime = 120.0,
        bigLoadAverageTaskInterval = 25.0,
        smallLoadAverageTaskInterval = 70.0,
        averageTaskIntervalDelta = averageTaskIntervalDelta,
        randomSeed = 23
    )
    val taskDataGeneratorOutput = taskDataGenerator.generate()

    val simulationExecutor = SimulationExecutor(GetMAX, availableNodesNumber)
    return taskDataGeneratorOutput.inputParameters to simulationExecutor.execute(taskDataGeneratorOutput.tasks)
}

fun plot(stats: List<SimulationStats>) {
    val taskIntervalCoefficientOfVariation = stats.map { it.taskIntervalCoefficientOfVariation }
    val taskSizeCoefficientOfVariation = stats.map { it.taskSizeCoefficientOfVariation }
    val averageResponseTime = stats.map { it.averageResponseTime }
    val averageProcessingTime = stats.map { it.averageProcessingTime }
    val averageDelayTime = stats.map { it.averageDelayTime }
    val averageTaskInterval = stats.map { it.averageTaskInterval }
    val averageLoad = stats.map { it.averageLoad }

    savePlot(
        "p1.png",
        listOf(
            "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
            "Average response time" to averageResponseTime
        )
    )

    savePlot(
        "p2.png",
        listOf(
            "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
            "Average processing time" to averageProcessingTime
        )
    )

    savePlot(
        "p3.png",
        listOf(
            "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
            "Average delay time" to averageDelayTime
        )
    )

    savePlot(
        "p4.png",
        listOf(
            "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
            "Average load" to averageLoad
        )
    )

    savePlot(
        "p5.png",
        listOf(
            "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
            "Average task interval" to averageTaskInterval
        )
    )

    savePlot(
        "p6.png",
        listOf(
            "Average load" to averageLoad,
            "Average response time" to averageResponseTime
        )
    )

    savePlot(
        "p7.png",
        listOf(
            "Average load" to averageLoad,
            "Average delay time" to averageDelayTime
        )
    )

    savePlot(
        "p8.png",
        listOf(
            "Average load" to averageLoad,
            "Average processing time" to averageProcessingTime
        )
    )

//    savePlot(
//        "p9.png",
//        listOf(
//            "Task interval CV" to taskIntervalCoefficientOfVariation,
//            "Task size CV" to taskSizeCoefficientOfVariation
//        )
//    )
}

fun savePlot(filename: String, data: List<Pair<String, *>>) {
    val plot = ggplot(data.toMap()) { x = data[0].first; y = data[1].first } + geomPoint() + geomSmooth()
    ggsave(plot, filename)
}