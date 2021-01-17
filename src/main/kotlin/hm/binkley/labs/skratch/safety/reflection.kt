package hm.binkley.labs.skratch.safety

import sun.misc.Unsafe
import java.lang.reflect.Field
import java.lang.reflect.Modifier

private val unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").apply {
    isAccessible = true
}.get(null) as Unsafe

@Suppress("UNCHECKED_CAST")
internal fun <T> Class<T>.newBlankInstance(): T =
    unsafe.allocateInstance(this) as T

private val Field.isStatic get() = Modifier.isStatic(modifiers)
private val Field.isTransient get() = Modifier.isTransient(modifiers)

internal val <T> Class<T>.serializedFields
    get() = declaredFields.filterNot {
        it.isStatic || it.isTransient
    }.onEach {
        it.isAccessible = true
    }

/** @todo Non-grossly move assertion to `validating.kt` */
internal fun <T> Class<T>.getSerializedField(name: String) = try {
    getDeclaredField(name).apply {
        isAccessible = true
    }
} catch (e: NoSuchFieldException) {
    throw AssertionError("Bad field name: $name")
}
