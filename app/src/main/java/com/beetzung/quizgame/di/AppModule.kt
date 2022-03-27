package com.beetzung.quizgame.di

import android.content.Context
import com.beetzung.quizgame.data.GameAPI
import com.beetzung.quizgame.data.GameAPIImpl
import com.beetzung.quizgame.data.Preferences
import com.beetzung.quizgame.data.LobbyAPI
import com.beetzung.quizgame.data.LobbyAPIImpl
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
    fun provideGameAPI(): GameAPI = GameAPIImpl()

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext context: Context) = Preferences(context)

    @Singleton
    @Provides
    fun provideLobbyAPI(): LobbyAPI = LobbyAPIImpl()
}