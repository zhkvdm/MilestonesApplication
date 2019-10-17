package com.example.milestonesapplication.View

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.milestonesapplication.Interface.FailureDialogInterface
import com.example.milestonesapplication.R
import com.example.milestonesapplication.databinding.FailureDialogLayoutBinding

class FailureDialog : DialogFragment() {

    private lateinit var binding: FailureDialogLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.failure_dialog_layout, container, false)
        binding.refreshButton.setOnClickListener {
            this.dismiss()
            if (activity is FailureDialogInterface) {
                (activity as FailureDialogInterface).refresh()
            }
        }

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    companion object {
        @kotlin.jvm.JvmField
        var TAG = FailureDialog::class.java.name
    }

}