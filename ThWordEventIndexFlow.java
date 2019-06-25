/*
 * Copyright 2019 wladimirowichbiaran.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.newcontrol.ncfv;

import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordEventIndexFlow {
    private ConcurrentSkipListMap<UUID, UUID> workers;
    private ConcurrentSkipListMap<UUID, UUID> activity;
    private ConcurrentSkipListMap<UUID, UUID> dataCache;
    private ConcurrentSkipListMap<UUID, UUID> dataFs;
    private ConcurrentSkipListMap<UUID, UUID> error;
    private ConcurrentSkipListMap<UUID, UUID> name;
    ThWordEventIndexFlow(){
        this.workers = new ConcurrentSkipListMap<UUID, UUID>();
        this.activity = new ConcurrentSkipListMap<UUID, UUID>();
        this.dataCache = new ConcurrentSkipListMap<UUID, UUID>();
        this.dataFs = new ConcurrentSkipListMap<UUID, UUID>();
        this.error = new ConcurrentSkipListMap<UUID, UUID>();
        this.name = new ConcurrentSkipListMap<UUID, UUID>();
    }

    public UUID getWorkers(UUID mainFlowInputed) {
        return workers.get(mainFlowInputed);
    }

    public void setWorkers(UUID mainFlowInputed, UUID workersInputed) {
        this.workers.put(mainFlowInputed, workersInputed);
    }

    public UUID getActivity(UUID mainFlowInputed) {
        return activity.get(mainFlowInputed);
    }

    public void setActivity(UUID mainFlowInputed, UUID activityInputed) {
        this.activity.put(mainFlowInputed, activityInputed);
    }

    public UUID getDataCache(UUID mainFlowInputed) {
        return dataCache.get(mainFlowInputed);
    }

    public void setDataCache(UUID mainFlowInputed, UUID dataCacheInputed) {
        this.dataCache.put(mainFlowInputed, dataCacheInputed);
    }

    public UUID getDataFs(UUID mainFlowInputed) {
        return dataFs.get(mainFlowInputed);
    }

    public void setDataFs(UUID mainFlowInputed, UUID dataFsInputed) {
        this.dataFs.put(mainFlowInputed, dataFsInputed);
    }

    public UUID getError(UUID mainFlowInputed) {
        return error.get(mainFlowInputed);
    }

    public void setError(UUID mainFlowInputed, UUID errorInputed) {
        this.error.put(mainFlowInputed, errorInputed);
    }

    public UUID getName(UUID mainFlowInputed) {
        return name.get(mainFlowInputed);
    }

    public void setName(UUID mainFlowInputed, UUID nameInputed) {
        this.name.put(mainFlowInputed, nameInputed);
    }
}
