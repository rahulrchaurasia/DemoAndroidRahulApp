package com.policyboss.demoandroidapp.HiltModule


import com.policyboss.demoandroidapp.HiltDemo.Car
import com.policyboss.demoandroidapp.HiltDemo.Engine
import com.policyboss.demoandroidapp.HiltDemo.Piston
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object HiltModules {


    //dependency Engine req. Piston in its constructor
    @Provides
    @Singleton
    fun provideEngine(piston: Piston): Engine {

        return Engine(piston)
    }

    // dependency Car req. Engine in its constructor
    @Provides
    @Singleton
    fun provideCar(engine: Engine) : Car {

        return Car(engine)
    }


    fun providePiston() : Piston{

        return Piston()
    }
}