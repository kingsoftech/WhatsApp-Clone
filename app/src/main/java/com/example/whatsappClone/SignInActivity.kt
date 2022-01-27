package com.example.whatsappClone

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.whatsappClone.Firebase.RealTimeDatabase
import com.example.whatsappClone.databinding.ActivitySigninBinding
import com.example.whatsappClone.databinding.CustomProgressDialogBinding
import com.example.whatsappClone.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity(),View.OnClickListener {
    private var googleSignInResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode == RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("google Mail", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("", "Google sign in failed", e)
            }
        }
    }
    private  var activitySignInBinding: ActivitySigninBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog:Dialog
    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySignInBinding = ActivitySigninBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContentView(activitySignInBinding?.root)
        supportActionBar!!.hide()
        activitySignInBinding!!.btnSignIn.setOnClickListener(this)
        activitySignInBinding!!.tvClickToSignUp.setOnClickListener(this)
        activitySignInBinding!!.btnGoogle.setOnClickListener(this)
        if(mAuth.currentUser != null){
           successFullySignIn()
        }


    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        val user = mAuth.currentUser
                        Toast.makeText(this, "signIn with Google",Toast.LENGTH_SHORT).show()
                        val users = User()
                        users.userId = user!!.uid
                        users.userName = user.displayName!!
                        users.profileImage = user.photoUrl.toString()
                        RealTimeDatabase().registerUser(users)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.exception)

                    }
                }
    }

    private fun successFullySignIn() {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signInRegisteredUser(){
        val email = activitySignInBinding!!.etEmail.text.toString().trim{it <= ' '}
        val password = activitySignInBinding!!.etPassword.text.toString().trim{ it <= ' '}
        if(validateForm(email, password))
        {
        showCustomDialog()
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                hideCustomDialog()
                if(task.isSuccessful){
                    hideCustomDialog()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    val user =mAuth.currentUser
                Toast.makeText(
                    this,
                    "you successfully signin",
                    Toast.LENGTH_SHORT
                ).show()

                    successFullySignIn()

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

    private fun showCustomDialog(){
        progressDialog = Dialog(this)
        val binding = CustomProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(binding.root)
        progressDialog.show()
    }

    private fun hideCustomDialog(){
        progressDialog.dismiss()
    }
    private fun validateForm( email: String, password: String): Boolean {
        return when {

            TextUtils.isEmpty(email) -> {
                Toast.makeText(
                    this,
                    "enter your email",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(
                    this,
                    "enter your password",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
}

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_sign_in ->
            {
                signInRegisteredUser()
            }
            R.id.tv_click_to_sign_up ->
            {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)

            }
            R.id.btn_google->
            {
                signIn()
            }
        }
    }
private fun signIn(){
    val signInIntent = googleSignInClient.signInIntent
    googleSignInResultLauncher.launch(signInIntent)

}
}