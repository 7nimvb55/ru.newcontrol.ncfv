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
public class ThWordWorkRead implements Runnable {
    private ThWordRule ruleWordRead;
    
    ThWordWorkRead(final ThWordRule outerRuleWordRead){
        this.ruleWordRead = outerRuleWordRead;
    }
    
    @Override
    public void run(){
        System.out.println(ThWordWorkRead.class.getCanonicalName() 
                + " run and say " 
                + this.ruleWordRead.toString());
        this.ruleWordRead.setTrueRunnedWordWorkRead();
        ThreadLocal<ThWordLogicRead> logicWordWorkRead = new ThreadLocal<ThWordLogicRead>();
        try{
            logicWordWorkRead.set(new ThWordLogicRead());
            logicWordWorkRead.get().doReadFromIndexWord(this.ruleWordRead);
        } finally {
            logicWordWorkRead.remove();
            logicWordWorkRead = null;
            this.ruleWordRead.setFalseRunnedWordWorkRead();
        }
    }
}
