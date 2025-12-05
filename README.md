# Coffee Shop – Android App

Aplicación móvil desarrollada con Kotlin + Jetpack Compose, integrada con Firebase Authentication, Firestore y Google Sign-In, diseñada para gestionar pedidos de un café de forma rápida y moderna.



Clonar el proyecto
https://github.com/wordydeveloper/Coffee-s-hop-.git

# Instalacion
Requisitos

Android Studio Ladybug 2024.2.1+

JDK 21

SDK mínimo API 24+

Proyecto en Firebase
Configuración de Firebase

# Crear proyecto en Firebase

Registrar app Android

Paquete: com.example.coffeeshop

Descargar google-services.json → moverlo a /app/
Activar servicios

Authentication (Email/Password y Google)

Firestore Database (modo prueba)
rules_version = '2';
service cloud.firestore {
match /databases/{database}/documents {

    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    match /orders/{orderId} {
      allow read: if request.auth != null && resource.data.userId == request.auth.uid;
      allow create: if request.auth != null && request.resource.data.userId == request.auth.uid;
      allow update, delete: if request.auth != null && resource.data.userId == request.auth.uid;
    }

    match /products/{id} {
      allow read: if true;
      allow write: if false;
    }
  }
}

Configurar Google Sign-In
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey \
  -storepass android -keypass android
Agregar el SHA-1 en Firebase → Project Settings.
# Compilar e instalar
./gradlew clean
./gradlew build
./gradlew installDebug

#link del video explicativo
https://youtu.be/UfjCm6KFKrA?si=E5AeT1s4hn8Z2Pya
