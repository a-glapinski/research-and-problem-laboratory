package task

import round
import kotlin.random.Random

interface Task {
    val id: Int
    val taskSize: TaskSize
    val maxNumberOfWantedNodes: Int
    val appearedAt: Double
    val nextTaskInterval: Double
}

data class TaskSize(
    val sequentialTime: Double,
    val parallelTime: Double
) {
    constructor(random: Random, time: Double) : this(
        sequentialTime = (time * random.nextDouble(from = 0.05, until = 0.20)).round(2),
        parallelTime = time
    )

    val totalTime get() = (sequentialTime + parallelTime).round(2)
}

fun Task.calculateRealProcessingTime(allocatedNodesNumber: Int, C: Double): Double {
    val sequentialProcessingTime = taskSize.sequentialTime + C * (allocatedNodesNumber - 1)
    val parallelProcessingTime = taskSize.parallelTime / allocatedNodesNumber
    return (sequentialProcessingTime + parallelProcessingTime).round(2)
}