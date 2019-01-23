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
        /**
         * ThDirListWorkRead implements Runnable
         * ThDirListWorkWrite implements Runnable
         * create and set in ThDirListRule
         * also send ThDirListRule into ThDirListManager constructor
         */
            ThreadLocal<ThDirListWorkRead> thDirListWorkRead = new ThreadLocal<ThDirListWorkRead>();
            ThreadLocal<ThDirListWorkWrite> thDirListWorkWrite = new ThreadLocal<ThDirListWorkWrite>();
            ThreadLocal<ThDirListLogicManager> thDirListManager = new ThreadLocal<ThDirListLogicManager>();
        try{    
            ThDirListBusReaded thDirListBusReaded = new ThDirListBusReaded();
            thDirListBusDataReaded.set(thDirListBusReaded);
            ThDirListBusWrited thDirListBusWrited = new ThDirListBusWrited();
            thDirListBusDataWrited.set(thDirListBusWrited);
            
            ThDirListRule thDirListRuleObject = new ThDirListRule();
            System.out.println(thDirListRuleObject.toString());
            thDirListRule.set(thDirListRuleObject);
            
            ThDirListState thDirListStateObject = new ThDirListState();
            thDirListState.set(thDirListStateObject);
            
            thDirListState.get().setBusJobForRead(thDirListBusDataReaded.get());
            thDirListState.get().setBusJobForWrite(thDirListBusDataWrited.get());
            
            thDirListRule.get().setDirListState(thDirListState.get());
            //Statistic into State
            //
            
            ThDirListStatistic thDirListStatisticObject = new ThDirListStatistic();
            thDirListStatistic.set(thDirListStatisticObject);
            thDirListRule.get().setDirListCounter(thDirListStatistic.get());
            /**
             * @todo
             * ThDirListBusReaded Queue set into ThDirListState
             * ThDirListBusWrited Queue set into ThDirListState
             * ThDirListRule insert into all Runnable workers by his constructor
             * after create Runnable workers set him in ThDirListRule class fields
             * ThDirListManager implements from Runnable
             * ThDirListManager.doIndexStorage() release in ThDirListLogicManager
             * insert from ThDirListLogicManager queue into ThDirListBusReaded
             * release in ThDirListWorkRead and ThDirListLogicRead job execution
             * from ThDirListBusReaded Queue provided by ThDirListRule
             */
            
            thDirListWorkRead.set(new ThDirListWorkRead(thDirListRuleObject));
            thDirListRule.get().setDirListWorkReader(thDirListWorkRead.get());
            thDirListWorkWrite.set(new ThDirListWorkWrite(thDirListRuleObject));
            thDirListRule.get().setDirListWorkWriter(thDirListWorkWrite.get());
            
            thDirListRule.get().runReadFromDirList();
            
            thDirListRule.get().runWriteToDirList();
            
            //need run into ThDirListWorkerManager
            thDirListManager.set(new ThDirListLogicManager());
            thDirListManager.get().doIndexStorage();
            
        } finally {
            thDirListBusDataReaded.remove();
            thDirListBusDataWrited.remove();
            thDirListRule.remove();
            thDirListState.remove();
            thDirListStatistic.remove();
            thDirListWorkRead.remove();
            thDirListWorkWrite.remove();
            thDirListManager.remove();  
        }
    }
    
}
