package com.beetzung.quizgame.di

import android.content.Context
import com.beetzung.quizgame.data.SocketAPI
import com.beetzung.quizgame.data.socket_api_impl.SocketAPIImpl
import com.beetzung.quizgame.data.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideSocketAPI(): SocketAPI = SocketAPIImpl()

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext context: Context) = Preferences(context)
}