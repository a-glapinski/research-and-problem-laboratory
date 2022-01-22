package plot

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
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

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Task interval coefficient of variation",
                x = taskIntervalCoefficientOfVariation,
                yLabel = "Average processing time",
                firstAlgorithmY = averageProcessingTimeGetMax to "GetMAX",
                secondAlgorithmY = averageProcessingTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Task interval coefficient of variation",
                x = taskIntervalCoefficientOfVariation,
                yLabel = "Average response time",
                firstAlgorithmY = averageResponseTimeGetMax to "GetMAX",
                secondAlgorithmY = averageResponseTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Task interval coefficient of variation",
                x = taskIntervalCoefficientOfVariation,
                yLabel = "Average delay time",
                firstAlgorithmY = averageDelayTimeGetMax to "GetMAX",
                secondAlgorithmY = averageDelayTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Task size coefficient of variation",
                x = taskSizeCoefficientOfVariation,
                yLabel = "Average processing time",
                firstAlgorithmY = averageProcessingTimeGetMax to "GetMAX",
                secondAlgorithmY = averageProcessingTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Task size coefficient of variation",
                x = taskSizeCoefficientOfVariation,
                yLabel = "Average response time",
                firstAlgorithmY = averageResponseTimeGetMax to "GetMAX",
                secondAlgorithmY = averageResponseTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Task size coefficient of variation",
                x = taskSizeCoefficientOfVariation,
                yLabel = "Average delay time",
                firstAlgorithmY = averageDelayTimeGetMax to "GetMAX",
                secondAlgorithmY = averageDelayTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Average load",
                x = averageLoad,
                yLabel = "Average processing time",
                firstAlgorithmY = averageProcessingTimeGetMax to "GetMAX",
                secondAlgorithmY = averageProcessingTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Average load",
                x = averageLoad,
                yLabel = "Average response time",
                firstAlgorithmY = averageResponseTimeGetMax to "GetMAX",
                secondAlgorithmY = averageResponseTimeParallelIfPossible to "Paralleled If Possible"
            )
        )

        saveComparisonPlot(
            filename = "p${++counter}.png",
            title = title,
            data = prepareComparisonPlotData(
                xLabel = "Average load",
                x = averageLoad,
                yLabel = "Average delay time",
                firstAlgorithmY = averageDelayTimeGetMax to "GetMAX",
                secondAlgorithmY = averageDelayTimeParallelIfPossible to "Paralleled If Possible"
            )
        )
    }

    private fun prepareComparisonPlotData(xLabel: String, x: List<Double>,
                                          yLabel: String,
                                          firstAlgorithmY: Pair<List<Double>, String>,
                                          secondAlgorithmY: Pair<List<Double>, String>): Map<String, *> {
        val firstAlgorithmStats = firstAlgorithmY.first.mapIndexed { i, stat ->
            Triple(x[i], stat, firstAlgorithmY.second)
        }
        val secondAlgorithmStats = secondAlgorithmY.first.mapIndexed { i, stat ->
            Triple(x[i], stat, secondAlgorithmY.second)
        }
        val stats = firstAlgorithmStats + secondAlgorithmStats
        return mapOf(
            xLabel to stats.map { it.first },
            yLabel to stats.map { it.second },
            "Algorithm" to stats.map { it.third }
        )
    }

    private fun saveComparisonPlot(data: Map<String, *>, title: String, filename: String) {
        ggsave(simulationComparisonPlot(data, title), filename)
    }

    private fun simulationComparisonPlot(data: Map<String, *>, title: String) =
        ggplot(data) {
            x = data.keys.elementAt(0)
            y = data.keys.elementAt(1)
            color = data.keys.elementAt(2)
        } + geomLine() + geomPoint() + ggtitle(title)
}