package ru.aim.anotheryetbashclient.support

import android.os.Bundle
import ru.aim.anotheryetbashclient.R

class SampleActivity : DelegateActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
    }
}