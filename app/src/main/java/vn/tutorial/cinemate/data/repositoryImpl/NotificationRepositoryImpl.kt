package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.domain.repository.NotificationRepository
import vn.tutorial.cinemate.mockdata.Notification
import vn.tutorial.cinemate.mockdata.NotificationData
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor() : NotificationRepository, BaseService() {
    override suspend fun getImportantNotifications(): Resource<List<Notification>> {
        val mockData = NotificationData.importantNotification
        return Resource.Success(mockData)
    }

    override suspend fun getOtherNotifications(): Resource<List<Notification>> {
        val mockData = NotificationData.otherNotification
        return Resource.Success(mockData)
    }
}