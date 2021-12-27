package plot

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.geom.geomSmooth
import jetbrains.letsPlot.ggplot
import simulation.SimulationStats

object SimulationStatsPlotter {
    fun plot(stats: List<SimulationStats>) {
        val taskIntervalCoefficientOfVariation = stats.map { it.taskIntervalCoefficientOfVariation }
        val taskSizeCoefficientOfVariation = stats.map { it.taskSizeCoefficientOfVariation }
        val averageResponseTime = stats.map { it.averageResponseTime }
        val averageProcessingTime = stats.map { it.averageProcessingTime }
        val averageDelayTime = stats.map { it.averageDelayTime }
        val averageTaskInterval = stats.map { it.averageTaskInterval }
        val averageLoad = stats.map { it.averageLoad }

        savePlot(
            filename = "p1.png",
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average response time" to averageResponseTime
            )
        )

        savePlot(
            filename = "p2.png",
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average processing time" to averageProcessingTime
            )
        )

        savePlot(
            filename = "p3.png",
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average delay time" to averageDelayTime
            )
        )

        savePlot(
            filename = "p4.png",
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average load" to averageLoad
            )
        )

        savePlot(
            filename = "p5.png",
            data = listOf(
                "Task interval coefficient of variation" to taskIntervalCoefficientOfVariation,
                "Average task interval" to averageTaskInterval
            )
        )

        savePlot(
            filename = "p6.png",
            data = listOf(
                "Average load" to averageLoad,
                "Average response time" to averageResponseTime
            )
        )

        savePlot(
            filename = "p7.png",
            data = listOf(
                "Average load" to averageLoad,
                "Average delay time" to averageDelayTime
            )
        )

        savePlot(
            filename = "p8.png",
            data = listOf(
                "Average load" to averageLoad,
                "Average processing time" to averageProcessingTime
            )
        )

//    savePlot(
//        filename = "p9.png",
//        data = listOf(
//            "Task interval CV" to taskIntervalCoefficientOfVariation,
//            "Task size CV" to taskSizeCoefficientOfVariation
//        )
//    )
    }

    private fun savePlot(data: List<Pair<String, *>>, filename: String) {
        ggsave(simulationPlot(data), filename)
    }

    private fun simulationPlot(data: List<Pair<String, *>>) =
        ggplot(data.toMap()) { x = data[0].first; y = data[1].first } + geomPoint() + geomSmooth()
}