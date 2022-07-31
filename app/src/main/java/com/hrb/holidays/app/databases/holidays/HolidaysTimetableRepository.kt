package com.hrb.holidays.app.databases.holidays

import android.content.Context
import androidx.datastore.core.DataStore
import com.hrb.holidays.app.databases.proto.*
import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import com.hrb.holidays.business.entities.holidays.HolidaysTimetable
import java.time.LocalDate

class HolidaysTimetableRepository(
    context: Context,
    store: DataStore<HolidaysTimetableProto> = context.holidaysTimetableDataStore
) : ProtoSerializerToGateway<HolidaysTimetable, HolidaysTimetableProto>(store),
    IHolidaysTimetableGateway {
    override var fetchedValue: HolidaysTimetable = HolidaysTimetable(mutableSetOf())

    override fun HolidaysTimetableProto.toEntity(): HolidaysTimetable {
        return HolidaysTimetable(
            this.holidayPeriodsList.map {
                HolidayPeriod(
                    name = it.name,
                    fromDate = LocalDate.of(it.fromDate.year, it.fromDate.month, it.fromDate.day),
                    toDate = LocalDate.of(it.toDate.year, it.toDate.month, it.toDate.day)
                )
            }.toMutableSet()
        )
    }

    private fun HolidayPeriod.toProto(): HolidayPeriodProto {
        return holidayPeriodProto {
            name = this@toProto.name
            fromDate = localDateProto {
                year = this@toProto.fromDate.year
                month = this@toProto.fromDate.monthValue
                day = this@toProto.fromDate.dayOfMonth
            }
            toDate = localDateProto {
                year = this@toProto.toDate.year
                month = this@toProto.toDate.monthValue
                day = this@toProto.toDate.dayOfMonth
            }
        }
    }

    override suspend fun updateStoreData(
        proto: HolidaysTimetableProto,
        newValue: HolidaysTimetable
    ): HolidaysTimetableProto {
        return proto.toBuilder()
            .clearHolidayPeriods()
            .addAllHolidayPeriods(newValue.timetable.map { it.toProto() })
            .build()
    }
}
