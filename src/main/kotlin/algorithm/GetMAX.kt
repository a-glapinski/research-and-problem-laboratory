package algorithm

import round
import task.Task
import task.TaskDefinition
import task.TaskSize

object GetMAX : SchedulingAlgorithm<GetMAXTask> {
    override val statsCalculator = GetMAXStatsCalculator

    override fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double): List<GetMAXTask> {
        var timer = 0.0
        var currentlyAvailableNodesNumber = availableNodesNumber

        val waiting = tasks.map(::GetMAXTask).toMutableList()
        val processing = mutableListOf<GetMAXTask>()
        val finished = mutableListOf<GetMAXTask>()

        while (waiting.any { it.maxNumberOfWantedNodes <= availableNodesNumber }) {
            // Picking first fitting task from the queue
            val waitingTask = waiting.firstOrNull {
                it.appearedAt <= timer && it.maxNumberOfWantedNodes <= currentlyAvailableNodesNumber
            }
            waiting.remove(waitingTask)

            // Moving task to currently processing tasks list
            waitingTask?.copy(
                processingStartedAt = timer,
                processingEndedAt =
                (timer + waitingTask.calculateProcessingTime(waitingTask.maxNumberOfWantedNodes, C)).round(2)
            )?.let { task ->
                processing.add(task)
                currentlyAvailableNodesNumber -= task.maxNumberOfWantedNodes
            }

            timer = calculateNextEventTime(tasks = waiting + processing, timer)

            // Moving finished task from currently processing tasks list
            val finishedTask = processing.firstOrNull {
                it.processingEndedAt!! <= timer
            }
            processing.remove(finishedTask)
            finishedTask?.let { task ->
                finished.add(task)
                currentlyAvailableNodesNumber += task.maxNumberOfWantedNodes
            }
        }

        return finished
    }

    private fun calculateNextEventTime(tasks: List<GetMAXTask>, timer: Double): Double {
        val taskFinishedEvents = tasks.mapNotNull { it.processingEndedAt }.filter { it > timer }
        val taskAppearedEvents = tasks.map { it.appearedAt }.filter { it > timer }
        return (taskFinishedEvents + taskAppearedEvents).minOf { it }
    }
}

data class GetMAXTask(
    override val id: Int,
    override val taskSize: TaskSize,
    override val maxNumberOfWantedNodes: Int,
    override val appearedAt: Double,
    override val nextTaskInterval: Double,
    val processingStartedAt: Double? = null,
    val processingEndedAt: Double? = null
) : Task {
    constructor(taskDefinition: TaskDefinition) : this(
        id = taskDefinition.id,
        taskSize = taskDefinition.taskSize,
        maxNumberOfWantedNodes = taskDefinition.maxNumberOfWantedNodes,
        appearedAt = taskDefinition.appearedAt,
        nextTaskInterval = taskDefinition.nextTaskInterval
    )
}