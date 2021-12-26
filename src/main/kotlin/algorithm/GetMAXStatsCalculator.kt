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
        val start = processedTasks.minOf { it.appearedAt }
        val end = processedTasks.maxOf { it.processingEndedAt!! }
        val simulationDuration = end - start

        // Not sure what is a correct way to calculate average load
//        val processingTime = processedTasks.sumOf { it.taskSize.parallelTime }
        val processingTime = processedTasks.sumOf { (it.processingEndedAt!! - it.processingStartedAt!!) * it.maxNumberOfWantedNodes }
        return processingTime / (simulationDuration * availableNodesNumber)
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
