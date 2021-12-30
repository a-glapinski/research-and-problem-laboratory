package simulation

import coefficientOfVariation
import round
import task.Task

abstract class SimulationStatsCalculator<T : Task> {
    fun calculate(processedTasks: List<T>, availableNodesNumber: Int) =
        SimulationStats(
            averageProcessingTime = calculateAverageProcessingTime(processedTasks).round(3),
            averageResponseTime = calculateAverageResponseTime(processedTasks).round(3),
            averageDelayTime = calculateAverageDelayTime(processedTasks).round(3),
            averageLoad = calculateAverageLoad(processedTasks, availableNodesNumber).round(3),
            averageTaskInterval = calculateAverageTaskInterval(processedTasks).round(3),
            averageTaskSize = calculateAverageTaskSize(processedTasks).round(3),
            taskIntervalCoefficientOfVariation = calculateTaskIntervalCoefficientOfVariation(processedTasks).round(3),
            taskSizeCoefficientOfVariation = calculateTaskSizeCoefficientOfVariation(processedTasks).round(3)
        )

    protected abstract fun calculateAverageProcessingTime(processedTasks: List<T>): Double

    protected abstract fun calculateAverageResponseTime(processedTasks: List<T>): Double

    protected abstract fun calculateAverageDelayTime(processedTasks: List<T>): Double

    protected fun calculateAverageLoad(processedTasks: List<T>, availableNodesNumber: Int): Double {
        val lambda = processedTasks.count() / processedTasks.maxOf { it.appearedAt }
        val mi = 1 / calculateAverageTaskSize(processedTasks)
        return lambda / (mi * availableNodesNumber)
    }

    protected fun calculateAverageTaskInterval(processedTasks: List<T>): Double =
        processedTasks.map { it.nextTaskInterval }.average()

    protected fun calculateAverageTaskSize(processedTasks: List<T>): Double =
        processedTasks.map { it.taskSize.totalTime }.average()

    protected fun calculateTaskIntervalCoefficientOfVariation(processedTasks: List<T>): Double =
        processedTasks.map { it.nextTaskInterval }.coefficientOfVariation()

    protected fun calculateTaskSizeCoefficientOfVariation(processedTasks: List<T>): Double =
        processedTasks.map { it.taskSize.totalTime }.coefficientOfVariation()
}