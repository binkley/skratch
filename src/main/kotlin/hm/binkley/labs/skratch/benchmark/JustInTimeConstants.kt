package hm.binkley.labs.skratch.benchmark

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode.AverageTime
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit.NANOSECONDS
import java.util.concurrent.TimeUnit.SECONDS

@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = SECONDS)
@Fork(3)
@BenchmarkMode(AverageTime)
@OutputTimeUnit(NANOSECONDS)
@State(Scope.Benchmark)
class JustInTimeConstants {
    internal val x_inst_final = java.lang.Long.getLong("divisor", 1000)!!
    internal var x_inst = java.lang.Long.getLong("divisor", 1000)!!

    @Benchmark
    fun _static_final(): Long {
        return 1000 / x_static_final
    }

    @Benchmark
    fun _static(): Long {
        return 1000 / x_static
    }

    @Benchmark
    fun _inst_final(): Long {
        return 1000 / x_inst_final
    }

    @Benchmark
    fun _inst(): Long {
        return 1000 / x_inst
    }

    companion object {
        internal val x_static_final = java.lang.Long.getLong("divisor",
                1000)!!
        internal var x_static = java.lang.Long.getLong("divisor", 1000)!!

        object BenchmarkRunner {
            @Throws(Exception::class)
            @JvmStatic
            fun main(args: Array<String>) {
                org.openjdk.jmh.Main.main(args)
            }
        }
    }
}
