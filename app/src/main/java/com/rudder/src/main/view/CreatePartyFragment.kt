package com.rudder.src.main.view

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.databinding.FragmentCreatePartyBinding
import com.rudder.src.main.viewmodel.CreatePartyViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.util.*

class CreatePartyFragment : Fragment() {
    private lateinit var binding: FragmentCreatePartyBinding

    private val viewModel: CreatePartyViewModel by viewModels()
    private lateinit var mContext: Context

    fun setActivityAction()  {
        (requireActivity() as MainActivity).fragmentCreated = { requestCode, resultCode, data ->
            Log.d("came here","came here")


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                Log.d("crop Ended", "Yeah")
                val cropResult = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {







                    val imageUri = cropResult.uri
                    imageUri?.let {
                        //val imageFile = File(getRealPathFromURI(it))
                        val imageFile = File(it.toString())
                        val cr: ContentResolver = context?.contentResolver!!

                        viewModel.setImage(imageFile, cr.getType(it).toString())

                        Glide.with(binding.selectedPhotoIV.context)
                            .load(imageUri)
                            .override(800,800)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.selectedPhotoIV)
                    }
                    Toast.makeText(requireActivity(), "Photo pick success", Toast.LENGTH_SHORT).show()
                }
            }

            //get your "requestCode" here with switch for "SomeUniqueID"
        }
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val tmpUri = result.data?.data
            tmpUri?.let {

                Log.d("came ImageResult","ImageResult")
                cropImage(it)
            }
        }

    }

    private fun cropImage(uri: Uri?){
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            //.setGuidelines(CropImageView.Guidelines.ON).setMinCropResultSize(500, 500).setMaxCropResultSize(500, 500)
            .setGuidelines(CropImageView.Guidelines.ON).setFixAspectRatio(true)
            //사각형 모양으로 자른다
            .start(this.requireActivity())
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreatePartyBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.createPartyFragment = this

        setActivityAction()

        val partyMemberCountList = listOf(5,6,7,8,9,10)
        val departmentASpinnerAdapter = ArrayAdapter(mContext, R.layout.custom_spinner_layout, partyMemberCountList)
        binding.partyMemberCountS.adapter = departmentASpinnerAdapter

        binding.partyDateDP.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.setCalendarTime(year,monthOfYear,dayOfMonth)
        }
        binding.partyDateTP.setOnTimeChangedListener { view, hourOfDay, minute ->
            viewModel.setCalendarTime(hourOfDay,minute)
        }


        viewModel.toastMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(mContext,it,Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    fun onSpinnerSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.setPartyMemberCount(parent.selectedItem as Int)
    }

    private fun getRealPathFromURI(uri: Uri): String {

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

    fun selectGallery(view: View) {


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