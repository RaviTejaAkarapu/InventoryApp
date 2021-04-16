package com.inventoryapp.mobile.di

import android.content.Context
import android.net.ConnectivityManager
import com.inventoryapp.mobile.networkNotification.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
interface NetworkMonitorModule {
    companion object {
        @Provides
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
            context.getSystemService(ConnectivityManager::class.java)

        @Provides
        fun provideNetworkMonitor(connectivityManager: ConnectivityManager) =
            NetworkMonitor(connectivityManager)
    }
}