package com.example.whatsappClone

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.whatsappClone.databinding.ActivitySettings2Binding
import com.example.whatsappClone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class ActivitySettings : AppCompatActivity() {

    var activitySettings2Binding:ActivitySettings2Binding? = null
    private lateinit var storage:FirebaseStorage
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

//activity result launcher which returns image Uri from mediaStore
        private val openGalleryResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK && result.data!=null)
        {
            //setting the resulting image to the image View
            val imageUri1 = result.data?.data!!
            //storing the image on the database Storage under the folder profile image->userID
            val storageRef = storage.reference.child("profile image")
                .child(mAuth.uid!!)
                storageRef.putFile(imageUri1).addOnSuccessListener {
                    //if it is successfully saved on the storage then get the download url and put it in the
                    //database
                    storageRef.downloadUrl.addOnSuccessListener{
                        database.reference.child("Users").child(mAuth.uid!!)
                            .child("profileImage").setValue(it.toString())

                    }
                }

            //val bitmap2: Bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUril!!))
            // val bitmap2:Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri1);



        }
    }
    //checking for the storage permission
    private val storageAndCameraPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if(isGranted){
                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(
                            this,
                            "permission granted to Read enternal Storage",
                            Toast.LENGTH_SHORT)
                            .show()
                        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        openGalleryResultLauncher.launch(pickIntent)
                    }


                }
                else{
                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(
                            this,
                            "permission not granted to read external storage",
                            Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettings2Binding = ActivitySettings2Binding.inflate(layoutInflater)
        setContentView(activitySettings2Binding!!.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
        activitySettings2Binding!!.ivBackArrow.setOnClickListener {
            onBackPressed()
            finish()
        }
        activitySettings2Binding!!.btnSave.setOnClickListener{
            val status = activitySettings2Binding!!.etStatus.text.toString()
            val userName = activitySettings2Binding!!.etUsername.text.toString()
            when {
                TextUtils.isEmpty(status)->
                {
                    Toast.makeText (this, "Profile status is empty", Toast.LENGTH_SHORT)
                        .show()
                }
                TextUtils.isEmpty(userName)-> {
                    Toast.makeText (this, "Profile userName is empty", Toast.LENGTH_SHORT)
                        .show()
                }
                else-> {
                    val hashMap = HashMap<String, Any>()
                    hashMap["userName"] = userName
                    hashMap["status"] = status
                    database.reference.child("Users").child(mAuth.uid!!)
                        .updateChildren(hashMap)
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }
        database.reference.child("Users").child(mAuth.uid!!).addListenerForSingleValueEvent(
            object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<User>()!!

                    Glide
                        .with(this@ActivitySettings)
                        .load(user.profileImage)
                        .centerCrop()
                        .placeholder(R.drawable.avatar3)
                        .into(activitySettings2Binding!!.ivUserProfileImage)
                    activitySettings2Binding!!.etStatus.setText(user.status)
                    activitySettings2Binding!!.etUsername.setText(user.userName)

                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(this@ActivitySettings, "error loading image", Toast.LENGTH_LONG).show()
                }

            }
        )
        //dataBase.reference.child("Users").child(mAuth.uid!!)
        activitySettings2Binding!!.ivPlus.setOnClickListener {
            launchStoragePermission()

        }

    }

        private fun launchStoragePermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M &&
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            &&shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            showRationaleDialog(" Permission Demo requires storage access",
                "camera cannot be used because location storage denied")
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                storageAndCameraPermission.launch(
                    arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            }
        }

    }
  private  fun showRationaleDialog(
        title:String,
        message: String
    ){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _->
                dialog.dismiss()
            }
        builder.create().show()

    }
}