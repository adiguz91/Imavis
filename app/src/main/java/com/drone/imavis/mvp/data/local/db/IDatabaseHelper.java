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
import com.drone.imavis.mvp.data.model.Project;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IDatabaseHelper {

    /* FLYPLAN */

    Observable<List<FlyPlan>> getAllFlyplans();

    Single<FlyPlan> getFlyplan(final Long id);

    Completable updateFlyplan(final FlyPlan flyplan);

    Single<Long> createFlyplan(final FlyPlan flyplan);

    Completable deleteFlyplan(final FlyPlan flyplan);

    /* PROJECT */

    Observable<List<Project>> getAllProjects();

    Single<Project> getProject(final Long id);

    Completable updateProject(final Project project);

    Single<Long> createProject(final Project project);

    Completable deleteProject(final Project project);
}
