package com.madispace.worldofmothers.worker

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.madispace.domain.models.image.PhotoModel
import com.madispace.domain.repository.UserRepository
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.ui.common.NotificationFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UploadPhotoWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val userRepository: UserRepository by inject()

    override suspend fun doWork(): Result {
        setForegroundAsync(createForegroundInfo())
        val uriString = inputData.getString(URI)
        val fileName = inputData.getString(FILE_NAME) ?: ""
        uriString?.let {
            val uri = Uri.parse(it)
            val mediaType = context.contentResolver.getType(uri) ?: ""
            val file = context.contentResolver.openInputStream(uri)?.readBytes()
            file?.let { safeFile ->
                val result = userRepository.uploadFile(
                    PhotoModel(
                        file = safeFile,
                        mediaType = mediaType,
                        fileName = fileName
                    )
                )
                return if (result) {
                    Result.success()
                } else {
                    Result.failure()
                }
            } ?: return Result.failure()
        } ?: return Result.failure()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val notificationId = 1
        return ForegroundInfo(
            notificationId, NotificationFactory.createUploadNotification(
                context,
                context.getString(R.string.upload_photo),
                context.getString(R.string.cancel),
                WorkManager.getInstance(context).createCancelPendingIntent(id)
            )
        )
    }

    companion object {
        const val FILE_NAME = "file_name"
        const val URI = "uri"
    }
}