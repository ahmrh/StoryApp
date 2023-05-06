package com.ahmrh.storyapp.ui.story

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.ahmrh.storyapp.databinding.FragmentAddStoryBinding
import com.ahmrh.storyapp.ui.main.MainActivity
import com.ahmrh.storyapp.ui.main.MainViewModel
import com.ahmrh.storyapp.ui.story.AddStoryFragment.Companion.CAMERA_X_RESULT
import java.io.File

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? =null
    private val binding get() = _binding!!

    private var getFile: File? =  null

    private val mainViewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val TAG = "AddStoryFragment"
    }

    private lateinit var fragmentManager: FragmentManager
    private var token: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUtil()
        setupPermission()

        setupAction()
    }

    private fun setupUtil(){
        fragmentManager = parentFragmentManager
        mainViewModel.getToken().observe(requireActivity()){
            token = it
            Log.d(TAG, "Token: $it")
        }
        mainViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }
    }
    private fun setupPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener{
            launcherIntentCameraX.launch(Intent(requireContext(), CameraActivity::class.java))
        }
        binding.btnGallery.setOnClickListener{
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
        binding.btnUpload.setOnClickListener{
            addStory()
        }
    }

    private fun addStory(){
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.edDescription.text.toString()
            if(token != null){
                val uploadSuccessLiveData = mainViewModel.uploadStory(file, description, token!!)
                uploadSuccessLiveData.observe(requireActivity()) { uploadSuccess ->
                    if (uploadSuccess) {
                        Toast.makeText(requireContext(), "Upload Success", Toast.LENGTH_SHORT).show()
                        fragmentManager.popBackStack()
                    } else{
                        Toast.makeText(requireContext(), "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "Who are you?", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill above data", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()

                fragmentManager.commit{
                    remove(this@AddStoryFragment)
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = myFile
                binding.imgPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                binding.imgPreview.setImageURI(uri)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}