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
 * AdifControl
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>flow
 * <li>Control
 * <ul>
 * Create UUID for new runner by process number
 * change process logic flags by Created UUID
 * see also AdifStorage for generate and temp save file names by process
 * see also AdifData for generate and temp save flags for process data in logic
 * {@code <UUID Thread.name, Integer AdihHelper.getProcessNames>}
 * 
 * <p>see also {@link ru.newcontrol.ncfv.AdihTemplateThread#getName() AdihTemplateThread extends Thread.getName()}
 * <p>see also {@link ru.newcontrol.ncfv.AdihHelper#getUuidWorkerFromName(ru.newcontrol.ncfv.AdihTemplateThread) AdihHelper.getUuidWorkerFromName() }
 * <p>see also {@link ru.newcontrol.ncfv.AdihHelper#getProcessNames() AdihHelper.getProcessNames() }
 * @author wladimirowichbiaran
 */
public class AdifControl {
    private final ConcurrentSkipListMap<Integer, UUID> processNumberedList;
    public AdifControl(){
        this.processNumberedList = new ConcurrentSkipListMap<Integer, UUID>();
    }
}
