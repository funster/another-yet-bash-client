package ru.aim.anotheryetbashclient.parser

import android.support.annotation.RawRes
import android.test.InstrumentationTestCase
import java.io.InputStream

abstract class BaseRawFileTestCase : InstrumentationTestCase() {

    protected fun getFile(@RawRes id: Int): InputStream {
        return instrumentation.context.resources.openRawResource(id)
    }
}