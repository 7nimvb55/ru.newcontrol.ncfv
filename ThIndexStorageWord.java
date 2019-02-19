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
public class ThIndexStorageWord extends Thread{
    private ThIndexRule ruleThIndex;
    
    ThIndexStorageWord(ThIndexRule outerRule){
        super(UUID.randomUUID().toString());
        this.ruleThIndex = outerRule;
        //Thread.currentThread().setName(UUID.randomUUID().toString());
    }
    
    @Override
    public void run(){
        System.out.println(ThIndexStorageWord.class.getCanonicalName() 
                + " do it +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        ThIndexState indexState = this.ruleThIndex.getIndexState();
        //init Bus
        ThStorageWordBusInput thStorageWordBusInput = new ThStorageWordBusInput();
        ThStorageWordBusOutput thStorageWordBusOutput = new ThStorageWordBusOutput();
        ThStorageWordBusOutput thStorageLongWordBusOutput = new ThStorageWordBusOutput();
        ThStorageWordBusWriter thStorageLongWordBusWriter = new ThStorageWordBusWriter();
        //init State
        ThStorageWordState thStorageWordState = new ThStorageWordState();
        thStorageWordState.setBusJobForStorageWordRouterJob(thStorageWordBusInput);
        thStorageWordState.setBusJobForWordWrite(thStorageWordBusOutput);
        thStorageWordState.setBusJobForLongWordWrite(thStorageLongWordBusOutput);
        thStorageWordState.setBusJobForStorageWordRouterJobToWriter(thStorageLongWordBusWriter);
        ThStorageWordStatistic thStorageWordStatistic = new ThStorageWordStatistic();
        //init Rule
        ThStorageWordRule thStorageWordRule = new ThStorageWordRule(this.ruleThIndex);
        //init Workers
        ThStorageWordWorkFilter thStorageWordWorkFilter = new ThStorageWordWorkFilter(thStorageWordRule);
        ThStorageWordWorkRouter thStorageWordWorkRouter = new ThStorageWordWorkRouter(thStorageWordRule);
        ThStorageWordWorkWrite thStorageWordWorkWrite = new ThStorageWordWorkWrite(thStorageWordRule);
        ThStorageWordWorkRead thStorageWordWorkRead = new ThStorageWordWorkRead(thStorageWordRule);
        
        thStorageWordRule.setStorageWordState(thStorageWordState);
        thStorageWordRule.setStorageWordStatistic(thStorageWordStatistic);
        thStorageWordRule.setStorageWordWorkFilter(thStorageWordWorkFilter);
        thStorageWordRule.setStorageWordWorkRouter(thStorageWordWorkRouter);
        thStorageWordRule.setStorageWordWorkWrite(thStorageWordWorkWrite);
        thStorageWordRule.setStorageWordWorkRead(thStorageWordWorkRead);
        //set StorageWord Rule in indexState
        indexState.setRuleStorageWord(thStorageWordRule);
        
        // run Workers
        thStorageWordRule.runFilterStorageWordWork();
        thStorageWordRule.runRouterStorageWordWork();
        thStorageWordRule.runReadStorageWordWork();
        thStorageWordRule.runWriteStorageWordWork();
    }
    
}
