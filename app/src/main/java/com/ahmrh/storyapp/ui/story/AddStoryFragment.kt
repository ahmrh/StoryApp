package com.ahmrh.storyapp.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.databinding.FragmentAddStoryBinding
import com.ahmrh.storyapp.databinding.FragmentDetailStoryBinding
import java.io.File

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? =null
    private val binding get() = _binding!!
    private lateinit var fragmentManager: FragmentManager

    private var getFile: File? =  null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
            startActivity(Intent(requireContext(), CameraActivity::class.java))
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



}