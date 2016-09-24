package ru.aim.anotheryetbashclient.ui

import org.simplepresenter.commands.DistinctViewCommand
import ru.aim.anotheryetbashclient.data.Quote

class ShareCommand(val type: ShareType = ShareType.DEFAULT, val quote: Quote) : DistinctViewCommand