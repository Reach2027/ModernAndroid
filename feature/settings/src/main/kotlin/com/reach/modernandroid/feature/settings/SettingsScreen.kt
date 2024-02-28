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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.reach.base.ui.common.devicepreview.DevicePreviews
import com.reach.modernandroid.core.data.datastore.model.DarkThemeConfig
import com.reach.modernandroid.core.data.datastore.model.UserSetting
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
        settings = settings,
        onDynamicColorChange = { viewModel.setDynamicTheme(it) },
        onDarkModeClick = { viewModel.setDarkThemeConfig(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onBackClick: () -> Unit,
    settings: UserSetting,
    onDynamicColorChange: (Boolean) -> Unit,
    onDarkModeClick: (Int) -> Unit,
) {
    Column {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.settings)) },
            onBackClick = onBackClick,
        )
        ThemeSetting(
            settings = settings,
            onDynamicColorChange = onDynamicColorChange,
            onDarkModeClick = onDarkModeClick,
        )
    }
}

@Composable
private fun ThemeSetting(
    settings: UserSetting,
    onDynamicColorChange: (Boolean) -> Unit,
    onDarkModeClick: (Int) -> Unit,
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
            text = settings.darkThemeConfig.name,
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
            selectedOption = settings.darkThemeConfig.ordinal,
            onDismissRequest = { showDarkModeDialog = false },
            options = DarkThemeConfig.entries.map { it.name },
            onDarkModeClick = onDarkModeClick,
        )
    }
}

@Composable
private fun SettingItem(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkModeDialog(
    selectedOption: Int,
    onDismissRequest: () -> Unit,
    options: List<String>,
    onDarkModeClick: (Int) -> Unit,
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

            options.forEachIndexed { index, str ->
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
                        selected = index == selectedOption,
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
            settings = UserSetting(),
            onDynamicColorChange = {},
            onDarkModeClick = {},
        )
    }
}
