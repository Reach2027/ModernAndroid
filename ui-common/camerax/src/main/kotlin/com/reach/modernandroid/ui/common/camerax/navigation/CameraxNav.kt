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

package com.reach.modernandroid.ui.common.camerax.navigation

import androidx.navigation.NavGraphBuilder
import com.reach.modernandroid.ui.base.common.navigation.CommonRoute
import com.reach.modernandroid.ui.base.common.navigation.screenComposable
import com.reach.modernandroid.ui.common.camerax.CameraxRoute

fun NavGraphBuilder.cameraxRoute() {
    screenComposable(
        route = CommonRoute.CAMERAX,
    ) {
        CameraxRoute()
    }
}
