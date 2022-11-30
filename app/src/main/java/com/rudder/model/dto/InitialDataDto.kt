package com.rudder.model.dto

data class InitialDataDtoArround(
    val results: InitialDataDto
)

data class InitialDataDto(
    val isNewest: Boolean,
    val notReadNotificationCount: Int,
)