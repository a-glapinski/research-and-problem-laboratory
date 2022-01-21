package plot

import jetbrains.letsPlot.Pos.fill
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.label.ylab
import jetbrains.letsPlot.scale.scaleColorManual
import simulation.SimulationStats

object SimulationStatsPlotter {
    fun plot(statsGetMax: List<SimulationStats>, statsParallelIfPossible: List<SimulationStats>, title: String) {
        val taskIntervalCoefficientOfVariation = statsGetMax.map { it.taskIntervalCoefficientOfVariation }
        val taskSizeCoefficientOfVariation = statsGetMax.map { it.taskSizeCoefficientOfVariation }
        val averageLoad = statsGetMax.map { it.averageLoad }
        val averageTaskInterval = statsGetMax.map { it.averageTaskInterval }
        val averageTaskSize = statsGetMax.map { it.averageTaskSize }

        val averageProcessingTimeGetMax = statsGetMax.map { it.averageProcessingTime }
        val averageResponseTimeGetMax = statsGetMax.map { it.averageResponseTime }
        val averageDelayTimeGetMax = statsGetMax.map { it.averageDelayTime }

        val averageProcessingTimeParallelIfPossible = statsParallelIfPossible.map { it.averageProcessingTime }
        val averageResponseTimeParallelIfPossible = statsParallelIfPossible.map { it.averageResponseTime }
        val averageDelayTimeParallelIfPossible = statsParallelIfPossible.map { it.averageDelayTime }


        var counter = 0

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average processing time" to averageProcessingTimeGetMax,
                "Average processing time parallelIfPossible" to averageProcessingTimeParallelIfPossible,
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average response time" to averageResponseTimeGetMax,
                "Average response time parallelIfPossible" to averageResponseTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average delay time" to averageDelayTimeGetMax,
                "Average delay time parallelIfPossible" to averageDelayTimeParallelIfPossible
            )
        )


        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average processing time" to averageProcessingTimeGetMax,
                "Average processing time parallelIfPossible" to averageProcessingTimeParallelIfPossible,
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average response time" to averageResponseTimeGetMax,
                "Average response time parallelIfPossible" to averageResponseTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average delay time" to averageDelayTimeGetMax,
                "Average delay time parallelIfPossible" to averageDelayTimeParallelIfPossible
            )
        )



        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average response time" to averageResponseTimeGetMax,
                "Average response time parallelIfPossible" to averageResponseTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average delay time" to averageDelayTimeGetMax,
                "Average delay time parallelIfPossible" to averageDelayTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average processing time getMax" to averageProcessingTimeGetMax,
                "Average processing time parallelIfPossible" to averageProcessingTimeParallelIfPossible,
            )
        )
    }

    private fun savePlot(data: List<Pair<String, *>>, title: String, filename: String) {
        ggsave(simulationPlot(data, title), filename)
    }

    private fun simulationPlot(data: List<Pair<String, *>>, title: String) =
        ggplot(data.toMap()) { x = data[0].first} + ggtitle(title) +
                geomLine(color = "red"){y = data[1].first} + geomPoint(color = "red"){y = data[1].first} +
                geomLine(color = "blue"){y = data[2].first} + geomPoint(color = "blue"){y = data[2].first} +
                ylab(data[1].first)
}