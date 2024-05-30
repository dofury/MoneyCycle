package com.dofury.moneycycle.module

import android.content.Context
import androidx.room.Room
import com.dofury.moneycycle.dao.MoneyLogDao
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.repository.MoneyLogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiModule {

    @Singleton
    @Provides
    fun provideMoneyLogRepository(
        moneyLogDao: MoneyLogDao
    ) : MoneyLogRepository {
        return MoneyLogRepository(moneyLogDao)
    }

    @Singleton
    @Provides
    fun provideMoneyLogDatabase(@ApplicationContext context: Context) : MoneyLogDatabase {
        return Room
            .databaseBuilder(
                context,
                MoneyLogDatabase::class.java,
                MoneyLogDatabase.DATABASE_NAME
            ).build()
    }

    @Singleton
    @Provides
    fun provideMoneyLogDAO(moneyLogDB: MoneyLogDatabase): MoneyLogDao {
        return moneyLogDB.moneyLogDao()
    }
}