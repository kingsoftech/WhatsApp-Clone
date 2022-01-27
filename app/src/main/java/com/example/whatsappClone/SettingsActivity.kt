package com.example.whatsappClone

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import com.example.whatsappClone.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private var activitySettingsBinding: ActivitySettingsBinding? = null

//    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            result ->
//        if(result.resultCode == RESULT_OK && result.data!=null)
//        {
//            //setting the resulting image to the image View
//            val imageUri1 = result.data?.data!!
//
////            val storageRef = storage.reference.child("profile image")
////                .child(mAuth.uid!!)
////                storageRef.putFile(imageUri1).addOnSuccessListener {
////                    storageRef.downloadUrl.addOnSuccessListener{
////                        dataBase.reference.child("Users").child(mAuth.uid!!)
////                            .child("profileImage").setValue(it.toString())
////
////                    }
//     //           }
//
//            //val bitmap2: Bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUril!!))
//            // val bitmap2:Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri1);
//
//
//
//        }
//    }
//    private val storageAndCameraPermission: ActivityResultLauncher<Array<String>> =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()){
//                permissions ->
//            permissions.entries.forEach{
//                val permissionName = it.key
//                val isGranted = it.value
//                if(isGranted){
//                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
//                        Toast.makeText(
//                            this,
//                            "permission granted to Read enternal Storage",
//                            Toast.LENGTH_SHORT)
//                            .show()
//                        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                        openGalleryResultLauncher.launch(pickIntent)
//                    }
//
//
//                }
//                else{
//                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
//                        Toast.makeText(
//                            this,
//                            "permission not granted to read external storage",
//                            Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//                }
//            }
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding!!.root)
        supportActionBar!!.hide()
//        storage = FirebaseStorage.getInstance()
//        mAuth = FirebaseAuth.getInstance()
//        dataBase = FirebaseDatabase.getInstance()
            activitySettingsBinding!!.ivBackArrow.setOnClickListener {
                onBackPressed()
                finish()
            }
        //dataBase.reference.child("Users").child(mAuth.uid!!)
        activitySettingsBinding!!.ivPlus.setOnClickListener {
          // launchStoragePermission()

        }


    }
//    private fun launchStoragePermission() {
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M &&
//            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
//            &&shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
//        {
//            showRationaleDialog(" Permission Demo requires storage access",
//                "camera cannot be used because location storage denied")
//        }
//        else
//        {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                storageAndCameraPermission.launch(
//                    arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE)
//                )
//            }
//        }
//
//    }
//  private  fun showRationaleDialog(
//        title:String,
//        message: String
//    ){
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder.setTitle(title)
//            .setMessage(message)
//            .setPositiveButton("Cancel"){dialog, _->
//                dialog.dismiss()
//            }
//        builder.create().show()
//
//    }
}