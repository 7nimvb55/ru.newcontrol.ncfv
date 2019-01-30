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

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordWorkWrite implements Runnable{
    private ThWordRule ruleWordWriteWork;
    
    ThWordWorkWrite(final ThWordRule outerRuleWriter){
        this.ruleWordWriteWork = outerRuleWriter;
    }
    
    @Override
    public void run(){
        System.out.println(ThWordWorkWrite.class.getCanonicalName() + " run and say " + this.ruleWordWriteWork.toString());
        this.ruleWordWriteWork.setTrueRunnedWordWorkWriter();
        ThreadLocal<ThWordLogicWrite> logicWriter = new ThreadLocal<ThWordLogicWrite>();
        try{
            logicWriter.set(new ThWordLogicWrite());
            logicWriter.get().doWriteJobFromBusForWord(this.ruleWordWriteWork);
        } finally {
            logicWriter.remove();
            this.ruleWordWriteWork.setFalseRunnedWordWorkWriter();
        }
    }
}
