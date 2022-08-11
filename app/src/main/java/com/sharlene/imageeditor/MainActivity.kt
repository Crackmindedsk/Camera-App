package com.sharlene.imageeditor

import android.Manifest
import android.Manifest.permission.CAMERA
import android.R.attr
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File


class MainActivity : AppCompatActivity() {

    private val cameraRequest =1888
    lateinit var imageView: ImageView
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    private var mUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val SELECT_PICTURE = 100
    private val REQUEST_PICK_IMAGE = 2
    private val REQUEST_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Kotlin App"
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                CAMERA
            ) == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), cameraRequest)
        imageView = findViewById(R.id.cropimg)
        val btn: Button = findViewById(R.id.cropbtn)
        cameraPermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        btn.setOnClickListener {

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Choose")
            dialog.setMessage("Choose between Gallery and Camera")
            dialog.setNeutralButton("Cancel"){
                dialogInterface, w->
                finish()
            }
            dialog.setNegativeButton("Gallery"){
                dialogInterface,w-> openGallery()
            }
            dialog.setPositiveButton("Camera"){
                dialogInterface,v-> openCamera()
            }

            dialog.create().show()
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(cameraIntent,cameraRequest)
//            openGallery()
//            openCamera()

        }
    }
    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
        }
        else if (requestCode == REQUEST_PICK_IMAGE) {
            val uri = data?.data
            imageView.setImageURI(uri)
        }
    }

}