package com.example.coffeeshop.data.repository


import com.example.coffeeshop.data.remote.FirebaseConfig
import com.example.coffeeshop.domain.repository.AuthResult
import com.example.coffeeshop.domain.repository.IAuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository : IAuthRepository {

    private val auth = FirebaseConfig.auth
    private val firestore = FirebaseConfig.firestore

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                updateFcmToken(it.uid)
                AuthResult.Success(it)
            } ?: AuthResult.Error("Authentication failed")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun signup(name: String, email: String, password: String): AuthResult {
        return try {
            // Crear usuario
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult.Error("User creation failed")

            // Actualizar perfil
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()

            // Guardar en Firestore
            val userData = hashMapOf(
                "uid" to user.uid,
                "name" to name,
                "email" to email,
                "role" to "user",
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection(FirebaseConfig.Collections.USERS)
                .document(user.uid)
                .set(userData)
                .await()

            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Signup failed")
        }
    }

    override suspend fun loginWithGoogle(credential: AuthCredential): AuthResult {
        return try {
            val result = auth.signInWithCredential(credential).await()
            val user = result.user ?: return AuthResult.Error("Google login failed")

            // Verificar si el usuario ya existe en Firestore
            val userDoc = firestore.collection(FirebaseConfig.Collections.USERS)
                .document(user.uid)
                .get()
                .await()

            if (!userDoc.exists()) {
                // Crear nuevo usuario
                val userData = hashMapOf(
                    "uid" to user.uid,
                    "name" to (user.displayName ?: "Usuario"),
                    "email" to (user.email ?: ""),
                    "role" to "user",
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection(FirebaseConfig.Collections.USERS)
                    .document(user.uid)
                    .set(userData)
                    .await()
            }

            updateFcmToken(user.uid)
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Google login failed")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun updateFcmToken(token: String) {
        try {
            val uid = currentUser?.uid ?: return
            firestore.collection(FirebaseConfig.Collections.USERS)
                .document(uid)
                .update("fcmToken", token)
                .await()
        } catch (e: Exception) {
            // Log error but don't crash
            e.printStackTrace()
        }
    }

    override fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }
}