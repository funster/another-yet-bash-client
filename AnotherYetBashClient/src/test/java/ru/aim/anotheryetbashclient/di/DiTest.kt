package ru.aim.anotheryetbashclient.di

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiTest {

    @Before
    fun before() {
        Di.clear()
        Di.bindFor(A::class.java) {
            withSpecific(AImpl())
            withSpecific(BImpl(), BindPriority.TEST)
        }
    }

    val a: A by inject()
    val b: A by inject(BindPriority.TEST)

    @Test
    fun bindingTest() {
        assertEquals(a.test(), "implementation")
        assertEquals(b.test(), "test")
    }

    interface A {
        fun test(): String
    }

    class AImpl : A {
        override fun test(): String = "implementation"
    }

    class BImpl : A {
        override fun test(): String = "test"
    }
}