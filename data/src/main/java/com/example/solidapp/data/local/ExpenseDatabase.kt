package com.example.solidapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.solidapp.data.local.dao.ExpenseDao
import com.example.solidapp.data.local.entity.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract val expenseDao: ExpenseDao
}
