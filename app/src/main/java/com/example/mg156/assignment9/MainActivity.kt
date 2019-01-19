package com.example.mg156.assignment9

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.util.*

class MainActivity : AppCompatActivity(), registerFragment.OnFragmentInteractionListenerRegister, loginFragment.OnFragmentInteractionListenerLogin {

    private val TAG = "EmailPassword"
    lateinit var recFragment: Fragment


    // [START declare_auth]
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLoggedIn()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            recFragment = loginFragment.newInstance(R.id.login_fragment_layout.toString(), "")
        }
        mAuth = FirebaseAuth.getInstance();
        supportFragmentManager.beginTransaction().replace(R.id.main_acitivity_container,
                recFragment).commit()
    }

    private fun isLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid

        if(uid != null){
            val intent = Intent(this,HomeActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(applicationContext!!.contentResolver,selectedPhotoUri)
            val profile_image = findViewById<ImageView>(R.id.register_page_image)
            profile_image.setImageBitmap(bitmap)
        }
    }

    override fun onFragmentInteractionRegister(v: View) {
        when (v.id) {
            R.id.register_page_image->{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,0)
            }
            R.id.register_page_sign_up_button -> {
                val editUserID = findViewById<EditText>(R.id.register_page_user_id)
                val editEmail = findViewById<EditText>(R.id.register_page_email)
                val editPassword = findViewById<EditText>(R.id.register_page_password)
                val editConfirmPassword = findViewById<EditText>(R.id.register_page_confirm_password)


                var userID = editUserID.text.toString()
                var email = editEmail.text.toString()
                var password = editPassword.text.toString()
                var confirmPassword = editConfirmPassword.text.toString()

                if (TextUtils.isEmpty(userID)) {
                    editUserID.setError("Enter User ID!")
                    editUserID.requestFocus()
                    return
                }

                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Enter email address!")
                    editEmail.requestFocus()
                    return
                }

                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Enter password!")
                    editPassword.requestFocus()
                    return
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    editConfirmPassword.setError("Enter confirm password!")
                    editConfirmPassword.requestFocus()
                    return
                }

                if (password.length < 6) {
                    editPassword.setError("Password too short, enter minimum 6 characters!")
                    editPassword.requestFocus()
                    return
                }

                if (password != confirmPassword) {
                    editConfirmPassword.setError("Password did not match!")
                    editConfirmPassword.requestFocus()
                    return
                }

                if(selectedPhotoUri == null){
                    Toast.makeText(baseContext, "Please select your profile image",
                            Toast.LENGTH_LONG).show()
                    return
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                Toast.makeText(baseContext, "Authentication Success.",
                                        Toast.LENGTH_LONG).show()
                                uploadImageToFirebaseStorage()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                        Toast.LENGTH_LONG).show()
                            }
                        }
            }
            R.id.register_page_sign_in_button -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.main_acitivity_container, loginFragment.newInstance(R.id.login_fragment_layout.toString(), ""))
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("SignUp", "Successfully uploaded image")
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener{
                    Log.d(" SignUp ", "Failed to upload image to storage : ${it.message}")
                }
    }

    class User(val uid: String, val username: String, val useremail: String, val profileImageUrl: String){
        constructor():this("","","","")
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val editUserID = findViewById<EditText>(R.id.register_page_user_id)
        val editEmail = findViewById<EditText>(R.id.register_page_email)
        val user = User(uid,editUserID.text.toString(),editEmail.text.toString(),profileImageUrl)

        ref.setValue(user).addOnSuccessListener {
            val intent = Intent(applicationContext,HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    override fun onFragmentInteractionLogin(v: View) {
        when (v.id) {
            R.id.login_page_btn_signup -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.main_acitivity_container, registerFragment.newInstance(R.id.register_fragment_layout.toString(), ""))
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            R.id.login_page_btn_login -> {
                val editEmail = findViewById<EditText>(R.id.login_page_email)
                val editPassword = findViewById<EditText>(R.id.login_page_password)

                var email = editEmail.text.toString()
                var password = editPassword.text.toString()

                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Enter email address!")
                    editEmail.requestFocus()
                    return
                }

                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Enter password!")
                    editPassword.requestFocus()
                    return
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = mAuth.currentUser
                                val intent = Intent(this, HomeActivity::class.java)
                                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                if (password.length < 6) {
                                    editPassword.setError(getString(R.string.minimum_password))
                                    editPassword.requestFocus()
                                } else {
                                    Toast.makeText(applicationContext, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        .addOnFailureListener{
                            Toast.makeText(applicationContext, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
            }
        }
    }
}
