package com.hrb.holidays

import android.app.Application
import com.hrb.holidays.app.databases.holidays.IHolidaysTimetableGateway
import com.hrb.holidays.app.databases.office.IOfficeWeekGateway
import com.hrb.holidays.app.databases.settings.ISettingsRepository
import com.hrb.holidays.koindi.controllers.controllersModule
import com.hrb.holidays.koindi.databases.databasesModule
import com.hrb.holidays.koindi.interactors.interactorsModules
import com.hrb.holidays.koindi.presenters.presentersModule
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(presentersModule, databasesModule, interactorsModules, controllersModule)
        }

        val officeWeekGateway: IOfficeWeekGateway = get()
        val holidaysTimetableGateway: IHolidaysTimetableGateway = get()
        val settingsRepository: ISettingsRepository = get()
        runBlocking {
            officeWeekGateway.initialize()
            holidaysTimetableGateway.initialize()
            settingsRepository.initialize()
        }
    }
}
