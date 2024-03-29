package task

data class TaskDataGeneratorOutput(
    val inputParameters: TaskDataGeneratorInputParameters,
    val tasks: List<TaskDefinition>
)

data class TaskDataGeneratorInputParameters(
    val randomSeed: Int,
    val totalTaskCount: Int,
    val phaseCount: Int,
    val smallTaskMaxNumberOfWantedNodesRange: IntRange,
    val bigTaskMaxNumberOfWantedNodesRange: IntRange,
    val smallTaskAverageSize: Double,
    val bigTaskAverageSize: Double,
    val bigLoadAverageTaskInterval: Double,
    val smallLoadAverageTaskInterval: Double,
    val averageTaskIntervalDelta: Double,
    val averageTaskSizeDelta: Double
) {
    constructor(taskDataGenerator: TaskDataGenerator) : this(
        randomSeed = taskDataGenerator.randomSeed,
        totalTaskCount = taskDataGenerator.totalTaskCount,
        phaseCount = taskDataGenerator.phaseCount,
        smallTaskMaxNumberOfWantedNodesRange = taskDataGenerator.smallTaskMaxNumberOfWantedNodesRange,
        bigTaskMaxNumberOfWantedNodesRange = taskDataGenerator.bigTaskMaxNumberOfWantedNodesRange,
        smallTaskAverageSize = taskDataGenerator.smallTaskAverageSize,
        bigTaskAverageSize = taskDataGenerator.bigTaskAverageSize,
        bigLoadAverageTaskInterval = taskDataGenerator.bigLoadAverageTaskInterval,
        smallLoadAverageTaskInterval = taskDataGenerator.smallLoadAverageTaskInterval,
        averageTaskIntervalDelta = taskDataGenerator.averageTaskIntervalDelta,
        averageTaskSizeDelta = taskDataGenerator.averageTaskSizeDelta
    )
}
