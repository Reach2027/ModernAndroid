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

package com.reach.base.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val coroutineScopeModule = module {
    includes(dispatcherModule)

    single<CoroutineScope>(qualifier(QualifierCoroutineScope.AppDefault)) {
        val dispatcher: CoroutineDispatcher = get(qualifier(QualifierDispatchers.Default))
        CoroutineScope(SupervisorJob() + dispatcher)
    }

    single<CoroutineScope>(qualifier(QualifierCoroutineScope.AppIo)) {
        val dispatcher: CoroutineDispatcher = get(qualifier(QualifierDispatchers.IO))
        CoroutineScope(SupervisorJob() + dispatcher)
    }

    factory<CoroutineScope>(qualifier(QualifierCoroutineScope.OneParallelismDefault)) {
        val dispatcher: CoroutineDispatcher = get(qualifier(QualifierDispatchers.Default))
        CoroutineScope(SupervisorJob() + dispatcher.limitedParallelism(1))
    }

    factory<CoroutineScope>(qualifier(QualifierCoroutineScope.OneParallelismIo)) {
        val dispatcher: CoroutineDispatcher = get(qualifier(QualifierDispatchers.IO))
        CoroutineScope(SupervisorJob() + dispatcher.limitedParallelism(1))
    }
}
