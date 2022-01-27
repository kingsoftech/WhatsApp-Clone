package com.example.whatsappClone

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.whatsappClone.Firebase.RealTimeDatabase
import com.example.whatsappClone.databinding.ActivitySignUpBinding
import com.example.whatsappClone.databinding.CustomProgressDialogBinding
import com.example.whatsappClone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity(), View.OnClickListener{
    private var activitySignUpBinding: ActivitySignUpBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(activitySignUpBinding!!.root)
        supportActionBar!!.hide()
        activitySignUpBinding!!.btnSignup.setOnClickListener(this)
        activitySignUpBinding!!.tvAlreadyHaveAnAccount.setOnClickListener(this)
    }

    private fun showCustomDialog(){
        progressDialog = Dialog(this)
        var binding = CustomProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(binding.root)
        progressDialog.show()
    }

    private fun hideCustomDialog(){
        progressDialog.dismiss()
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.btn_signup ->{
                val userName = activitySignUpBinding?.etUsername?.text.toString().trim { it <= ' ' }
                val password = activitySignUpBinding?.etPassword?.text.toString().trim { it <= ' ' }
                val email = activitySignUpBinding?.etEmail?.text.toString().trim { it <= ' ' }

                when{
                    TextUtils.isEmpty(userName)->{
                        Toast.makeText(
                            this,
                            "enter your username",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    TextUtils.isEmpty(email)->{
                        Toast.makeText(
                            this,
                            "enter your email",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    TextUtils.isEmpty(password)->{
                        Toast.makeText(
                            this,
                            "enter your password",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    else->
                    {
                        showCustomDialog()
                        mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener {
                                task->
                                if(task.isSuccessful){
                                    val userId = task.result!!.user!!.uid
                                    val user = User(
                                         userName=userName,
                                        email = email,
                                        password = password,
                                        userId = userId

                                    )
                                   RealTimeDatabase().registerUser(user)
                                    hideCustomDialog()
                                    Toast.makeText(
                                        this,
                                        "you have successfully created you acount",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    activitySignUpBinding!!.etUsername.text.clear()
                                    activitySignUpBinding!!.etEmail.text.clear()
                                    activitySignUpBinding!!.etPassword.text.clear()
                                    signInUser(email, password)
                                }
                                else{
                                    hideCustomDialog()
                                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                                }
                            }

                    }
                }


            }
            R.id.tv_already_have_an_account ->
            {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun signInUser(email:String, password:String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                hideCustomDialog()
                if(task.isSuccessful){
                    hideCustomDialog()
                    val user =mAuth.currentUser
                    Toast.makeText(
                        this,
                        "you successfully signin",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign In", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }

            }
    }
}