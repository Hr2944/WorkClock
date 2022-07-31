package com.hrb.holidays.koindi.interactors

import com.hrb.holidays.business.interactors.holidays.Holidays
import com.hrb.holidays.business.interactors.holidays.IHolidays
import com.hrb.holidays.business.interactors.office.IRangeOfficeTimeCalculator
import com.hrb.holidays.business.interactors.office.IRangeTimeCalculator
import com.hrb.holidays.business.interactors.office.RangeOfficeTimeCalculator
import com.hrb.holidays.business.interactors.office.RangeTimeCalculator
import org.koin.dsl.module

val interactorsModules = module {
    single<IHolidays> { Holidays(get()) }
    single<IRangeOfficeTimeCalculator> { RangeOfficeTimeCalculator(get()) }
    single<IRangeTimeCalculator> { RangeTimeCalculator() }
}
