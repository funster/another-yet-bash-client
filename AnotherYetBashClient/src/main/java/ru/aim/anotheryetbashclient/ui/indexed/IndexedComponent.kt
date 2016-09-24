package ru.aim.anotheryetbashclient.ui.indexed

import dagger.Component
import ru.aim.anotheryetbashclient.data.DataComponent

@Component(dependencies = arrayOf(DataComponent::class))
interface IndexedComponent {

    fun inject(presenter: IndexedPresenter)
}