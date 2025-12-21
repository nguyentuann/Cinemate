package vn.tutorial.cinemate.domain.usecase.notification

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.NotificationRepository
import vn.tutorial.cinemate.mockdata.Notification
import javax.inject.Inject

class GetImportantNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
): BaseUseCase<Unit, Resource<List<Notification>>>() {
    override suspend fun execute(param: Unit): Resource<List<Notification>> {
       return notificationRepository.getImportantNotifications()
    }
}