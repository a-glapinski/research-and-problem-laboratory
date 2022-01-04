package algorithm

import algorithm.GetMAX.calculateProcessingTime
import round
import task.Task
import task.TaskDefinition
import task.TaskSize

object ParallelIfPossible : SchedulingAlgorithm<ParallelIfPossibleTask> {
    override val statsCalculator = ParallelIfPossibleStatsCalculator

    override fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double): List<ParallelIfPossibleTask> {
        var timer = 0.0
        var currentlyAvailableNodesNumber = availableNodesNumber
        var lastTimeWindowLength = 0.0

        var nextEventTime = 0.0

//        val waiting = tasks.map(::ParallelIfPossibleTask).toMutableList()
        val active = mutableListOf<ParallelIfPossibleTask>().filter { it.appearedAt <= timer && it.processingEndedAt == null }.sortedBy { it.maxNumberOfWantedNodes }
//        val finished = mutableListOf<ParallelIfPossibleTask>()

        while (active != null) {
            // zerowanie przydzielonych node
            for (task in active) {
                task.currentNumberOfNodes = 0
            }
            currentlyAvailableNodesNumber = availableNodesNumber;
            // przydzielanie do zadań wolnych węzłów
            while (currentlyAvailableNodesNumber > 0) {
                for (task in active) {
                    if (task.maxNumberOfWantedNodes != task.currentNumberOfNodes) {
                        task.currentNumberOfNodes = task.currentNumberOfNodes?.plus(1)!!;
                        currentlyAvailableNodesNumber -= 1;
                        if (task.processingStartedAt == null) {
                            task.processingStartedAt = timer;
                        }
                    }
                    if (currentlyAvailableNodesNumber == 0) {
                        break;
                    }
                }
                if (active.filter { it.maxNumberOfWantedNodes != it.currentNumberOfNodes }.isEmpty()) {
                    break;
                }
            }
            // obliczanie przewidywanego czasu zakończenia zadania przy aktualnej liczbie węzłów
            for (task in active) {
                task.estimatedProcessingEndedAt = calculateEstimatedProcessingEndedAt(task, timer);
            }
            // sprawdzić, kiedy wystąpi kolejne zdarzenie i uaktualnić completion_percentage
            nextEventTime = calculateNextEventTime(tasks = active, timer);
            if (nextEventTime != null) {
                lastTimeWindowLength = nextEventTime - timer;
                timer = nextEventTime;
            } else {
                break;
            }
            for (task in active) {
                updateTask(task, lastTimeWindowLength);
            }
        }

        return active
    }

    private fun updateTask(task: ParallelIfPossibleTask, lastTimeWindowLength: Double) {
        task.processingParallelDone += lastTimeWindowLength * task.currentNumberOfNodes;
    }

    private fun calculateEstimatedProcessingEndedAt(task: ParallelIfPossibleTask, timer: Double): Double {
        return timer + calculateTimeToBeingCompleted(task);
    }

    private fun calculateTimeToBeingCompleted(task: ParallelIfPossibleTask): Double {
        return task.taskSize.parallelTime/task.currentNumberOfNodes;
    }

    private fun calculateNextEventTime(tasks: List<ParallelIfPossibleTask>, timer: Double): Double {
        val tasksInProgress = tasks.mapNotNull { it.estimatedProcessingEndedAt }.filter { it > timer }
        val tasksWaiting = tasks.mapNotNull { it.appearedAt }.filter { it > timer }
        return (tasksInProgress + tasksWaiting).minOf { it }
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
    var estimatedProcessingEndedAt: Double?
) : Task {
    constructor(taskDefinition: TaskDefinition) : this(
        id = taskDefinition.id,
        taskSize = taskDefinition.taskSize,
        maxNumberOfWantedNodes = taskDefinition.maxNumberOfWantedNodes,
        appearedAt = taskDefinition.appearedAt,
        nextTaskInterval = taskDefinition.nextTaskInterval
    )
}