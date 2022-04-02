package com.gq.loginsharing.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.gq.basic.common.GsonCommon
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import javax.inject.Inject


class TPLSGoogleSignIn @Inject constructor(
    @ActivityContext var activity: Context
) {

    private var registerForActivityResult: ActivityResultLauncher<Intent>? = null

    fun googleSignIn(): Intent {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(activity, options)

        val signInIntent = mGoogleSignInClient.signInIntent
        return signInIntent
    }


    fun registerLauncherForActivityResult(callback: (ActivityResult) -> Unit) {
        registerForActivityResult =
            (activity as ComponentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { ar: ActivityResult? ->
                if (ar?.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(ar.data)
                    try {
                        val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                        Timber.i(GsonCommon.gson.toJson(account))
                        val acct = GoogleSignIn.getLastSignedInAccount(activity)
                        if (acct != null) {
                            val personName = acct.displayName
                            val personGivenName = acct.givenName
                            val personFamilyName = acct.familyName
                            val personEmail = acct.email
                            val personId = acct.id
                            val personPhoto: Uri? = acct.photoUrl
                            Timber.i(personName)
                        }
                        callback(ar)
                    } catch (e: ApiException) {
                        Timber.e(e)
                    }
                }
            }
    }
}