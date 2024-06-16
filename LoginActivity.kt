package com.example.task

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    private var fileName="shredPref"
    private var email="username"
    private var password="password"
    private lateinit var sharedPreferences:SharedPreferences
    var auth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        var view=loginBinding.root

        loginBinding.forgetPassword.setOnClickListener {
            var intent = Intent(this@LoginActivity,
                ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        loginBinding.registernow.setOnClickListener {
            var intent = Intent(this@LoginActivity,
              SIgnUpActivity2::class.java)
            startActivity(intent)
        }


        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedPreferences=getSharedPreferences(fileName, MODE_PRIVATE)
        val fetchedEmail=sharedPreferences.getString(email,"")
        val fetchedPassword=sharedPreferences.getString(password,"")

        loginBinding.loginButton.setOnClickListener {
            val emailFieldValue=loginBinding.emailAddress.text.toString()
            val pwdFieldValue=loginBinding.password.text.toString()

            if (emailFieldValue.equals(fetchedEmail)&&pwdFieldValue.equals(fetchedPassword)){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        task ->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext,"User created",
                            Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(applicationContext,task.exception?.message.toString(),
                            Toast.LENGTH_LONG).show()
                    }
                }
                Toast.makeText(this,"Login Sucessfully",Toast.LENGTH_LONG).show()
            }
        }
    }
}