package task

import Probability
import erlangDistribution
import org.apache.commons.math3.distribution.ExponentialDistribution
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.random.RandomGeneratorFactory
import round
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.random.nextInt

class TaskDataGenerator(
    private val taskCount: Int,
    private val taskMaxNumberOfWantedNodes: Int,
    private val bigTaskProbability: Probability,
    private val smallTaskAverageProcessingTime: Double,
    private val bigTaskAverageProcessingTime: Double,
    bigLoadAverageTaskInterval: Double,
    private val smallLoadAverageTaskInterval: Double,
    private val bigLoadAverageTaskIntervalDelta: Double = 0.0,
    bigLoadProbability: Probability,
    randomSeed: Int
) {
    private val bigLoadAverageTaskInterval = bigLoadAverageTaskInterval + bigLoadAverageTaskIntervalDelta
    private val smallLoadProbability =
        ((1 - bigLoadProbability.value) * smallLoadAverageTaskInterval - (bigLoadProbability.value - 1) * this.bigLoadAverageTaskInterval - bigLoadAverageTaskIntervalDelta) / (smallLoadAverageTaskInterval - this.bigLoadAverageTaskInterval - bigLoadAverageTaskIntervalDelta)
    private val bigLoadProbability = 1.0 - smallLoadProbability

    private val random = Random(randomSeed)
    private val randomGenerator: RandomGenerator =
        RandomGeneratorFactory.createRandomGenerator(random.asJavaRandom())

    private val bigLoadTaskIntervalExponentialDistribution =
        ExponentialDistribution(randomGenerator, this.bigLoadAverageTaskInterval)
    private val smallLoadTaskIntervalExponentialDistribution =
        ExponentialDistribution(randomGenerator, smallLoadAverageTaskInterval)

    private val bigTaskErlangDistribution =
        erlangDistribution(randomGenerator, shape = 1, scale = bigTaskAverageProcessingTime)
    private val smallTaskErlangDistribution =
        erlangDistribution(randomGenerator, shape = 1, scale = smallTaskAverageProcessingTime)

    fun generate(): List<TaskDefinition> {
        var timer = 0.0
        return (1..taskCount).map { id ->
            val task = generate(id, timer)
            timer = (timer + task.nextTaskInterval).round(2)
            task
        }
    }

    private fun generate(id: Int, timer: Double): TaskDefinition {
        val nextTaskInterval =
            if (random.nextDouble() > bigLoadProbability)
                bigLoadTaskIntervalExponentialDistribution.sample().round(2)
            else
                smallLoadTaskIntervalExponentialDistribution.sample().round(2)

        val taskSize =
            if (random.nextDouble() > bigTaskProbability.value)
                TaskSize(random, time = bigTaskErlangDistribution.sample().round(2))
            else
                TaskSize(random, time = smallTaskErlangDistribution.sample().round(2))

        return TaskDefinition(
            id = id,
            taskSize = taskSize,
            maxNumberOfWantedNodes = random.nextInt(1..taskMaxNumberOfWantedNodes),
            appearedAt = timer,
            nextTaskInterval = nextTaskInterval
        )
    }
}