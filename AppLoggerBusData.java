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
public class AppLoggerBusData {
    private ConcurrentSkipListMap<String, ArrayBlockingQueue<String>> writerList;
    AppLoggerBusData(){
        this.writerList = new ConcurrentSkipListMap<String, ArrayBlockingQueue<String>>();
    }
    protected ArrayBlockingQueue<String> getByKey(String keyForGet){
        return writerList.get(keyForGet);
    }
    protected String addAndGetKey(ArrayBlockingQueue<String> elementForAdd){
        String strKey = AppFileOperationsSimple.getNowTimeStringWithMS() + "-" + String.valueOf(this.writerList.size());
        this.writerList.put(strKey, elementForAdd);
        return strKey;
    }
    protected String newAndGetKey(Integer queueSize){
        String strKey = AppFileOperationsSimple.getNowTimeStringWithMS() + "-" + String.valueOf(this.writerList.size());
        ArrayBlockingQueue<String> queueElements = new ArrayBlockingQueue<String>(queueSize);
        this.writerList.put(strKey, queueElements);
        return strKey;
    }
}
