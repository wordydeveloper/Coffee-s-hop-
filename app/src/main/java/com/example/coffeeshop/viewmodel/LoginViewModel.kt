package com.example.coffeeshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.AuthEvent
import com.example.coffeeshop.data.model.AuthState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging // Import required
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loginState = MutableLiveData<AuthState>()
    val loginState: LiveData<AuthState> = _loginState

    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = AuthState.Error("Email or password can't be empty.")
            return
        }

        _loginState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _loginState.value = AuthState.Authenticated(user!!)
                    updateFcmToken()
                } else {
                    _loginState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun loginWithGoogle(credential: AuthCredential) {
        _loginState.value = AuthState.Loading

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        checkIfUserExistsInFirestore(user)
                    }
                } else {
                    _loginState.value = AuthState.Error(task.exception?.message ?: "Google Login failed")
                }
            }
    }

    private fun checkIfUserExistsInFirestore(user: FirebaseUser) {
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Case 1: Returning User
                    _loginState.value = AuthState.Authenticated(user)
                    updateFcmToken()
                    triggerSuccessEvents()
                } else {
                    // Case 2: New User
                    val name = user.displayName ?: "Usuario Google"
                    saveGoogleUserToFirestore(user, name)
                }
            }
            .addOnFailureListener {
                _loginState.value = AuthState.Error("Error connecting to database")
            }
    }

    private fun saveGoogleUserToFirestore(user: FirebaseUser, name: String) {
        val userData = hashMapOf(
            "uid" to user.uid,
            "name" to name,
            "email" to (user.email ?: ""),
            "role" to "user"
        )

        firestore.collection("users").document(user.uid).set(userData)
            .addOnSuccessListener {
                _loginState.value = AuthState.Authenticated(user)
                updateFcmToken()
                triggerSuccessEvents()
            }
            .addOnFailureListener {
                _loginState.value = AuthState.Error("Failed to create profile")
            }
    }

    private fun triggerSuccessEvents() {
        viewModelScope.launch {
            _events.send(AuthEvent.ShowToast("Login Successful!"))
            _events.send(AuthEvent.Navigate("home"))
        }
    }

    fun updateFcmToken() {
        val uid = auth.currentUser?.uid ?: return

        // Ensure you have imported com.google.firebase.messaging.FirebaseMessaging
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                firestore.collection("users").document(uid)
                    .update("fcmToken", token)
            }
        }
    }
}