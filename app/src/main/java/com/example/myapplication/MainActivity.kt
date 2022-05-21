package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
const val RC_SIGN_IN = 123

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signBtn = findViewById(R.id.sign_in_button) as SignInButton
        val messsage = findViewById(R.id.tv_name) as TextView
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signBtn.visibility = View.VISIBLE
        messsage.visibility = View.GONE
        signBtn.setSize(SignInButton.SIZE_STANDARD)
        signBtn.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            signBtn.visibility = View.VISIBLE
            messsage.text = acct.displayName
            messsage.visibility = View.GONE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val signBtn = findViewById(R.id.sign_in_button) as SignInButton
            val messsage = findViewById(R.id.tv_name) as TextView

            // Signed in successfully, show authenticated UI.
            signBtn.visibility = View.VISIBLE
            messsage.text = account.displayName
            messsage.visibility = View.GONE
        } catch (e: ApiException) {
            val signBtn = findViewById(R.id.sign_in_button) as SignInButton
            val messsage = findViewById(R.id.tv_name) as TextView
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            signBtn.visibility = View.VISIBLE
            messsage.text = ""
            messsage.visibility = View.GONE
        }
    }
}