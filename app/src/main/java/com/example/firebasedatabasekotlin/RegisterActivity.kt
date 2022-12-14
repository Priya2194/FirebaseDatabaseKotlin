package com.example.firebasedatabasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.name)
        val contact = findViewById<EditText>(R.id.contact)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("user")
        signup.setOnClickListener() {
            signupUser()
        }
    }
   private fun signupUser() {
    if (email.text.toString().isEmpty()) {
        email.error = "Please enter email"
        email.requestFocus()
        return
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
        email.error = "Please enter valid email"
        email.requestFocus()
        return
    }
    if (password.text.toString().isEmpty()) {
        password.error = "Please enter password"
        password.requestFocus()
        return
    }
       if (password.length() < 8) {
           password.error = "Minimum length of password is 8"
           password.requestFocus();
           return;
       }
    auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
// Sign in success, update UI with the signed-in user's information
                val id = database.push().key
                val user = User(name.text.toString(), email.text.toString(), contact.text.toString(),
                    password.text.toString())
                database.child("users").child(id.toString()).setValue(user)

                Toast.makeText(this, "User Register Successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,LoginActivity::class.java))
            } else {
// If sign in fails, display a message to the user.
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
}
}

