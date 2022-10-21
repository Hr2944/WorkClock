package com.hrb.holidays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.hrb.holidays.app.entities.settings.Theme
import com.hrb.holidays.app.presenters.settings.SettingsScreenPresenter
import com.hrb.holidays.ui.components.datepicker.RangePicker
import com.hrb.holidays.ui.theme.HolidaysTheme
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppMainScreen()
        }
    }
}

@Composable
fun AppMainScreen(settingsPresenter: SettingsScreenPresenter = getViewModel()) {
    HolidaysTheme(
        darkTheme = settingsPresenter.settingsState.theme == Theme.DARK
    ) {
        Surface {
//            BaseScreenActivity()
            RangePicker(
                onDismissRequest = {},
                onSave = { _, _ -> }
            )
        }
    }
}
