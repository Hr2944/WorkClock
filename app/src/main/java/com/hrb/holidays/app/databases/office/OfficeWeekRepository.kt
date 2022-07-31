package com.hrb.holidays.app.databases.office

import android.content.Context
import androidx.datastore.core.DataStore
import com.hrb.holidays.app.databases.proto.*
import com.hrb.holidays.business.entities.office.OfficeDay
import com.hrb.holidays.business.entities.office.OfficeWeek
import java.time.DayOfWeek
import java.time.LocalTime

class OfficeWeekRepository(
    context: Context,
    store: DataStore<OfficeWeekProto> = context.officeWeekDataStore,
) : ProtoSerializerToGateway<OfficeWeek, OfficeWeekProto>(store),
    IOfficeWeekGateway {
    override var fetchedValue: OfficeWeek = OfficeWeek(setOf())

    override fun OfficeWeekProto.toEntity(): OfficeWeek {
        return OfficeWeek(
            this.weekDaysList.map { officeDay ->
                OfficeDay(
                    dayOfWeek = DayOfWeek.of(officeDay.dayOfWeek.number),
                    startAt = LocalTime.of(officeDay.startAt.hours, officeDay.startAt.minutes),
                    endAt = LocalTime.of(officeDay.endAt.hours, officeDay.endAt.minutes)
                )
            }.toSet()
        )
    }

    private fun OfficeDay.toProto(): OfficeDayProto {
        return officeDayProto {
            dayOfWeek = DayOfWeekProto.forNumber(this@toProto.dayOfWeek.value)
            startAt = localTimeProto {
                hours = this@toProto.startAt.hour
                minutes = this@toProto.startAt.minute
            }
            endAt = localTimeProto {
                hours = this@toProto.endAt.hour
                minutes = this@toProto.endAt.minute
            }
        }
    }

    override suspend fun updateStoreData(
        proto: OfficeWeekProto,
        newValue: OfficeWeek
    ): OfficeWeekProto {
        return proto.toBuilder()
            .clearWeekDays()
            .addAllWeekDays(newValue.week.map { officeDay -> officeDay.toProto() })
            .build()
    }
}
