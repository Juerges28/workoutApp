package com.example.solidapp.data.di

import android.app.Application
import androidx.room.Room
import com.example.solidapp.data.local.ExpenseDatabase
import com.example.solidapp.data.local.dao.ExpenseDao
import com.example.solidapp.data.repository.ExpenseRepositoryImpl
import com.example.solidapp.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(app: Application): ExpenseDatabase {
        return Room.databaseBuilder(
            app,
            ExpenseDatabase::class.java,
            "expenses_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao {
        return db.expenseDao
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(dao)
    }
}
