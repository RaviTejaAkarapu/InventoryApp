package com.inventoryapp.mobile.di

import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.repositoryImpl.ItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
interface ItemRepositoryModule {

    @Binds
    fun bindsItemRepository(
        itemRepositoryImpl: ItemRepositoryImpl
    ): ItemRepository

}