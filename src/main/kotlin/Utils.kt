import org.apache.commons.math3.distribution.GammaDistribution
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.util.Precision

/** Erlang distribution is a special case of Gamma distribution with shape parameter being a positive integer. */
fun erlangDistribution(randomGenerator: RandomGenerator, shape: Int, scale: Double) =
    GammaDistribution(randomGenerator, shape.toDouble(), scale)

@JvmInline
value class Probability(val value: Double) {
    init {
        require(value in 0.0..1.0)
    }
}

fun Double.round(scale: Int) =
    Precision.round(this, scale)

fun List<Double>.standardDeviation() =
    StandardDeviation(false).evaluate(this.toDoubleArray())

fun List<Double>.coefficientOfVariation() =
    this.standardDeviation() / this.average()
