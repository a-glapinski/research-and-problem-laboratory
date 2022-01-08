package plot

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
import simulation.SimulationStats

object SimulationStatsPlotter {
    fun plot(stats: List<SimulationStats>, title: String) {
        val taskIntervalCoefficientOfVariation = stats.map { it.taskIntervalCoefficientOfVariation }
        val taskSizeCoefficientOfVariation = stats.map { it.taskSizeCoefficientOfVariation }
        val averageLoad = stats.map { it.averageLoad }
        val averageProcessingTime = stats.map { it.averageProcessingTime }
        val averageResponseTime = stats.map { it.averageResponseTime }
        val averageDelayTime = stats.map { it.averageDelayTime }
        val averageTaskInterval = stats.map { it.averageTaskInterval }
        val averageTaskSize = stats.map { it.averageTaskSize }

        var counter = 0

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average processing time" to averageProcessingTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average response time" to averageResponseTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average delay time" to averageDelayTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average task interval" to averageTaskInterval
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average load" to averageLoad
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average processing time" to averageProcessingTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average response time" to averageResponseTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average delay time" to averageDelayTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average task size" to averageTaskSize
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Task size coefficient of variation" to taskSizeCoefficientOfVariation,
                "Average load" to averageLoad
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average response time" to averageResponseTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average delay time" to averageDelayTime
            )
        )

        savePlot(
            filename = "p${++counter}.png",
            title = title,
            data = listOf(
                "Average load" to averageLoad,
                "Average processing time" to averageProcessingTime
            )
        )
    }

    private fun savePlot(data: List<Pair<String, *>>, title: String, filename: String) {
        ggsave(simulationPlot(data, title), filename)
    }

    private fun simulationPlot(data: List<Pair<String, *>>, title: String) =
        ggplot(data.toMap()) { x = data[0].first; y = data[1].first } + ggtitle(title) + geomPoint() + geomLine()
}