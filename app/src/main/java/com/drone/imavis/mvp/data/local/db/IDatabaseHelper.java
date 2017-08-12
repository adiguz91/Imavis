/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.drone.imavis.mvp.data.local.db;

import com.drone.imavis.mvp.data.model.FlyPlan;

import java.util.List;

import io.reactivex.Observable;

public interface IDatabaseHelper {

    Observable<List<FlyPlan>> getAllFlyPlans();

    Observable<FlyPlan> getFlyPlan(final Long id);

    Observable<Boolean> updateFlyPlan(final FlyPlan flyPlan);

    Observable<Long> createFlyPlan(final FlyPlan flyPlan);

    Observable<Boolean> deleteFlyPlan(final FlyPlan flyPlan);
}
