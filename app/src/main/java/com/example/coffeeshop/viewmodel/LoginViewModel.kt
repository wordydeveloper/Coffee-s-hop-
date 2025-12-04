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
import com.google.firebase.messaging.FirebaseMessaging
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
            _loginState.value = AuthState.Error("El email o contraseña no pueden estar vacíos")
            return
        }

        _loginState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        _loginState.value = AuthState.Authenticated(user)
                        updateFcmToken()

                        // ✅ ENVIAR EVENTOS DE ÉXITO
                        viewModelScope.launch {
                            _events.send(AuthEvent.ShowToast("¡Inicio de sesión exitoso!"))
                            _events.send(AuthEvent.Navigate("home"))
                        }
                    }
                } else {
                    val errorMessage = when {
                        task.exception?.message?.contains("password") == true ->
                            "Contraseña incorrecta"
                        task.exception?.message?.contains("user") == true ->
                            "Usuario no encontrado"
                        else -> task.exception?.message ?: "Error al iniciar sesión"
                    }
                    _loginState.value = AuthState.Error(errorMessage)
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
                    _loginState.value = AuthState.Error(
                        task.exception?.message ?: "Error con Google Sign-In"
                    )
                }
            }
    }

    private fun checkIfUserExistsInFirestore(user: FirebaseUser) {
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Usuario existente
                    _loginState.value = AuthState.Authenticated(user)
                    updateFcmToken()
                    triggerSuccessEvents()
                } else {
                    // Usuario nuevo desde Google
                    val name = user.displayName ?: "Usuario Google"
                    saveGoogleUserToFirestore(user, name)
                }
            }
            .addOnFailureListener {
                _loginState.value = AuthState.Error("Error al conectar con la base de datos")
            }
    }

    private fun saveGoogleUserToFirestore(user: FirebaseUser, name: String) {
        val userData = hashMapOf(
            "uid" to user.uid,
            "name" to name,
            "email" to (user.email ?: ""),
            "role" to "user",
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users").document(user.uid).set(userData)
            .addOnSuccessListener {
                _loginState.value = AuthState.Authenticated(user)
                updateFcmToken()
                triggerSuccessEvents()
            }
            .addOnFailureListener {
                _loginState.value = AuthState.Error("Error al crear el perfil")
            }
    }

    private fun triggerSuccessEvents() {
        viewModelScope.launch {
            _events.send(AuthEvent.ShowToast("¡Inicio de sesión exitoso!"))
            _events.send(AuthEvent.Navigate("home"))
        }
    }

    private fun updateFcmToken() {
        val uid = auth.currentUser?.uid ?: return

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                firestore.collection("users").document(uid)
                    .update("fcmToken", token)
            }
        }
    }
}