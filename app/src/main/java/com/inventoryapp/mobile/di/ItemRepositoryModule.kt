package com.inventoryapp.mobile.di

import android.content.Context
import androidx.room.Room
import com.inventoryapp.mobile.dao.ItemDao
import com.inventoryapp.mobile.db.AppDatabase
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.repository.cache.ItemCache
import com.inventoryapp.mobile.repository.remote.ItemRemote
import com.inventoryapp.mobile.repositoryImpl.ItemRepositoryImpl
import com.inventoryapp.mobile.repositoryImpl.cache.ItemCacheImpl
import com.inventoryapp.mobile.repositoryImpl.remote.ItemRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ItemRepositoryModule {

    @Binds
    fun bindsItemRepository(
        itemRepositoryImpl: ItemRepositoryImpl
    ): ItemRepository

    @Binds
    fun bindsItemCache(
        itemCacheImpl: ItemCacheImpl
    ): ItemCache

    @Binds
    fun bindsItemRemote(
        itemRemoteImpl: ItemRemoteImpl
    ): ItemRemote

    companion object {

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder( appContext,
                AppDatabase::class.java,
                "inventory-database" ).build()
        }

        @Provides
        fun provideItemDao(appDatabase: AppDatabase): ItemDao =
            appDatabase.itemDao()

        @Provides
        fun provideItemCache(itemDao: ItemDao) = ItemCacheImpl(itemDao)

        @Provides
        fun provideItemRemote() = ItemRemoteImpl()

        @Provides
        fun provideItemRepository(
            itemCache: ItemCache,
            itemRemote: ItemRemote
        ) = ItemRepositoryImpl(itemCache, itemRemote)

    }
}