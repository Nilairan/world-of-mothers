package com.madispace.worldofmothers.ui.loadphoto.dialog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.madispace.worldofmothers.common.createFile
import com.madispace.worldofmothers.common.getUriByFile
import com.madispace.worldofmothers.databinding.DialogChooseLoadPhotoBinding
import java.io.File

class ChooseLoadPhotoDialog : BottomSheetDialogFragment() {

    private val binding: DialogChooseLoadPhotoBinding by viewBinding(CreateMethod.INFLATE)
    private var file: File? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            cameraImage.setOnClickListener {
                with(it.context) {
                    file = createFile()
                    captureImage.launch(getUriByFile(file!!))
                }
            }
            storageImage.setOnClickListener {
                loadImage.launch("image/*")
            }
        }
    }

    private val loadImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { safeUri ->
                setFragmentResult(LOAD_MEDIA, Bundle().apply { putParcelable(URI, safeUri) })
                dismiss()
            }
        }

    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        when (it) {
            true -> {
                context?.let {
                    setFragmentResult(
                        LOAD_MEDIA,
                        Bundle().apply { putParcelable(URI, it.getUriByFile(file!!)) })
                    dismiss()
                }
            }
            else -> file?.delete()
        }
    }

    companion object {
        fun newInstance() = ChooseLoadPhotoDialog()
        const val LOAD_MEDIA = "LOAD_MEDIA"
        const val URI = "URI"
    }
}