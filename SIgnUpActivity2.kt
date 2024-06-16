package com.example.task

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth


class SIgnUpActivity2 : AppCompatActivity() {
    private lateinit var signUp2Binding: ActivitySignUp2Binding
    private var fileName="shredPref"
    private var email="email"
    private var password="password"
    private var username="username"
    private var phoneNo="phoneNo"
    private var confirmpassword="confirm password"
    private lateinit var sharedPreferences: SharedPreferences
    private val auth= FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        signUp2Binding = ActivitySignUp2Binding.inflate(layoutInflater)
        var view = signUp2Binding.root

        signUp2Binding.signupButton.setOnClickListener {
            var intent = Intent(
                this@SIgnUpActivity2,
                MainActivity::class.java
            )
            startActivity(intent)

        }

        setContentView(view)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        sharedPreferences=getSharedPreferences(fileName, MODE_PRIVATE)
//        val fetchedEmail=sharedPreferences.getString(email,"")
//        val fetchedPassword=sharedPreferences.getString(password,"")
//        val fetchedUsername=sharedPreferences.getString(username,"")
//        val fetchedPhoneNo=sharedPreferences.getString(phoneNo,"")
//        val fetchedConfirmPassword=sharedPreferences.getString(confirmpassword,"")
//
//        signUp2Binding.signupButton.setOnClickListener {
//            val emailFieldValue=signUp2Binding.emailText.text.toString()
//            val pwdFieldValue=signUp2Binding.passwordText.text.toString()
//            val usernameFieldValue=signUp2Binding.userNameText.text.toString()
//            val phoneNoFieldValue=signUp2Binding.phoneNoText.text.toString()
//            val confirmPasswordFieldValue=signUp2Binding.confirmPasswordText.text.toString()
//
//            if (emailFieldValue.equals(fetchedEmail)&&pwdFieldValue.equals(fetchedPassword)){
//                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
//                        task ->
//                    if(task.isSuccessful){
//                        Toast.makeText(applicationContext,"User created",
//                            Toast.LENGTH_LONG).show()
//                    }else{
//                        Toast.makeText(applicationContext,task.exception?.message.toString(),
//                            Toast.LENGTH_LONG).show()
//                    }
//                }
//                Toast.makeText(this,"Login Sucessfully",Toast.LENGTH_LONG).show()
//            }
//        }

    }
}