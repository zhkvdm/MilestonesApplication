package com.example.milestonesapplication.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.example.milestonesapplication.R
import com.example.milestonesapplication.databinding.WaitDialogLayoutBinding

class WaitDialog : DialogFragment() {

    private lateinit var binding: WaitDialogLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.wait_dialog_layout, container, false)

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    companion object {
        @kotlin.jvm.JvmField
        var TAG = InfoWindow::class.java.name
    }
}
