package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel

val samplePlans = listOf(
    SubscriptionPlanModel(
        id = "1",
        name = "Cao cấp",
        resolution = "4K + HDR",
        price = 273_000,
        quality = "Tốt nhất",
        sound = "Âm thanh không gian (âm thanh chân thực)",
        supportedDevices = "TV, máy tính bảng, điện thoại di động, máy tính",
        simultaneousDevices = 4,
        downloadDevices = 6,
    ),
    SubscriptionPlanModel(
        id = "2",
        name = "Tiêu chuẩn",
        resolution = "1080p",
        price = 180_000,
        quality = "Tốt",
        sound = "Có",
        supportedDevices = "TV, máy tính bảng, điện thoại di động, máy tính",
        simultaneousDevices = 2,
        downloadDevices = 2
    ),
    SubscriptionPlanModel(
        id = "3",
        name = "Cơ bản",
        resolution = "720p",
        price = 108_000,
        quality = "Khá",
        sound = "Có",
        supportedDevices = "TV, máy tính bảng, điện thoại di động, máy tính",
        simultaneousDevices = 1,
        downloadDevices = 1
    ),
    SubscriptionPlanModel(
        id = "4",
        name = "Di động",
        resolution = "480p",
        price = 70_000,
        quality = "Cơ bản",
        sound = "Có",
        supportedDevices = "Điện thoại di động, máy tính bảng",
        simultaneousDevices = 1,
        downloadDevices = 1
    )
)
