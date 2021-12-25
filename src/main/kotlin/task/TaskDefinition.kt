package task

import kotlinx.serialization.Serializable
import round
import kotlin.random.Random

interface Task {
    val id: Int
    val taskSize: TaskSize
    val maxNumberOfWantedNodes: Int
    val appearedAt: Double
    val nextTaskInterval: Double
}

@Serializable
data class TaskDefinition(
    override val id: Int,
    override val taskSize: TaskSize,
    override val maxNumberOfWantedNodes: Int,
    override val appearedAt: Double,
    override val nextTaskInterval: Double
) : Task

@Serializable
data class TaskSize(
    val sequentialTime: Double,
    val parallelTime: Double
) {
    constructor(random: Random, time: Double) : this(
        sequentialTime = (time * random.nextDouble(from = 0.05, until = 0.20)).round(2),
        parallelTime = time
    )

    val totalTime get() = sequentialTime + parallelTime
}
