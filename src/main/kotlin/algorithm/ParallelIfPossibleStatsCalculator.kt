package algorithm

import simulation.SimulationStatsCalculator

object ParallelIfPossibleStatsCalculator : SimulationStatsCalculator<ParallelIfPossibleTask>() {
    override fun calculateAverageProcessingTime(processedTasks: List<ParallelIfPossibleTask>) =
        processedTasks.map { it.processingEndedAt!! - it.processingStartedAt!! }.average()

    override fun calculateAverageResponseTime(processedTasks: List<ParallelIfPossibleTask>) =
        processedTasks.map { it.processingEndedAt!! - it.appearedAt }.average()

    override fun calculateAverageDelayTime(processedTasks: List<ParallelIfPossibleTask>) =
        processedTasks.map { it.processingStartedAt!! - it.appearedAt }.average()
}