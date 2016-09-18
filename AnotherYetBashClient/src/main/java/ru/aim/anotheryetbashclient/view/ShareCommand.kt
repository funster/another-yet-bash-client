package ru.aim.anotheryetbashclient.view

import org.simplepresenter.commands.DistinctViewCommand
import ru.aim.anotheryetbashclient.data.Quote

class ShareCommand(val type: ShareType = ShareType.DEFAULT, val quote: Quote) : DistinctViewCommand