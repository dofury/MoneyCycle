package com.dofury.moneycycle.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.databinding.ActivityLoginBinding
import com.dofury.moneycycle.dto.User
import com.dofury.moneycycle.fragment.SettingFragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var signInButton: SignInButton
    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        buttonEvent()
        signInButton = binding.signInButton
        signInButton.setOnClickListener {
            signIn()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("759618820171-pu745d0iistbba8tjgmgpqeesgecfmom.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun buttonEvent(){
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (task!!.isSuccess) {
                val account = task.signInAccount
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken.toString(),this)
                }
            } else {
                Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String,context: Context) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    MyApplication.prefs.setBoolean("auto_login",true)
                    MyApplication.prefs.setString("id_token",idToken)
                    updateUI(user,context)
                } else {
                    //Toast.makeText(this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?,context: Context) {
        if (user != null) {
            finish()
            val users = User(user.getIdToken(true).toString(),user.email!!,user.displayName!!,user.providerId)
            SettingFragment.user = users
            SettingFragment.userInit()
            // 로그인 성공
            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
        } else {
            // 로그인 실패
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}



