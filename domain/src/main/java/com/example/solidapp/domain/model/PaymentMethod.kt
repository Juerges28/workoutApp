package com.example.solidapp.domain.model

sealed class PaymentMethod {
    data object Cash : PaymentMethod()
    data class Card(val lastFourDigits: String) : PaymentMethod()
}
