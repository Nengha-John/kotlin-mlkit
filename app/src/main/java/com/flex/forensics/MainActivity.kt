package com.flex.forensics

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Space
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flex.forensics.ui.theme.ForensicsTheme
import java.util.ArrayList

class MainActivity : ComponentActivity() {
    var recognizedText =  mutableStateOf("")
    val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val values = result.data?.getStringExtra("recognized_text")
                Log.d(TAG,"Updated text intent $values")
                recognizedText.value = values?: "No text"
            }
        }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var recognizedText: String = ""
        setContent {
            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // Add padding to the entire column
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Flex KYC Module",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(bottom = 16.dp) // Add padding below the Text
                        )

                        Spacer(modifier = Modifier.height(24.dp)) //
                        Greetings(name = "User", onNavigate = {
//                            val intent = Intent(this@MainActivity, StillImageActivity::class.java)
//                            startActivityForResult(intent, REQUEST_CODE_TEXT_RECOGNITION)
                           launchProcessor()

                        }, recognizedText = recognizedText.value)
                    }
                }
            }
        }

        // Handle onActivityResult in the Activity (outside of setContent)
        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }
    }

    private fun launchProcessor(){

        intentLauncher.launch(Intent(this, StillImageActivity::class.java))
    }

    private fun allRuntimePermissionsGranted(): Boolean {

        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val permissionsToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }

    companion object {
        private const val TAG = "EntryChoiceActivity"
        private const val PERMISSION_REQUESTS = 1
        private const val REQUEST_CODE_TEXT_RECOGNITION = 100001


        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE_TEXT_RECOGNITION && resultCode == Activity.RESULT_OK) {
            val newRecognizedText = data?.getStringExtra("recognized_text") ?: ""
            // Update the recognizedText variable with the new value
            runOnUiThread { setContent { recognizedText.value = newRecognizedText } }
//            recognizedText.value = newRecognizedText
            Log.d(TAG, "Recognized text in Main Activity ${recognizedText.value}")
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            
        }
    }


}

//@Composable
//fun MyApp() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "home") {
//        composable("home"){
//                Greetings(name = "Android app with Kotlin" , onNavigate = {navController.navigate("camera")})
//        }
//    }
//
//}

@Composable
fun Greetings(name: String, onNavigate: () -> Unit, recognizedText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding to the entire column
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello $name!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 16.dp) // Add padding below the Text
        )

        Spacer(modifier = Modifier.height(24.dp)) // Increased space between elements

        Button(
            onClick = {
                Log.d("INFO", "Greetings: after click")
                onNavigate()
            },
            modifier = Modifier.fillMaxWidth() // Make button full width
        ) {
            Text(
                text = "Scan",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Space before recognized text

        Text(
            text = "Output: \n $recognizedText",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(top = 16.dp) // Add padding above the Text
        )
    }
}
