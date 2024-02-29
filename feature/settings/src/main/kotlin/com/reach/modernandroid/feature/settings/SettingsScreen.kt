/*
 * Copyright 2024 Reach Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reach.modernandroid.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.reach.base.ui.common.devicepreview.DevicePreviews
import com.reach.modernandroid.core.data.datastore.model.UserSettings
import com.reach.modernandroid.core.ui.common.AppPreview
import com.reach.modernandroid.core.ui.common.AppUiState
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.navigation.screenComposable
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.design.AppIcons
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

fun NavGraphBuilder.settingsRoute() {
    screenComposable(AppRoute.SETTINGS) {
        SettingsRoute()
    }
}

@Composable
private fun SettingsRoute(
    appUiState: AppUiState = koinInject(),
    viewModel: SettingsViewModel = koinNavViewModel(),
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    SettingsScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        onDynamicColorChange = { viewModel.setDynamicTheme(it) },
        onDarkModeClick = { viewModel.setDarkThemeConfig(it) },
        settings = settings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onBackClick: () -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onDarkModeClick: (Int) -> Unit,
    settings: UserSettings,
) {
    Column {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.settings)) },
            onBackClick = onBackClick,
        )
        ThemeSetting(
            onDynamicColorChange = onDynamicColorChange,
            onDarkModeClick = onDarkModeClick,
            settings = settings,
            darkModes = stringArrayResource(id = R.array.dark_modes),
        )
    }
}

@Composable
private fun ThemeSetting(
    onDynamicColorChange: (Boolean) -> Unit,
    onDarkModeClick: (Int) -> Unit,
    settings: UserSettings,
    darkModes: Array<String>,
) {
    var showDarkModeDialog by rememberSaveable { mutableStateOf(false) }

    SettingItem {
        Text(text = stringResource(id = R.string.dynamic_color))
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = settings.dynamicColor,
            onCheckedChange = onDynamicColorChange,
        )
    }

    SettingItem {
        Text(text = stringResource(id = R.string.dark_mode))
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = darkModes[settings.darkThemeConfig.ordinal],
            modifier = Modifier
                .clickable { showDarkModeDialog = true }
                .padding(4.dp),
        )
        Icon(
            imageVector = AppIcons.next,
            contentDescription = "",
            modifier = Modifier
                .clickable { showDarkModeDialog = true }
                .padding(8.dp),
        )
    }

    if (showDarkModeDialog) {
        DarkModeDialog(
            darkModes = darkModes,
            onDismissRequest = { showDarkModeDialog = false },
            onDarkModeClick = onDarkModeClick,
            selectedMode = settings.darkThemeConfig.ordinal,
        )
    }
}

@Composable
private fun SettingItem(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkModeDialog(
    darkModes: Array<String>,
    onDismissRequest: () -> Unit,
    onDarkModeClick: (Int) -> Unit,
    selectedMode: Int,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(id = R.string.dark_mode))

                Icon(
                    imageVector = AppIcons.Close,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { onDismissRequest() }
                        .align(Alignment.CenterEnd)
                        .padding(16.dp),
                )
            }

            darkModes.forEachIndexed { index, str ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable {
                            onDismissRequest()
                            onDarkModeClick(index)
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = index == selectedMode,
                        onClick = {
                            onDismissRequest()
                            onDarkModeClick(index)
                        },
                    )
                    Text(text = str)
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun SettingsScreenPreview() {
    AppPreview {
        SettingsScreen(
            onBackClick = { },
            onDynamicColorChange = {},
            onDarkModeClick = {},
            settings = UserSettings(),
        )
    }
}
