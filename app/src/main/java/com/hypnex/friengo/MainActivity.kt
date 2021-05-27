package com.hypnex.friengo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hypnex.friengo.auth.FirebaseSignIn
import com.hypnex.friengo.callbacks.AuthStateListener
import com.hypnex.friengo.data.models.User
import com.hypnex.friengo.databinding.ActivityMainBinding
import com.hypnex.friengo.fragments.BottomNavigationFragmentDirections
import com.hypnex.friengo.fragments.SignFragmentDirections
import com.hypnex.friengo.utils.toast
import com.hypnex.friengo.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val auth = FirebaseSignIn(this)

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var authFailedFragmentCallback: () -> Unit

    private val preferences = MainApplication.preferences

    private val authListener: AuthStateListener =
        object : AuthStateListener {
            override fun onAuthChanged(isLoggedIn: Boolean) {
                if (!isLoggedIn) {
                    preferences.removeUser()
                    navigateToLogin()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.isLoad.observe(this, {
            setLoading(it)
        })

        viewModel.toastMessage.observe(this, {
            toast(it)
        })
    }

    fun setLoading(isLoad: Int) {
        binding.mainProgressIndicator.visibility = isLoad
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthChangeListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthChangeListener(authListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                auth.handleSignInResult(data,
                    {
                        val user = auth.getUser()

                        val myUser = User(
                            user?.uid,
                            user?.displayName,
                            user?.displayName,
                            user?.photoUrl.toString(),
                            user?.email
                        )

                        preferences.saveUser(myUser)

                        viewModel.createUser(
                            myUser,
                            { navigateToBottomNavFragment() },
                            { navigateToBottomNavFragment() }
                        )
                    }, {
                        showErrorSignIn()
                    })
            } catch (e: Exception) {
                showErrorSignIn()
            }
        }
    }

    private fun navigateToBottomNavFragment() {
        val direction =
            SignFragmentDirections.actionSignFragmentToBottomNavigationFragment()

        findNavController(R.id.nav_host_fragment_container).navigate(direction)
    }

    private fun navigateToLogin() {
        val direction =
            BottomNavigationFragmentDirections.actionBottomNavigationFragmentToSignFragment()

        findNavController(R.id.nav_host_fragment_container).navigate(direction)
    }

    fun startSignIn(authFailedFragmentCallback: () -> Unit) {
        this.authFailedFragmentCallback = authFailedFragmentCallback
        auth.startSignIn(RC_GOOGLE_SIGN_IN)
    }

    fun signOut() {
        auth.signOut()
    }

    private fun showErrorSignIn() {
        authFailedFragmentCallback()

        toast(R.string.google_sign_up_failed)
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}