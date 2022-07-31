package com.hrb.holidays.koindi.controllers

import com.hrb.holidays.app.controllers.settings.ISettingsController
import com.hrb.holidays.app.controllers.settings.SettingsController
import com.hrb.holidays.business.controllers.holidays.HolidaysTimetableController
import com.hrb.holidays.business.controllers.holidays.IHolidaysTimetableController
import com.hrb.holidays.business.controllers.office.IOfficeTimeBeforeHolidaysController
import com.hrb.holidays.business.controllers.office.IOfficeWeekController
import com.hrb.holidays.business.controllers.office.OfficeTimeBeforeHolidaysController
import com.hrb.holidays.business.controllers.office.OfficeWeekController
import org.koin.core.module.Module
import org.koin.dsl.module


val controllersModule: Module = module {
    single<IOfficeTimeBeforeHolidaysController> {
        OfficeTimeBeforeHolidaysController(
            get(),
            get()
        )
    }
    single<IHolidaysTimetableController> { HolidaysTimetableController(get()) }
    single<IOfficeWeekController> { OfficeWeekController(get()) }
    single<ISettingsController> { SettingsController(get()) }
}
