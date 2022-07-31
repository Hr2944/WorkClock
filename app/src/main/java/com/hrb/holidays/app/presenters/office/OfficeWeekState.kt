package com.hrb.holidays.app.presenters.office

import com.hrb.holidays.business.entities.office.OfficeDay

data class OfficeWeekState(
    val weekDays: Set<OfficeDay>
)
