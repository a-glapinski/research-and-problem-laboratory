package algorithm

import coefficientOfVariation
import round
import simulation.SimulationStats
import simulation.SimulationStatsCalculator

object GetMAXStatsCalculator : SimulationStatsCalculator<GetMAXTask> {
    override fun calculate(processedTasks: List<GetMAXTask>, availableNodesNumber: Int) =
        SimulationStats(
            averageResponseTime = calculateAverageResponseTime(processedTasks).round(2),
            averageProcessingTime = calculateAverageProcessingTime(processedTasks).round(2),
            averageDelayTime = calculateAverageDelayTime(processedTasks).round(2),
            averageLoad = calculateLoad(processedTasks, availableNodesNumber).round(2),
            taskIntervalCoefficientOfVariation = calculateTaskIntervalCoefficientOfVariation(processedTasks).round(2),
            taskSizeCoefficientOfVariation = calculateTaskSizeCoefficientOfVariation(processedTasks).round(2)
        )

    private fun calculateAverageResponseTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.appearedAt }.average()

    private fun calculateAverageDelayTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingStartedAt!! - it.appearedAt }.average()

    private fun calculateLoad(processedTasks: List<GetMAXTask>, availableNodesNumber: Int): Double {
        val start = processedTasks.minOf { it.appearedAt }
        val end = processedTasks.maxOf { it.processingEndedAt!! }
        val simulationDuration = end - start

        val processingTime = processedTasks.sumOf { it.taskSize.parallelTime }
        return processingTime / (simulationDuration * availableNodesNumber)
    }

    private fun calculateAverageProcessingTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.processingStartedAt!! }.average()

    private fun calculateTaskIntervalCoefficientOfVariation(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.nextTaskInterval }.coefficientOfVariation()

    private fun calculateTaskSizeCoefficientOfVariation(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.taskSize.totalTime }.coefficientOfVariation()
}
