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

/**
 *
 * @author wladimirowichbiaran
 */
public class ThIndexDirList extends Thread{
    
    ThIndexDirList(){
        Thread.currentThread().setName(UUID.randomUUID().toString());
    }
    
    @Override
    public void run(){
            ThreadLocal<ThDirListBusReaded> thDirListBusDataReaded = new ThreadLocal<ThDirListBusReaded>();
            ThreadLocal<ThDirListBusWrited> thDirListBusDataWrited = new ThreadLocal<ThDirListBusWrited>();
            ThreadLocal<ThDirListRule> thDirListRule = new ThreadLocal<ThDirListRule>();
            ThreadLocal<ThDirListState> thDirListState = new ThreadLocal<ThDirListState>();
            ThreadLocal<ThDirListStatistic> thDirListStatistic = new ThreadLocal<ThDirListStatistic>();
            //Rule create
            //State create
            ThreadLocal<ThDirListManager> thDirListManager = new ThreadLocal<ThDirListManager>();
        try{    
            thDirListBusDataReaded.set(new ThDirListBusReaded());
            thDirListBusDataWrited.set(new ThDirListBusWrited());
            thDirListRule.set(new ThDirListRule());
            thDirListState.set(new ThDirListState());
            thDirListStatistic.set(new ThDirListStatistic());
            thDirListManager.set(new ThDirListManager());
            thDirListManager.get().doIndexStorage();
        } finally {
            thDirListBusDataReaded.remove();
            thDirListBusDataWrited.remove();
            thDirListManager.remove();  
        }
    }
    
}
