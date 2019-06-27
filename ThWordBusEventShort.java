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
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordBusEventShort {
    /**
     * {@code <vectorCodeName, <mainFlowUUID>>}
     */
    private ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> shortEventsBus;
    ThWordBusEventShort(){
        this.shortEventsBus = new ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>>();
    }
    private void setShortEventBus(Integer codeEventName, LinkedTransferQueue<UUID> listEvents){
        //if settable value == null not set
        //if constainKey where keyCodeEventName not set
    }
    /**
     * rules logic:
     * states see in state descr: event... wait... do...
     * setStopFlag(int, UUID) into wait only
     * moveFlagInTo(int, UUID) from wait to do after finished 
     *  - into event or next logic level
     * deleteFinishedFlag(int, UUID)
     * - getEvent... from state
     */
}
