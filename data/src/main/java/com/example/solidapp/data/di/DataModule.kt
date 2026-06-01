package com.example.solidapp.data.di

import android.app.Application
import androidx.room.Room
import com.example.solidapp.data.export.CsvExpenseExporter
import com.example.solidapp.data.export.PdfExpenseExporter
import com.example.solidapp.data.local.ExpenseDatabase
import com.example.solidapp.data.local.dao.ExpenseDao
import com.example.solidapp.data.repository.ExpenseRepositoryImpl
import com.example.solidapp.data.util.SystemTimeProvider
import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.repository.ExpenseReader
import com.example.solidapp.domain.repository.ExpenseWriter
import com.example.solidapp.domain.util.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(app: Application): ExpenseDatabase {
        return Room.databaseBuilder(app, ExpenseDatabase::class.java, "expenses_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao = db.expenseDao

    @Provides
    @Singleton
    fun provideExpenseRepositoryImpl(dao: ExpenseDao): ExpenseRepositoryImpl = ExpenseRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideExpenseReader(impl: ExpenseRepositoryImpl): ExpenseReader = impl

    @Provides
    @Singleton
    fun provideExpenseWriter(impl: ExpenseRepositoryImpl): ExpenseWriter = impl

    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = SystemTimeProvider()

    @Provides
    @IntoMap
    @StringKey("CSV")
    fun provideCsvExporter(): ExpenseExporter = CsvExpenseExporter()

    @Provides
    @IntoMap
    @StringKey("PDF")
    fun providePdfExporter(): ExpenseExporter = PdfExpenseExporter()
}
