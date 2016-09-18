package ru.aim.anotheryetbashclient

import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View

class AboutDialog : DialogFragment(), View.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val view = View.inflate(activity, R.layout.about_screen, null)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.about_app_title)
        builder.setView(view)
        view.findViewById(android.R.id.text1).setOnClickListener(this)
        view.findViewById(android.R.id.text2).setOnClickListener(this)
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }

    override fun onClick(v: View) {
        when (v.id) {
            android.R.id.text1 -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                activity.startActivity(intent)
            }
            android.R.id.text2 -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(getString(R.string.bash_url))
                startActivity(i)
            }
        }
    }
}
