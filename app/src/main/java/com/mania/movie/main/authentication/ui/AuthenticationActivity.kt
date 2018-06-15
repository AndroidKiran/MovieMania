package com.mania.movie.main.authentication.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mania.movie.BR
import com.mania.movie.R
import com.mania.movie.databinding.ActivityAuthenticationBinding
import com.mania.movie.main.authentication.presenter.AuthenticationViewModel
import com.mania.movie.main.base.BaseActivity
import javax.inject.Inject
import com.google.android.gms.common.api.ApiException
import com.mania.movie.binding.SnackbarConfiguration
import com.mania.movie.extension.showSnackBar


class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding, AuthenticationViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var authenticationViewModel: AuthenticationViewModel

    private lateinit var activityAuthenticationBinding: ActivityAuthenticationBinding

    private var googleSignInClient: GoogleSignInClient? = null

    override fun getViewModel() =
            ViewModelProviders.of(this@AuthenticationActivity, viewFactory)
                    .get(AuthenticationViewModel::class.java).also {
                        authenticationViewModel = it
                    }

    override fun getLayoutId() = R.layout.activity_authentication

    override fun executePendingVariablesBinding() {
        getViewDataBinding().also {
            it.setVariable(BR.authenticationViewModel, authenticationViewModel)
            it.setVariable(BR.snackBarConfig, snackBarConfiguration)
            activityAuthenticationBinding = it
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        googleSignInClient = getGoogleSignInClient()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                account.photoUrl?.let {
                    authenticationViewModel.persistUrlPhoto(it.toString())
                }

                account.id?.let {
                    authenticationViewModel.persistAccountId(it)
                }

                authenticationViewModel.persistLoginState(true)

                setResult(Activity.RESULT_OK)
                finish()
            } catch (e: ApiException) {
                snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg),
                        SnackbarConfiguration.Type.NEUTRAL,
                        Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun initUI() {
        activityAuthenticationBinding.btnSignIn.setOnClickListener {
            startActivityForResult(googleSignInClient?.signInIntent, RC_SIGN_IN)
        }

        activityAuthenticationBinding.btnSkip.setOnClickListener {
            finish()
        }

        activityAuthenticationBinding.btnLogout.setOnClickListener {
            signOut()
        }

    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun signOut() {
        googleSignInClient?.signOut()?.addOnCompleteListener(this, {
            if (it.isSuccessful) {
                authenticationViewModel.persistUrlPhoto("")
                authenticationViewModel.persistLoginState(false)

                setResult(Activity.RESULT_OK)
                finish()
            } else {
                snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg),
                        SnackbarConfiguration.Type.NEUTRAL,
                        Snackbar.LENGTH_SHORT)
            }
        })
    }


    companion object {
        private const val RC_SIGN_IN = 9001
        fun start(context: Context) = Intent(context, AuthenticationActivity::class.java)
    }
}