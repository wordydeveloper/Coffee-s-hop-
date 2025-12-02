package com.example.coffeeshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.AuthEvent
import com.example.coffeeshop.data.model.AuthState
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignupViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    private val _signupState = MutableLiveData<AuthState>()
    val signupState: LiveData<AuthState> = _signupState

    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()

    fun signup(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _signupState.value = AuthState.Error("Please fill in all fields.")
            return
        }

        _signupState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        updateUserProfile(firebaseUser, name)
                    } else {
                        _signupState.value = AuthState.Error("User not found after creation.")
                    }
                } else {
                    _signupState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
                }
            }
    }

    private fun updateUserProfile(firebaseUser: FirebaseUser, name: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserToFirestore(firebaseUser, name)
                } else {
                    _signupState.value = AuthState.Error("Failed to update profile.")
                }
            }
    }

    private fun saveUserToFirestore(firebaseUser: FirebaseUser, name: String) {
        val user = hashMapOf(
            "uid" to firebaseUser.uid,
            "name" to name,
            "email" to firebaseUser.email,
            "role" to "user"
        )

        firestore.collection("users").document(firebaseUser.uid)
            .set(user)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _signupState.value = AuthState.Authenticated(firebaseUser)
                    _events.send(AuthEvent.ShowToast("Account created successfully!"))
                    _events.send(AuthEvent.Navigate("login"))
                }
            }
            .addOnFailureListener { e ->
                viewModelScope.launch {
                    _signupState.value = AuthState.Error(e.message ?: "Firestore error")
                    _events.send(AuthEvent.ShowToast("Failed to save data"))
                }
            }
    }

    fun signupWithGoogle(credential: AuthCredential) {
        _signupState.value = AuthState.Loading

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        checkAndSaveGoogleUser(user)
                    }
                } else {
                    _signupState.value = AuthState.Error(task.exception?.message ?: "Google Signup Failed")
                }
            }
    }

    private fun checkAndSaveGoogleUser(user: FirebaseUser) {
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    // SI NO EXISTE: Es un registro nuevo. Guardamos los datos.
                    val userData = hashMapOf(
                        "uid" to user.uid,
                        "name" to (user.displayName ?: "Usuario Google"),
                        "email" to (user.email ?: ""),
                        "role" to "user"
                    )

                    firestore.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            _signupState.value = AuthState.Authenticated(user)
                            viewModelScope.launch {
                                _events.send(AuthEvent.ShowToast("Google Account created!"))
                                _events.send(AuthEvent.Navigate("home"))
                            }
                        }
                } else {
                    _signupState.value = AuthState.Authenticated(user)
                    viewModelScope.launch {
                        _events.send(AuthEvent.ShowToast("Welcome back!"))
                        _events.send(AuthEvent.Navigate("home"))
                    }
                }
            }
            .addOnFailureListener {
                _signupState.value = AuthState.Error("Database error")
            }
    }
}