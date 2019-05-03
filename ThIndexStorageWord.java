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
        ThStorageWordBusReader thStorageLongWordBusReader = new ThStorageWordBusReader();
        //init State
        ThStorageWordState thStorageWordState = new ThStorageWordState();
        thStorageWordState.setBusJobForStorageWordRouterJob(thStorageWordBusInput);
        thStorageWordState.setBusJobForWordWrite(thStorageWordBusOutput);
        thStorageWordState.setBusJobForLongWordWrite(thStorageLongWordBusOutput);
        thStorageWordState.setBusJobForStorageWordRouterJobToWriter(thStorageLongWordBusWriter);
        thStorageWordState.setBusJobForStorageWordRouterJobToReader(thStorageLongWordBusReader);
        ThStorageWordStatusMainFlow thStorageWordStatusMainFlow = new ThStorageWordStatusMainFlow();
        ThStorageWordBusReadedFlow thStorageWordFlowRead = new ThStorageWordBusReadedFlow(thStorageWordStatusMainFlow);
        //init Rule
        ThStorageWordRule thStorageWordRule = new ThStorageWordRule(this.ruleThIndex);
        thStorageWordRule.setStorageWordState(thStorageWordState);
        thStorageWordRule.setStorageWordStatusMainFlow(thStorageWordStatusMainFlow);
        thStorageWordState.setStorageWordFlowReaded(thStorageWordFlowRead);
        //init Workers
        ThStorageWordWorkFilter thStorageWordWorkFilter = new ThStorageWordWorkFilter(thStorageWordRule);
        thStorageWordRule.setStorageWordWorkFilter(thStorageWordWorkFilter);
        ThStorageWordWorkRouter thStorageWordWorkRouter = new ThStorageWordWorkRouter(thStorageWordRule);
        thStorageWordRule.setStorageWordWorkRouter(thStorageWordWorkRouter);
        ThStorageWordWorkWrite thStorageWordWorkWrite = new ThStorageWordWorkWrite(thStorageWordRule);
        thStorageWordRule.setStorageWordWorkWrite(thStorageWordWorkWrite);
        ThStorageWordWorkRead thStorageWordWorkRead = new ThStorageWordWorkRead(thStorageWordRule);
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
