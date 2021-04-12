package com.inventoryapp.mobile.di

import com.inventoryapp.mobile.dao.ItemDao
import com.inventoryapp.mobile.db.AppDatabase
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.repositoryImpl.ItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ItemRepositoryModule {

    @Binds
    fun bindsItemRepository(
        itemRepositoryImpl: ItemRepositoryImpl
    ): ItemRepository

companion object {

    @Provides
    fun provideItemDao(appDatabase: AppDatabase): ItemDao =
        appDatabase.itemDao()

    @Provides
    fun provideItemRepository() = ItemRepositoryImpl()
}
}