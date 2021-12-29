package algorithm

import coefficientOfVariation
import round
import simulation.SimulationStats
import simulation.SimulationStatsCalculator

object GetMAXStatsCalculator : SimulationStatsCalculator<GetMAXTask> {
    override fun calculate(processedTasks: List<GetMAXTask>, availableNodesNumber: Int) =
        SimulationStats(
            averageResponseTime = calculateAverageResponseTime(processedTasks).round(5),
            averageProcessingTime = calculateAverageProcessingTime(processedTasks).round(5),
            averageDelayTime = calculateAverageDelayTime(processedTasks).round(5),
            averageLoad = calculateAverageLoad(processedTasks, availableNodesNumber).round(5),
            averageTaskInterval = calculateAverageTaskInterval(processedTasks).round(5),
            taskIntervalCoefficientOfVariation = calculateTaskIntervalCoefficientOfVariation(processedTasks).round(5),
            taskSizeCoefficientOfVariation = calculateTaskSizeCoefficientOfVariation(processedTasks).round(5)
        )

    private fun calculateAverageResponseTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.appearedAt }.average()

    private fun calculateAverageDelayTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingStartedAt!! - it.appearedAt }.average()

    private fun calculateAverageLoad(processedTasks: List<GetMAXTask>, availableNodesNumber: Int): Double {
        val lambda = processedTasks.count() / processedTasks.maxOf { it.appearedAt }
        val mi = 1 / processedTasks.map { it.taskSize.totalTime }.average()
        return lambda / (mi * availableNodesNumber)
    }

    private fun calculateAverageTaskInterval(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.nextTaskInterval }.average()

    private fun calculateAverageProcessingTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.processingStartedAt!! }.average()

    private fun calculateTaskIntervalCoefficientOfVariation(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.nextTaskInterval }.coefficientOfVariation()

    private fun calculateTaskSizeCoefficientOfVariation(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.taskSize.totalTime }.coefficientOfVariation()
}
