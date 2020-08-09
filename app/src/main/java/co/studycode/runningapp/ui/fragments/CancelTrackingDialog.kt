package co.studycode.runningapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import co.studycode.runningapp.R

class CancelTrackingDialog :DialogFragment(){
    private var _listener:(()-> Unit)? = null

    fun setListener(listener:()-> Unit){

        _listener = listener

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return  AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel Run")
            .setMessage("Are you sure you want to cancel")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                _listener?.let { y ->
                    y()
                }
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()


    }

}