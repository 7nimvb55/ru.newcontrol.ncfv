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
public class ThIndexWord extends Thread{
    private ThIndexRule ruleThIndex;
    ThIndexWord(ThIndexRule outerRule){
        super(UUID.randomUUID().toString());
        this.ruleThIndex = outerRule;
        //Thread.currentThread().setName(UUID.randomUUID().toString());
    }
    
    @Override
    public void run(){
        System.out.println(ThIndexWord.class.getCanonicalName() 
                + " do it +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        ThIndexState indexState = this.ruleThIndex.getIndexState();
        ThWordRule thWordRule = new ThWordRule(this.ruleThIndex);
        //init State
        ThWordStatusMainFlow wordStatusMainFlow = new ThWordStatusMainFlow();
        thWordRule.setWordStatusMainFlow(wordStatusMainFlow);
        ThWordBusFlowEvent busWordRouterJobToReaderOuter = new ThWordBusFlowEvent(wordStatusMainFlow);
        ThWordBusFlowEvent busWordRouterJobToWriterOuter = new ThWordBusFlowEvent(wordStatusMainFlow);
        ThWordBusFlowEvent stateWordReadedOuter = new ThWordBusFlowEvent(wordStatusMainFlow);
        ThWordState thWordState = new ThWordState(thWordRule);
        thWordState.setBusJobForWordRouterJobToReader(busWordRouterJobToReaderOuter);
        thWordState.setBusJobForWordRouterJobToWriter(busWordRouterJobToWriterOuter);
        thWordState.setWordFlowReaded(stateWordReadedOuter);
        ThWordBusReadedFlow thWordFlowRead = new ThWordBusReadedFlow(wordStatusMainFlow);
        //init Rule
        
        thWordRule.setWordState(thWordState);
        
        
        //init Workers
        
        ThWordWorkRouter thWordWorkRouter = new ThWordWorkRouter(thWordRule);
        thWordRule.setWordWorkRouter(thWordWorkRouter);
        ThWordWorkWrite thWordWorkWrite = new ThWordWorkWrite(thWordRule);
        thWordRule.setWordWorkWrite(thWordWorkWrite);
        ThWordWorkRead thWordWorkRead = new ThWordWorkRead(thWordRule);
        thWordRule.setWordWorkRead(thWordWorkRead);
        
        //set Word Rule in indexState
        
        // run Workers
        thWordRule.runRouterWordWork();
        thWordRule.runReadWordWork();
        thWordRule.runWriteWordWork();
        System.out.println(thWordWorkRouter.toString()
                + " do it +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        
    }
    
}
