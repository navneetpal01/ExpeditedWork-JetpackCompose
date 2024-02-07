package com.example.expeditedwork

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.expeditedwork.ui.theme.ExpeditedWorkTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.Duration

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpeditedWorkTheme {
                val permission = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
                LaunchedEffect(key1 = Unit){
                   if (permission.status.isGranted){
                       val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
                           //Only for Expedited work
                           .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                           //Can't use delay with Expedited Work
                           //.setInitialDelay(Duration.ofSeconds(10))
                           .setBackoffCriteria(
                               backoffPolicy = BackoffPolicy.LINEAR,
                               duration = Duration.ofSeconds(15)
                           )
                           .build()
                       //application Context is use whose lifecycle is different from the current context we use LocalContext
                       //for the Composable
                       WorkManager.getInstance(applicationContext).enqueue(workRequest)
                   }else{
                       permission.launchPermissionRequest()
                   }
                }

            }
        }
    }
}

