package com.example.solidapp.data.repository

import com.example.solidapp.data.local.dao.ExpenseDao
import com.example.solidapp.data.local.entity.toDomain
import com.example.solidapp.data.local.entity.toEntity
import com.example.solidapp.domain.model.WorkoutSession
import com.example.solidapp.domain.repository.ExpenseReader
import com.example.solidapp.domain.repository.ExpenseWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseReader, ExpenseWriter {

    override fun getAllExpenses(): Flow<List<WorkoutSession>> =
        dao.getAllExpenses().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getExpenseById(id: Long): WorkoutSession? =
        dao.getExpenseById(id)?.toDomain()

    override suspend fun insertExpense(session: WorkoutSession) =
        dao.insertExpense(session.toEntity())

    override suspend fun deleteExpense(session: WorkoutSession) =
        dao.deleteExpense(session.toEntity())
}
