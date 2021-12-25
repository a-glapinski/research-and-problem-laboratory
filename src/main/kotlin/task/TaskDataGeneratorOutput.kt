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
)
