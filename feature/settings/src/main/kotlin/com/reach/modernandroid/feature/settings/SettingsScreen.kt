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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.reach.base.ui.common.devicepreview.DevicePreviews
import com.reach.base.ui.common.devicepreview.previewWindowSizeClass
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
        windowSizeClass = appUiState.getWindowSizeClass(),
        onBackClick = { appUiState.getNavController().navigateUp() },
        settings = settings,
        onDynamicColorChange = { viewModel.setDynamicTheme(it) },
        onOptionClick = { viewModel.setDarkThemeConfig(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    windowSizeClass: WindowSizeClass,
    onBackClick: () -> Unit,
    settings: UserSetting,
    onDynamicColorChange: (Boolean) -> Unit,
    onOptionClick: (Int) -> Unit,
) {
    Column {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.settings)) },
            onBackClick = onBackClick,
        )
        ThemeSetting(
            settings = settings,
            onDynamicColorChange = onDynamicColorChange,
            onOptionClick = onOptionClick,
        )
    }
}

@Composable
private fun ThemeSetting(
    settings: UserSetting,
    onDynamicColorChange: (Boolean) -> Unit,
    onOptionClick: (Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

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
        Text(text = settings.darkThemeConfig.name)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = AppIcons.next,
            contentDescription = "",
            modifier = Modifier.clickable { showDialog = true },
        )
    }

    AnimatedVisibility(visible = showDialog) {
        DarkModeDialog(
            selectedOption = settings.darkThemeConfig.ordinal,
            onDismissRequest = { showDialog = false },
            options = DarkThemeConfig.entries.map { it.name },
            onOptionClick = onOptionClick,
        )
    }
}

@Composable
private fun SettingItem(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun DarkModeDialog(
    selectedOption: Int,
    onDismissRequest: () -> Unit,
    options: List<String>,
    onOptionClick: (Int) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            options.forEachIndexed { index, str ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable {
                            onDismissRequest()
                            onOptionClick(index)
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = index == selectedOption,
                        onClick = {
                            onDismissRequest()
                            onOptionClick(index)
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
            windowSizeClass = previewWindowSizeClass(),
            onBackClick = { },
            settings = UserSetting(),
            onDynamicColorChange = {},
            onOptionClick = {},
        )
    }
}
