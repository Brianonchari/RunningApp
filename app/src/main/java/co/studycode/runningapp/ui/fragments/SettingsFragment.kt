package co.studycode.runningapp.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.studycode.runningapp.R
import co.studycode.runningapp.ui.viewmodels.MainViewModel
import co.studycode.runningapp.utils.Constants
import co.studycode.runningapp.utils.Constants.KEY_IMAGE
import co.studycode.runningapp.utils.Constants.KEY_NAME
import co.studycode.runningapp.utils.Constants.KEY_WEIGHT
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                Snackbar.make(view, "Saved Changes", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(view, "Please fill out all fielda", Snackbar.LENGTH_LONG).show()
            }
        }
        img.setOnClickListener {
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
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPref.getString(KEY_NAME, "")
        val weight = sharedPref.getFloat(KEY_WEIGHT, 63f)
        val image = sharedPref.getString(KEY_IMAGE, "")
        etName.setText(name)
        etWeight.setText(weight.toString())
        Glide.with(this).load(image).into(img)
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


    private fun applyChangesToSharedPref(): Boolean {
        val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()
        if (nameText.isEmpty() || weightText.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, nameText)
            .putFloat(KEY_WEIGHT, weightText.toFloat())
            .apply()

        val toolbarText = "Let's go $nameText"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true
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
                img.setImageBitmap(bitmap)
            } else if (requestCode == Constants.REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                img.setImageURI(uri)
            }
        }
    }
}