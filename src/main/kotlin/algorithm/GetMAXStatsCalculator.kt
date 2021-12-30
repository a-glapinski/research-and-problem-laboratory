package algorithm

import simulation.SimulationStatsCalculator

object GetMAXStatsCalculator : SimulationStatsCalculator<GetMAXTask>() {
    override fun calculateAverageProcessingTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.processingStartedAt!! }.average()

    override fun calculateAverageResponseTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingEndedAt!! - it.appearedAt }.average()

    override fun calculateAverageDelayTime(processedTasks: List<GetMAXTask>) =
        processedTasks.map { it.processingStartedAt!! - it.appearedAt }.average()
}
