package com.hrb.holidays.business.controllers.office

import com.hrb.holidays.business.entities.office.OfficeDay
import com.hrb.holidays.business.entities.office.OfficeWeek

interface IOfficeWeekController {
    fun getWeek(): OfficeWeek
    suspend fun updateDay(day: OfficeDay): OfficeWeek
}
