package com.example.weatherlogger.di.module

import com.example.weatherlogger.data.source.local.WeatherLocalDataSource
import com.example.weatherlogger.data.source.local.WeatherLocalDataSourceImpl
import com.example.weatherlogger.data.source.remote.WeatherRemoteDataSource
import com.example.weatherlogger.data.source.remote.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourcesModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSourceImpl: WeatherLocalDataSourceImpl): WeatherLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource
}
