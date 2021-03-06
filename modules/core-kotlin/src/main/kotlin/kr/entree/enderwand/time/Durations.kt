package kr.entree.enderwand.time

import java.time.Duration

/**
 * Created by JunHyung Lim on 2020-01-06
 */
val Number.milliseconds get() = Duration.ofMillis(toLong())

val Number.seconds get() = Duration.ofSeconds(toLong())

val Number.minutes get() = Duration.ofMinutes(toLong())

val Number.hours get() = Duration.ofHours(toLong())

fun CharSequence.toDuration(parser: DurationParser = DURATION_PARSER_ENGLISH) = parser.parse(toString())

fun CharSequence.toDurationKor() = toDuration(DURATION_PARSER_KOREAN)