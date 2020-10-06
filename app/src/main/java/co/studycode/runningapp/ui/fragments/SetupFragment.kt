package co.studycode.runningapp.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.studycode.runningapp.R
import co.studycode.runningapp.ui.viewmodels.MainViewModel
import co.studycode.runningapp.utils.Constants
import co.studycode.runningapp.utils.Constants.KET_FIRST_TIME_TOGLE
import co.studycode.runningapp.utils.Constants.KEY_IMAGE
import co.studycode.runningapp.utils.Constants.KEY_NAME
import co.studycode.runningapp.utils.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_setup.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {


    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstTime = true

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isFirstTime) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )

        }
        btnSelectImage.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .setTitle("Choose Image")
            val mAlertDialog = dialogBuilder.show()

            mDialogView.gallery_tv.setOnClickListener {
                mAlertDialog.dismiss()
                openGallery()
            }
            mDialogView.camera_tv.setOnClickListener {
                openCamera()
                mAlertDialog.dismiss()
            }
        }

//        save.setOnClickListener {
//            saveProfile()
//            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
//        }

        save.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please enter all fields", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = nameEt.text.toString()
        val weight = weightEt.text.toString()
        val profileImg: Bitmap =
            (img.getDrawable() as BitmapDrawable).getBitmap()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
//encode image to base64 string
        img.buildDrawingCache()
        val bitmap: Bitmap = img.getDrawingCache()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        val img_str = Base64.encodeToString(image, 0)

        //decode string to image

        //decode string to image
        val imageAsBytes =
            Base64.decode(img_str.toByteArray(), Base64.DEFAULT)
        val ivsavedphoto: ImageView = img as ImageView
        ivsavedphoto.setImageBitmap(
            BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
        )


        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KET_FIRST_TIME_TOGLE, false)
            .putString(KEY_IMAGE, img_str)
            .apply()
//        val toolbarText = "Let's go , $name"
//        requireActivity().tvToolbarTitle.text = toolbarText
        return true

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.REQUEST_PICK_IMAGE)
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                Constants.REQUEST_PERMISSION
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCameraPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
//                val uri:Uri =data.data


                // This is the key line item, URI specifies the name of the data
                val mImageUri = data.data
                img.setImageBitmap(bitmap)
            } else if (requestCode == Constants.REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                img.setImageURI(uri)
            }
        }
    }


}