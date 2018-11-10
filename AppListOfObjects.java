/*
 * Copyright 2018 wladimirowichbiaran.
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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppListOfObjects {
    private ConcurrentSkipListMap<String,Thread> currentWorkerList;
    private ArrayBlockingQueue<String> messagesQueueForLogging;
    
    private final Integer messagesQueueSize = 1000;

    public AppListOfObjects() {
        this.currentWorkerList = new ConcurrentSkipListMap<>();
        this.messagesQueueForLogging = new ArrayBlockingQueue<String>(messagesQueueSize);
        currentWorkerList.put(AppMsgEnPrefixes.TH_NAME_LOG, new AppLogger(messagesQueueForLogging));
    }
    protected ConcurrentSkipListMap<String,Thread> getWorkerList(){
        return currentWorkerList;
    }
    protected ArrayBlockingQueue<String> getLoggingQueue(){
        return messagesQueueForLogging;
    }
    protected Thread getLogger(){
        Thread getForReturn = currentWorkerList.get(AppMsgEnPrefixes.TH_NAME_LOG);
        if( getForReturn == null ){
            currentWorkerList.put(AppMsgEnPrefixes.TH_NAME_LOG, new AppLogger(messagesQueueForLogging));
            getForReturn = currentWorkerList.get(AppMsgEnPrefixes.TH_NAME_LOG);
        }
        return getForReturn;
    }
}
