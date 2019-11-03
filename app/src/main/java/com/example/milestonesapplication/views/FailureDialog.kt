package com.example.milestonesapplication.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.milestonesapplication.databinding.FailureDialogLayoutBinding
import com.example.milestonesapplication.interfaces.FailureDialogInterface

class FailureDialog : DialogFragment() {

    private lateinit var binding: FailureDialogLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FailureDialogLayoutBinding.inflate(inflater)
        binding.parentView = this

        isCancelable = false
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }

        return binding.root
    }

    fun refresh() {
        this.dismiss()
        if (activity is FailureDialogInterface) {
            (activity as FailureDialogInterface).onRefreshClick()
        }
    }

    companion object {
        @JvmField
        var TAG = FailureDialog::class.java.name
    }

}