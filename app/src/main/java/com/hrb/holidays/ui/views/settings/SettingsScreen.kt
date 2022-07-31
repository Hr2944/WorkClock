package com.hrb.holidays.ui.views.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hrb.holidays.app.entities.settings.Theme
import com.hrb.holidays.app.presenters.settings.SettingsScreenPresenter
import com.hrb.holidays.ui.components.buttons.TextButtonGroup
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreenActivity(presenter: SettingsScreenPresenter = getViewModel()) {
    val scope = rememberCoroutineScope()

    SettingsScreen(
        availableThemes = mapOf("Light" to Theme.LIGHT, "Dark" to Theme.DARK),
        theme = presenter.settingsState.theme,
        onSelectTheme = { theme -> scope.launch { presenter.updateTheme(theme) } }
    )
}

@Composable
fun SettingsScreen(
    theme: Theme,
    onSelectTheme: (Theme) -> Unit,
    availableThemes: Map<String, Theme>
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Theme",
            style = MaterialTheme.typography.overline,
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextButtonGroup(
            onSelect = { name, _ -> onSelectTheme(availableThemes[name] ?: theme) },
            buttonTexts = availableThemes.keys.toTypedArray(),
            selectedIndex = availableThemes.values.indexOf(theme)
        )
    }
}
