package algorithm

import round
import task.Task
import task.TaskDefinition
import task.TaskSize

object ParallelIfPossible : SchedulingAlgorithm<ParallelIfPossibleTask> {
    override val statsCalculator = ParallelIfPossibleStatsCalculator

    override fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double): List<ParallelIfPossibleTask> {
        var timer = 0.0
        var currentlyAvailableNodesNumber: Int
        var lastTimeWindowLength: Double
        var nextEventTime: Double?

        val parallelIfPossibleTasks = tasks.map(::ParallelIfPossibleTask)

        while (parallelIfPossibleTasks.any { it.processingEndedAt == null }) {
            val ready = parallelIfPossibleTasks
                .filter { it.appearedAt <= timer && it.processingEndedAt == null }
                .sortedBy { it.maxNumberOfWantedNodes }
            // zerowanie przydzielonych node
            for (task in ready) {
                task.currentNumberOfNodes = 0
            }
            currentlyAvailableNodesNumber = availableNodesNumber
            // przydzielanie do zadań wolnych węzłów
            while (currentlyAvailableNodesNumber > 0) {
                for (task in ready) {
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
                if (ready.all { it.maxNumberOfWantedNodes == it.currentNumberOfNodes }) {
                    break
                }
            }
            val active = ready.filter { it.currentNumberOfNodes > 0 }
            // obliczanie przewidywanego czasu zakończenia zadania przy aktualnej liczbie węzłów
            for (task in active) {
                task.estimatedProcessingEndedAt =
                    (timer + task.calculateProcessingTime(task.currentNumberOfNodes, C, task.remainingRatio)).round(2)
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
        task.processingTime = (task.processingTime + lastTimeWindowLength).round(2)

        val estimatedProcessingTime = task.estimatedProcessingEndedAt!! - (timer - lastTimeWindowLength)
        val processingElapsedRatio = (lastTimeWindowLength / estimatedProcessingTime).round(2)
        task.remainingRatio = (task.remainingRatio - task.remainingRatio * processingElapsedRatio).round(2)

        if (task.remainingRatio <= 0.0) {
            task.processingEndedAt = timer
        }
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
    var processingTime: Double = 0.0,
    var currentNumberOfNodes: Int = 0,
    var remainingRatio: Double = 1.0,
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