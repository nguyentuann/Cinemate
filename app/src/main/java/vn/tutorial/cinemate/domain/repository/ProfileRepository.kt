package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ProfileModel
import java.io.File

interface ProfileRepository {
    suspend fun getProfile(): Resource<ProfileModel?>
    suspend fun saveProfile(profile: ProfileModel): Resource<ProfileModel?>
    suspend fun updateAvatar(file: File): Resource<String?>
}