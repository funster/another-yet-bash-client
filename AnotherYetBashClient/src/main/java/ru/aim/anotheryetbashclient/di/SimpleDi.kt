package ru.aim.anotheryetbashclient.di

import java.util.*

/**
 * Simple (very) dependency injection based on kotlin delegate properties.
 */

inline fun <reified T : Any> inject(priority: Int = BindPriority.DEFAULT) = lazy { doInject(T::class.java, priority) }

object Di {

    private val bindings = ArrayList<Binding<out Any>>()

    fun <T : Any> bindFor(base: Class<T>, f: BindingBuilder<T>.() -> Unit) = BindingBuilder(base).apply { f.invoke(this) }

    fun <T : Any> bindFor(base: Class<T>) = BindingBuilder(base)

    fun <T : Any> findBinding(base: Class<T>) = bindings.find { it.base == base }

    fun <T : Any> addBinding(binding: Binding<T>) = bindings.add(binding)

    fun clear() {
        bindings.forEach { it.impls.clear() }
        bindings.clear()
    }
}

fun <T : Any> doInject(clazz: Class<T>, priority: Int = BindPriority.DEFAULT): T {
    val binding = Di.findBinding(clazz) ?: throw IllegalStateException("No binding for ${clazz.canonicalName}")
    val candidates = binding.impls.filter { it.priority == priority }
    @Suppress("UNCHECKED_CAST")
    return when {
        candidates.isEmpty() -> throw IllegalStateException("None candidates for ${clazz.canonicalName}")
        candidates.count() > 1 -> throw IllegalStateException("More than on candidates for ${clazz.canonicalName}")
        else -> candidates.first().impl as T
    }
}

data class Binding<T : Any>(val base: Class<T>, val impls: MutableList<BindImpl<T>> = ArrayList<BindImpl<T>>())

data class BindImpl<T>(val impl: T, val priority: Int = BindPriority.DEFAULT)

@Suppress("unused")
object BindPriority {
    const val DEFAULT = 1
    const val TEST = 2
}

@Suppress("UNCHECKED_CAST")
class BindingBuilder<T : Any>(base: Class<T>) {

    private var binding: Binding<T>? = null

    init {
        val tmpBinding = Di.findBinding(base)
        if (tmpBinding != null) {
            binding = tmpBinding as Binding<T>?
        } else {
            Di.addBinding(Binding(base))
            binding = Di.findBinding(base) as Binding<T>
        }
    }

    fun withSpecific(impl: T, priority: Int = BindPriority.DEFAULT): BindingBuilder<T> {
        binding?.impls?.add(BindImpl(impl, priority))
        return this
    }
}