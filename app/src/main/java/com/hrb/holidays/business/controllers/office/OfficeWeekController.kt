package com.hrb.holidays.business.controllers.office

import com.hrb.holidays.business.entities.office.OfficeDay
import com.hrb.holidays.business.entities.office.OfficeWeek
import com.hrb.holidays.app.databases.office.IOfficeWeekGateway

class OfficeWeekController(
    private val officeWeekFetcher: IOfficeWeekGateway
) : IOfficeWeekController {
    override fun getWeek(): OfficeWeek = officeWeekFetcher.fetch()

    override suspend fun updateDay(day: OfficeDay): OfficeWeek {
        val currentWeek = officeWeekFetcher.fetch()
        val updatedWeek = currentWeek.week.map {
            if (it.dayOfWeek == day.dayOfWeek) {
                day
            } else {
                it
            }
        }
        officeWeekFetcher.update(currentWeek.copy(week = updatedWeek.toMutableSet()))
        return getWeek()
    }
}
