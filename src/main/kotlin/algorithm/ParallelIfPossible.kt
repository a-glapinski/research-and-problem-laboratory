package algorithm

import round
import task.Task
import task.TaskDefinition
import task.TaskSize

object ParallelIfPossible : SchedulingAlgorithm<ParallelIfPossibleTask> {
    override val statsCalculator = ParallelIfPossibleStatsCalculator

    override fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double): List<ParallelIfPossibleTask> {
        var timer = 0.0
        var currentlyAvailableNodesNumber = availableNodesNumber
        var lastTimeWindowLength: Double
        var nextEventTime: Double?

        val parallelIfPossibleTasks = tasks.map(::ParallelIfPossibleTask)

        while (parallelIfPossibleTasks.any { it.processingEndedAt == null }) {
            val active = parallelIfPossibleTasks
                .filter { it.appearedAt <= timer && it.processingEndedAt == null }
                .sortedBy { it.maxNumberOfWantedNodes }
            // zerowanie przydzielonych node
            for (task in active) {
                task.currentNumberOfNodes = 0
            }
            currentlyAvailableNodesNumber = availableNodesNumber
            // przydzielanie do zadań wolnych węzłów
            while (currentlyAvailableNodesNumber > 0) {
                for (task in active) {
                    if (task.maxNumberOfWantedNodes != task.currentNumberOfNodes) {
                        task.currentNumberOfNodes += 1
                        currentlyAvailableNodesNumber -= 1
                        if (task.processingStartedAt == null) {
                            task.processingStartedAt = timer
                        }
                    }
                    if (currentlyAvailableNodesNumber == 0) {
                        break
                    }
                }
                if (active.all { it.maxNumberOfWantedNodes == it.currentNumberOfNodes }) {
                    break
                }
            }
            // obliczanie przewidywanego czasu zakończenia zadania przy aktualnej liczbie węzłów
            for (task in active) {
                task.estimatedProcessingEndedAt = calculateEstimatedProcessingEndedAt(task, timer).round(2)
            }
            // sprawdzić, kiedy wystąpi kolejne zdarzenie i uaktualnić completion_percentage
            nextEventTime = calculateNextEventTime(tasks = parallelIfPossibleTasks, timer)
            if (nextEventTime != null) {
                lastTimeWindowLength = (nextEventTime - timer).round(2)
                timer = nextEventTime
            } else {
                break
            }
            for (task in active) {
                updateTask(task, lastTimeWindowLength, timer)
            }
        }

        return parallelIfPossibleTasks
    }

    private fun updateTask(task: ParallelIfPossibleTask, lastTimeWindowLength: Double, timer: Double) {
        task.processingParallelDone = (task.processingParallelDone + lastTimeWindowLength * task.currentNumberOfNodes).round(2)
        if ((task.processingParallelDone / task.taskSize.parallelTime) > 0.999) {
            task.processingParallelDone = task.taskSize.parallelTime
            task.processingEndedAt = timer
        }
    }

    private fun calculateEstimatedProcessingEndedAt(task: ParallelIfPossibleTask, timer: Double): Double {
        return timer + calculateTimeToBeingCompleted(task)
    }

    private fun calculateTimeToBeingCompleted(task: ParallelIfPossibleTask): Double {
        return ((task.taskSize.parallelTime - task.processingParallelDone) / task.currentNumberOfNodes).round(2)
    }

    private fun calculateNextEventTime(tasks: List<ParallelIfPossibleTask>, timer: Double): Double? {
        val tasksInProgress = tasks.mapNotNull { it.estimatedProcessingEndedAt }.filter { it > timer }
        val tasksWaiting = tasks.map { it.appearedAt }.filter { it > timer }
        return (tasksInProgress + tasksWaiting)
            .ifEmpty { return null }
            .minOf { it }
            .round(2)
    }
}

data class ParallelIfPossibleTask(
    override val id: Int,
    override val taskSize: TaskSize,
    override val maxNumberOfWantedNodes: Int,
    override val appearedAt: Double,
    override val nextTaskInterval: Double,
    var currentNumberOfNodes: Int = 0,
    var processingParallelDone: Double = 0.0,
    var processingStartedAt: Double? = null,
    var processingEndedAt: Double? = null,
    var estimatedProcessingEndedAt: Double? = null
) : Task {
    constructor(taskDefinition: TaskDefinition) : this(
        id = taskDefinition.id,
        taskSize = taskDefinition.taskSize,
        maxNumberOfWantedNodes = taskDefinition.maxNumberOfWantedNodes,
        appearedAt = taskDefinition.appearedAt,
        nextTaskInterval = taskDefinition.nextTaskInterval
    )
}