package com.atech.core.utils

import com.atech.core.datasource.datastore.Cgpa
import com.atech.core.datasource.firebase.auth.AttendanceUploadModel

sealed class UpdateDataType {
    data class UploadAttendance(val data: List<AttendanceUploadModel>) : UpdateDataType()
    data class UpdateCourseSem(val course: String, val sem: String) : UpdateDataType()
    data class UpdateCgpa(val cgpa: Cgpa) : UpdateDataType()
}