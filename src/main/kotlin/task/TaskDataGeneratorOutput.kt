package task

data class TaskDataGeneratorOutput(
    val inputParameters: TaskDataGeneratorInputParameters,
    val tasks: List<TaskDefinition>
)

data class TaskDataGeneratorInputParameters(
    val taskCount: Int,
    val taskMaxNumberOfWantedNodes: Int,
    val smallTaskAverageProcessingTime: Double,
    val bigTaskAverageProcessingTime: Double,
    val averageTaskIntervalDelta: Double,
    val bigLoadAverageTaskInterval: Double,
    val smallLoadAverageTaskInterval: Double,
    val randomSeed: Int
) {
    constructor(taskDataGenerator: TaskDataGenerator) : this(
        taskCount = taskDataGenerator.taskCount,
        taskMaxNumberOfWantedNodes = taskDataGenerator.taskMaxNumberOfWantedNodes,
        smallTaskAverageProcessingTime = taskDataGenerator.smallTaskAverageProcessingTime,
        bigTaskAverageProcessingTime = taskDataGenerator.bigTaskAverageProcessingTime,
        averageTaskIntervalDelta = taskDataGenerator.averageTaskIntervalDelta,
        bigLoadAverageTaskInterval = taskDataGenerator.bigLoadAverageTaskInterval,
        smallLoadAverageTaskInterval = taskDataGenerator.smallLoadAverageTaskInterval,
        randomSeed = taskDataGenerator.randomSeed,
    )
}
