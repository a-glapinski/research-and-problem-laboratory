package algorithm

import coefficientOfVariation
import round
import simulation.SimulationStats
import simulation.SimulationStatsCalculator

object GetMAXStatsCalculator : SimulationStatsCalculator<GetMAXTask> {
    override fun calculate(processedTasks: List<GetMAXTask>, availableNodesNumber: Int) =
        SimulationStats(
            averageLoad = calculateLoad(processedTasks, availableNodesNumber).round(2),
            averageProcessingTime = calculateAverageProcessingTime(processedTasks).round(2),
            taskIntervalCoefficientOfVariation = processedTasks.map { it.nextTaskInterval }.coefficientOfVariation()
                .round(2)
        )

    private fun calculateLoad(processedTasks: List<GetMAXTask>, availableNodesNumber: Int): Double {
        val start = processedTasks.minOf { it.appearedAt }
        val end = processedTasks.maxOf { it.processingEndedAt!! }
        val simulationDuration = end - start

        val processingTime = processedTasks.sumOf { it.taskSize.parallelTime }
        return processingTime / (simulationDuration * availableNodesNumber)
    }

    private fun calculateAverageProcessingTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.processingStartedAt!! }.average()
}
