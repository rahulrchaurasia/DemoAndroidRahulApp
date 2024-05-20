package com.policyboss.demoandroidapp.HiltModule

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.policyboss.demoandroidapp.Utility.NotificationHelper
import com.policyboss.demoandroidapp.Utility.NotificationHelperNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/*
A module scoped to the ViewModelComponent cannot provide
@ActivityContext Context because that would be a
 leak since ViewModels are retained across configuration
 changes and outlive the Activity. However if you only need a Context,
  not necessarily the activity one, then you can change your provider function
  to request for a Context with the @ApplicationContext qualifier.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModules {


    @Singleton
    @Provides
    fun provideNotificationHelperNavGraph(
        @ApplicationContext context: Context,
        notificationManager: NotificationManagerCompat
    ): NotificationHelperNavGraph {
        return NotificationHelperNavGraph(context, notificationManager)
    }

    @Singleton
    @Provides
    fun provideNotificationHelper(
        @ApplicationContext context: Context,
        notificationManager: NotificationManagerCompat
    ): NotificationHelper {
        return NotificationHelper(context, notificationManager)
    }


    @Singleton
    @Provides

    fun provideNotificationManagerorNotifications(@ApplicationContext context: Context) : NotificationManagerCompat {

       return NotificationManagerCompat.from(context)

    }

    @Qualifier
    annotation class ForNotifications

    @Qualifier
    annotation class ForNavigation



}

