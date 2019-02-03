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
public class ThStorageWordWorkRead implements Runnable{
    private ThStorageWordRule ruleStorageWordRead;
    
    ThStorageWordWorkRead(final ThStorageWordRule outerRuleStorageWordRead){
        this.ruleStorageWordRead = outerRuleStorageWordRead;
    }
    
    @Override
    public void run(){
        System.out.println(ThStorageWordWorkRead.class.getCanonicalName() 
                + " run and say " 
                + this.ruleStorageWordRead.toString());
        this.ruleStorageWordRead.setTrueRunnedStorageWordWorkRead();
        ThreadLocal<ThStorageWordLogicRead> logicStorageWordWorkRead = new ThreadLocal<ThStorageWordLogicRead>();
        try{
            logicStorageWordWorkRead.set(new ThStorageWordLogicRead());
            logicStorageWordWorkRead.get().doReadFromIndexStorageWord(this.ruleStorageWordRead);
        } finally {
            logicStorageWordWorkRead.remove();
            logicStorageWordWorkRead = null;
            this.ruleStorageWordRead.setFalseRunnedStorageWordWorkRead();
        }
    }
}
