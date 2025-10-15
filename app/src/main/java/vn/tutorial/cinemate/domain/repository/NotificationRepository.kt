package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.mockdata.Notification

interface NotificationRepository {
    suspend fun getImportantNotifications(): Resource<List<Notification>>
    suspend fun getOtherNotifications(): Resource<List<Notification>>
}