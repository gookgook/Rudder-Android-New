package com.rudder.src.auth.view

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.rudder.R
import com.rudder.databinding.FragmentSignup3Binding
import com.rudder.src.auth.viewmodel.SignUpViewModel
import java.io.File

class SignUp3Fragment: Fragment() {
    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var binding: FragmentSignup3Binding

    var currentImagePicker: Int = 0
    var availablePicker: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setBinding()


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup3, container, false)
        binding.main = viewModel
        binding.frag = this
        return binding.root
    }

    fun setBinding()  {

    }


    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        availablePicker = currentImagePicker + 1

        var currentImageView: ImageView
        when (currentImagePicker) {
            1 -> currentImageView = binding.profile1IV
            2 -> currentImageView = binding.profile2IV
            3 -> currentImageView = binding.profile3IV
            4 -> currentImageView = binding.profile4IV
            5 -> currentImageView = binding.profile5IV
            else -> currentImageView = binding.profile6IV
        }

        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                val imageFile = File(getRealPathFromURI(it))
                val cr: ContentResolver = context?.contentResolver!!
                viewModel.appendImage(imageFile, cr.getType(it).toString())

                Glide.with(this.activity)
                    .load(imageUri)
                    .fitCenter()
                    .into(currentImageView)
            }
            Toast.makeText(this.activity, "Photo pick success", Toast.LENGTH_SHORT).show()
        }
    }

    fun getRealPathFromURI(uri: Uri): String {

        val buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    private fun viewIdToInt(viewId: Int) : Int  {
        when(viewId){
            R.id.cs2 -> return 1
            R.id.cs4 -> return 2
            R.id.cs6 -> return 3
            R.id.cs9 -> return 4
            R.id.cs11 -> return 5
            else -> return 6
        }
    }

    fun selectGallery(view: View) {
        if (availablePicker != viewIdToInt(view.id) ) {
            Toast.makeText(this.activity, "You must fill in the previous box first", Toast.LENGTH_SHORT).show()
            return
        }

        currentImagePicker = viewIdToInt(view.id)

        val writePermission = ContextCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        //권한 확인
        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {

            // 권한 요청
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )

        } else {
            // 권한이 있는 경우 갤러리 실행
            val intent = Intent(Intent.ACTION_PICK)
            // intent의 data와 type을 동시에 설정하는 메서드
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )

            imageResult.launch(intent)
        }
    }
}