package task

import erlangDistribution
import org.apache.commons.math3.distribution.ExponentialDistribution
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.random.RandomGeneratorFactory
import round
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.random.nextInt

class TaskDataGenerator(
    val randomSeed: Int,
    val totalTaskCount: Int,
    val phaseCount: Int,
    val taskMaxNumberOfWantedNodes: Int,
    smallTaskAverageSize: Double,
    bigTaskAverageSize: Double,
    bigLoadAverageTaskInterval: Double,
    smallLoadAverageTaskInterval: Double,
    val averageTaskIntervalDelta: Double = 0.0,
    val averageTaskSizeDelta: Double = 0.0
) {
    val smallTaskAverageSize = smallTaskAverageSize - averageTaskSizeDelta
    val bigTaskAverageSize = bigTaskAverageSize + averageTaskSizeDelta

    val bigLoadAverageTaskInterval = bigLoadAverageTaskInterval - averageTaskIntervalDelta
    val smallLoadAverageTaskInterval = smallLoadAverageTaskInterval + averageTaskIntervalDelta

    init {
        require(totalTaskCount > 0)
        require(phaseCount > 0)
        require(taskMaxNumberOfWantedNodes > 0)
        require(this.smallTaskAverageSize < this.bigTaskAverageSize)
        require(this.bigLoadAverageTaskInterval < this.smallLoadAverageTaskInterval)
        require(averageTaskIntervalDelta < bigLoadAverageTaskInterval)
        require(averageTaskSizeDelta < smallTaskAverageSize)
        require(listOf(averageTaskIntervalDelta, averageTaskSizeDelta).any { it == 0.0 })
    }

    fun generate(): TaskDataGeneratorOutput {
        val taskPerPhaseCount = totalTaskCount / phaseCount
        var timer = 0.0
        var taskId = 0
        val tasks = (0 until phaseCount).flatMap { i ->
            (0 until taskPerPhaseCount).map {
                val nextTaskInterval =
                    if (i % 2 == 0)
                        bigLoadTaskIntervalExponentialDistribution.sample().round(2)
                    else
                        smallLoadTaskIntervalExponentialDistribution.sample().round(2)

                val taskSize =
                    if (i % 2 == 0)
                        TaskSize(taskSizeRandom, totalTime = bigTaskSizeErlangDistribution.sample().round(2))
                    else
                        TaskSize(taskSizeRandom, totalTime = smallTaskSizeErlangDistribution.sample().round(2))

                val task = TaskDefinition(
                    id = ++taskId,
                    taskSize = taskSize,
                    maxNumberOfWantedNodes = maxNumberOfWantedNodesRandom.nextInt(1..taskMaxNumberOfWantedNodes),
                    appearedAt = timer,
                    nextTaskInterval = nextTaskInterval
                )
                timer = (timer + task.nextTaskInterval).round(2)
                task
            }
        }

        return TaskDataGeneratorOutput(
            inputParameters = TaskDataGeneratorInputParameters(this),
            tasks = tasks
        )
    }

    private val taskSizeRandom = getRandom()
    private val maxNumberOfWantedNodesRandom = getRandom()

    private val bigLoadTaskIntervalExponentialDistribution =
        ExponentialDistribution(getRandomGenerator(), this.bigLoadAverageTaskInterval)
    private val smallLoadTaskIntervalExponentialDistribution =
        ExponentialDistribution(getRandomGenerator(), this.smallLoadAverageTaskInterval)

    private val bigTaskSizeErlangDistribution =
        erlangDistribution(getRandomGenerator(), shape = 1, scale = this.bigTaskAverageSize)
    private val smallTaskSizeErlangDistribution =
        erlangDistribution(getRandomGenerator(), shape = 1, scale = this.smallTaskAverageSize)

    private fun getRandomGenerator(): RandomGenerator =
        RandomGeneratorFactory.createRandomGenerator(getRandom().asJavaRandom())

    private fun getRandom(): Random =
        Random(randomSeed)
}