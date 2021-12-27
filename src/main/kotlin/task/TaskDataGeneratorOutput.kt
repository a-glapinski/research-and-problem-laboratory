package task

data class TaskDataGeneratorOutput(
    val inputParameters: TaskDataGeneratorInputParameters,
    val tasks: List<TaskDefinition>
)

data class TaskDataGeneratorInputParameters(
    val totalTaskCount: Int,
    val phaseCount: Int,
    val taskMaxNumberOfWantedNodes: Int,
    val smallTaskAverageProcessingTime: Double,
    val bigTaskAverageProcessingTime: Double,
    val averageTaskIntervalDelta: Double,
    val bigLoadAverageTaskInterval: Double,
    val smallLoadAverageTaskInterval: Double,
    val randomSeed: Int
) {
    constructor(taskDataGenerator: TaskDataGenerator) : this(
        totalTaskCount = taskDataGenerator.totalTaskCount,
        phaseCount = taskDataGenerator.phaseCount,
        taskMaxNumberOfWantedNodes = taskDataGenerator.taskMaxNumberOfWantedNodes,
        smallTaskAverageProcessingTime = taskDataGenerator.smallTaskAverageProcessingTime,
        bigTaskAverageProcessingTime = taskDataGenerator.bigTaskAverageProcessingTime,
        averageTaskIntervalDelta = taskDataGenerator.averageTaskIntervalDelta,
        bigLoadAverageTaskInterval = taskDataGenerator.bigLoadAverageTaskInterval,
        smallLoadAverageTaskInterval = taskDataGenerator.smallLoadAverageTaskInterval,
        randomSeed = taskDataGenerator.randomSeed,
    )
}
