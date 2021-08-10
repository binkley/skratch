@file:OptIn(ExperimentalTime::class)

package hm.binkley.labs.skratch.datetime

import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

fun main() {
    val now = System.now()
    println(now)

    val earlier = now.minusSeconds(65)
    println(earlier)
}

private fun Instant.minusSeconds(seconds: Long) =
    minus(Duration.seconds(seconds))
