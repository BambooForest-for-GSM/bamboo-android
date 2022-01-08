package com.study.bamboo.di

import com.example.data.network.admin.AdminApi
import com.example.data.network.user.UserApi
import com.example.data.repository.admin.AdminDataSourceImpl
import com.example.data.repository.user.UserDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun providesAdminDataSource(service: AdminApi) =
        AdminDataSourceImpl(service)

    @Provides
    @Singleton
    fun providesUserDataSource(service: UserApi) =
        UserDataSourceImpl(service)
}