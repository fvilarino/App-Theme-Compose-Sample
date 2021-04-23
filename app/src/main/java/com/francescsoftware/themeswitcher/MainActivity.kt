package com.francescsoftware.themeswitcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.francescsoftware.themeswitcher.settings.AppTheme
import com.francescsoftware.themeswitcher.settings.UserSettings
import com.francescsoftware.themeswitcher.ui.theme.ThemeSwitcherTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSettings: UserSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme = userSettings.themeStream.collectAsState()
            val useDarkColors = when (theme.value) {
                AppTheme.MODE_AUTO -> isSystemInDarkTheme()
                AppTheme.MODE_DAY -> false
                AppTheme.MODE_NIGHT -> true
            }
            ThemeSwitcherTheme(darkTheme = useDarkColors) {
                Surface {
                    LandingScreen(
                        selectedTheme = theme.value,
                        onItemSelected = { theme -> userSettings.theme = theme },
                    )
                }
            }
        }
    }
}

@Composable
fun LandingScreen(
    selectedTheme: AppTheme,
    onItemSelected: (AppTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    val themeItems = listOf(
        RadioButtonItem(
            id = AppTheme.MODE_DAY.ordinal,
            title = stringResource(id = R.string.light_theme),
        ),
        RadioButtonItem(
            id = AppTheme.MODE_NIGHT.ordinal,
            title = stringResource(id = R.string.dark_theme),
        ),
        RadioButtonItem(
            id = AppTheme.MODE_AUTO.ordinal,
            title = stringResource(id = R.string.auto_theme),
        ),
    )

    Column(
        modifier = modifier.fillMaxSize().padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.choose_your_theme),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 24.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        RadioGroup(
            items = themeItems,
            selected = selectedTheme.ordinal,
            onItemSelect = { id -> onItemSelected(AppTheme.fromOrdinal(id)) },
            modifier = Modifier.fillMaxWidth(.6f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.padding(all = 16.dp),
            onClick = { },
        ) {
            Text(
                text = stringResource(R.string.sample_button_label)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.lorem_ipsum),
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    ThemeSwitcherTheme {
        var theme by remember {
            mutableStateOf(AppTheme.MODE_AUTO)
        }
        Surface(modifier = Modifier.width(420.dp)) {
            LandingScreen(
                selectedTheme = theme,
                onItemSelected = { value -> theme = value}
            )
        }
    }
}
