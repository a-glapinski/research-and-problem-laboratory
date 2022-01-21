package plot

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
import simulation.SimulationStats

object SimulationStatsPlotter {
    fun plot(stats: List<List<SimulationStats>>, title: String) {
        val taskIntervalCoefficientOfVariation = stats.map { it[0].taskIntervalCoefficientOfVariation }
        val taskSizeCoefficientOfVariation = stats.map { it[0].taskSizeCoefficientOfVariation }
        val averageLoad = stats.map { it[0].averageLoad }
        val averageTaskInterval = stats.map { it[0].averageTaskInterval }
        val averageTaskSize = stats.map { it[0].averageTaskSize }

        val averageProcessingTimeGetMax = stats.map { it[0].averageProcessingTime }
        val averageResponseTimeGetMax = stats.map { it[0].averageResponseTime }
        val averageDelayTimeGetMax = stats.map { it[0].averageDelayTime }

        val averageProcessingTimeParallelIfPossible = stats.map { it[1].averageProcessingTime }
        val averageResponseTimeParallelIfPossible = stats.map { it[1].averageResponseTime }
        val averageDelayTimeParallelIfPossible = stats.map { it[1].averageDelayTime }


        var counter = 0

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average processing time getMax" to averageProcessingTimeGetMax,
                "Average processing time parallelIfPossible" to averageProcessingTimeParallelIfPossible,
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average response time getMax" to averageResponseTimeGetMax,
                "Average response time parallelIfPossible" to averageResponseTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average delay time getMax" to averageDelayTimeGetMax,
                "Average delay time parallelIfPossible" to averageDelayTimeParallelIfPossible
            )
        )


        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average processing time getMax" to averageProcessingTimeGetMax,
                "Average processing time parallelIfPossible" to averageProcessingTimeParallelIfPossible,
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average response time getMax" to averageResponseTimeGetMax,
                "Average response time parallelIfPossible" to averageResponseTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average delay time getMax" to averageDelayTimeGetMax,
                "Average delay time parallelIfPossible" to averageDelayTimeParallelIfPossible
            )
        )



        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average response time getMax" to averageResponseTimeGetMax,
                "Average response time parallelIfPossible" to averageResponseTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average delay time getMax" to averageDelayTimeGetMax,
                "Average delay time parallelIfPossible" to averageDelayTimeParallelIfPossible
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average processing time getMax" to averageProcessingTimeGetMax,
                "Average processing time parallelIfPossible" to averageProcessingTimeParallelIfPossible,            )
        )
    }

    private fun savePlot(data: List<Pair<String, *>>, title: String, filename: String) {
        ggsave(simulationPlot(data, title), filename)
    }

    private fun simulationPlot(data: List<Pair<String, *>>, title: String) =
        ggplot(data.toMap()) { x = data[0].first} + ggtitle(title) + geomLine{y = data[1].first} + geomPoint{y = data[1].first}
}