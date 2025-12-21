package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.FeaturedSubscriptionPlanModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel

val samplePlans = listOf(
    SubscriptionPlanModel(
        id = "007eb805-ca46-4a4b-a90c-f371211e31dd",
        name = "Premium",
        description = "Premium monthly subscription with unlimited access to all content",
        price = 79000.0,
        durationDays = 30,
        maxDevice = 4,
        featured = FeaturedSubscriptionPlanModel (
            addFree = true,
    offlineDownload = true,
    hdStreaming = true,
    multipleDevices = true,
    familySharing = false
        )
    ),
    SubscriptionPlanModel(
        id = "ad518148-5b5f-4549-95b9-8df2a589d200",
        name = "Family",
        description = "Family plan with up to 6 members, perfect for families with parental controls",
        price = 149000.0,
        durationDays = 30,
        maxDevice = 10,
        featured = FeaturedSubscriptionPlanModel(
            addFree = true,
            offlineDownload = true,
            hdStreaming = true,
            multipleDevices = true,
            familySharing = true
        )
    )
)
