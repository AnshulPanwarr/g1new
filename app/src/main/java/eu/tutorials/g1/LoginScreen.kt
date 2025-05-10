package eu.tutorials.g1

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var retryCount by remember { mutableStateOf(0) }
    var showRetryDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun handleLoginError(exception: Exception) {
        val errorMessage = when (exception) {
            is FirebaseNetworkException -> {
                retryCount++
                if (retryCount <= 3) {
                    showRetryDialog = true
                    "Network error. Attempting to reconnect..."
                } else {
                    "Network error. Please check your internet connection and try again."
                }
            }
            is FirebaseAuthInvalidUserException -> "No account found with this email."
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
            else -> "Login failed: ${exception.message}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    fun attemptLogin() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (!isNetworkAvailable(context)) {
                Toast.makeText(
                    context,
                    "No internet connection. Please check your network settings.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            isLoading = true
            
            // Try to sign in with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        retryCount = 0
                        navController.navigate(Screen.SportSelection.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        // If the error is related to reCAPTCHA, try to handle it
                        val exception = task.exception
                        if (exception?.message?.contains("reCAPTCHA") == true) {
                            // For emulator, try to sign in without reCAPTCHA
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { retryTask ->
                                    isLoading = false
                                    if (retryTask.isSuccessful) {
                                        retryCount = 0
                                        navController.navigate(Screen.SportSelection.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    } else {
                                        retryTask.exception?.let { handleLoginError(it) }
                                    }
                                }
                        } else {
                            isLoading = false
                            task.exception?.let { handleLoginError(it) }
                        }
                    }
                }
        } else {
            Toast.makeText(
                context,
                "Please fill in all fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (showRetryDialog) {
        AlertDialog(
            onDismissRequest = { showRetryDialog = false },
            title = { Text("Network Error") },
            text = { Text("Having trouble connecting. Would you like to retry?") },
            confirmButton = {
                Button(
                    onClick = {
                        showRetryDialog = false
                        scope.launch {
                            delay(1000) // Wait for 1 second before retrying
                            attemptLogin()
                        }
                    }
                ) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRetryDialog = false
                        retryCount = 0
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E), // Deep Blue
                        Color(0xFF0D47A1)  // Darker Blue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo and Name
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF64B5F6), // Light Blue
                                    Color(0xFF1976D2)  // Medium Blue
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "GO",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Text(
                text = "Game On",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Welcome Back! 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", fontSize = 16.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A237E),
                            focusedLabelColor = Color(0xFF1A237E)
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            color = Color(0xFF1A237E)
                        ),
                        enabled = !isLoading
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", fontSize = 16.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A237E),
                            focusedLabelColor = Color(0xFF1A237E)
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            color = Color(0xFF1A237E)
                        ),
                        enabled = !isLoading
                    )

                    Button(
                        onClick = { attemptLogin() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A237E)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                "Login",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    TextButton(
                        onClick = { 
                            if (!isLoading) {
                                navController.navigate(Screen.Register.route)
                            }
                        },
                        enabled = !isLoading
                    ) {
                        Text(
                            "Don't have an account? Sign Up",
                            color = Color(0xFF1A237E),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
