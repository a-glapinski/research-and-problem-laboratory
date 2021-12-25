package task

import round

fun Task.calculateRealProcessingTime(allocatedNodesNumber: Int, C: Double): Double {
    val sequentialProcessingTime =
        taskSize.sequentialTime + C * (allocatedNodesNumber - 1)
    val parallelProcessingTime = taskSize.parallelTime / allocatedNodesNumber
    return (sequentialProcessingTime + parallelProcessingTime).round(2)
}