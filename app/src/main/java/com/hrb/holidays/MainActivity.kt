package com.hrb.holidays

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.hrb.holidays.app.entities.settings.Theme
import com.hrb.holidays.app.presenters.settings.SettingsScreenPresenter
import com.hrb.holidays.ui.components.datepicker.DateRangePicker
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppMainScreen(settingsPresenter: SettingsScreenPresenter = getViewModel()) {
    HolidaysTheme(
        darkTheme = settingsPresenter.settingsState.theme == Theme.DARK
    ) {
//        Surface {
//            BaseScreenActivity()
//        }

        var showDialog: Boolean by rememberSaveable {
            mutableStateOf(true)
        }

        Button(onClick = {
            showDialog = true
        }) {
            Text("Show Dialog")
        }
        if (showDialog) {
            DateRangePicker(
                onDismissRequest = {
                    showDialog = false
                },
                onSave = { _, _ ->
                    showDialog = false
                }
            )
        }

//        val l = remember { mutableStateListOf(1, 2, 3) }
//
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Button(onClick = { l.add(l.last() + 1) }) {
//                Text("Add")
//            }
//
//            LazyColumn(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Log.d("LazyColumn", "Composed!")
//                items(
//                    l,
//                    key = { it }
//                ) { t ->
//                    Log.d("LazyColumn--items", "Composed! -- Item: $t")
//                    T(t.toString(), modifier = Modifier.fillMaxHeight(.5f))
//                }
//            }
//        }
    }
}

@Composable
fun T(t: String, modifier: Modifier = Modifier) {
    Log.d("T", "Composed! -- Item: $t")
    Text(text = t, modifier = modifier)
    Divider(modifier = Modifier.fillMaxWidth())
}

// Il appel toujours LazyColumn--items, mais pas T lorsque T n'est pas visible ou n'as pas changé
// -> recompose que ce qui est visible et différent, mais LazyColumn elle même est recomposée à
// chaque fois.
