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
    val smallTaskAverageProcessingTime: Double,
    val bigTaskAverageProcessingTime: Double,
    bigLoadAverageTaskInterval: Double,
    smallLoadAverageTaskInterval: Double,
    val averageTaskIntervalDelta: Double = 0.0
) {
    val bigLoadAverageTaskInterval = bigLoadAverageTaskInterval - averageTaskIntervalDelta
    val smallLoadAverageTaskInterval = smallLoadAverageTaskInterval + averageTaskIntervalDelta

    init {
        require(totalTaskCount > 0)
        require(phaseCount > 0)
        require(taskMaxNumberOfWantedNodes > 0)
        require(smallTaskAverageProcessingTime < bigTaskAverageProcessingTime)
        require(this.bigLoadAverageTaskInterval < this.smallLoadAverageTaskInterval)
        require(averageTaskIntervalDelta < bigLoadAverageTaskInterval)
    }

    private val random = Random(randomSeed)
    private val randomGenerator: RandomGenerator =
        RandomGeneratorFactory.createRandomGenerator(random.asJavaRandom())

    private val bigLoadTaskIntervalExponentialDistribution =
        ExponentialDistribution(randomGenerator, this.bigLoadAverageTaskInterval)
    private val smallLoadTaskIntervalExponentialDistribution =
        ExponentialDistribution(randomGenerator, this.smallLoadAverageTaskInterval)

    private val bigTaskErlangDistribution =
        erlangDistribution(randomGenerator, shape = 1, scale = bigTaskAverageProcessingTime)
    private val smallTaskErlangDistribution =
        erlangDistribution(randomGenerator, shape = 1, scale = smallTaskAverageProcessingTime)

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
                        TaskSize(random, time = bigTaskErlangDistribution.sample().round(2))
                    else
                        TaskSize(random, time = smallTaskErlangDistribution.sample().round(2))

                val task = TaskDefinition(
                    id = ++taskId,
                    taskSize = taskSize,
                    maxNumberOfWantedNodes = random.nextInt(1..taskMaxNumberOfWantedNodes),
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
}