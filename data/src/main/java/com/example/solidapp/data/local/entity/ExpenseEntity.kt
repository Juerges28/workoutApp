package com.example.solidapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.model.WorkoutLocation
import com.example.solidapp.domain.model.WorkoutSession

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val timestamp: Long,
    val paymentMethodType: String = "Gym",
    val cardLastFourDigits: String? = null
)

fun ExpenseEntity.toDomain(): WorkoutSession {
    return WorkoutSession(
        id              = id,
        title           = title,
        durationMinutes = amount,
        category        = runCatching { WorkoutCategory.valueOf(category) }
                              .getOrDefault(WorkoutCategory.OTHER),
        timestamp       = timestamp,
        location        = when (paymentMethodType) {
            "Home"    -> WorkoutLocation.Home
            "Outdoor" -> WorkoutLocation.Outdoor
            else      -> WorkoutLocation.Gym
        }
    )
}

fun WorkoutSession.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id                 = id,
        title              = title,
        amount             = durationMinutes,
        category           = category.name,
        timestamp          = timestamp,
        paymentMethodType  = when (location) {
            is WorkoutLocation.Gym     -> "Gym"
            is WorkoutLocation.Home    -> "Home"
            is WorkoutLocation.Outdoor -> "Outdoor"
        },
        cardLastFourDigits = null
    )
}
