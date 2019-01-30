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
public class ThWordWorkBuild implements Runnable{
    private ThWordRule ruleWordBuildWork;
    
    ThWordWorkBuild(final ThWordRule outerRuleBuildr){
        this.ruleWordBuildWork = outerRuleBuildr;
    }
    
    @Override
    public void run(){
        System.out.println(ThWordWorkBuild.class.getCanonicalName() + " run and say " + this.ruleWordBuildWork.toString());
        this.ruleWordBuildWork.setTrueRunnedWordWorkBuild();
        ThreadLocal<ThWordLogicBuild> logicBuildr = new ThreadLocal<ThWordLogicBuild>();
        try{
            logicBuildr.set(new ThWordLogicBuild());
            logicBuildr.get().doWordIndex(this.ruleWordBuildWork);
        } finally {
            logicBuildr.remove();
            this.ruleWordBuildWork.setFalseRunnedWordWorkBuild();
        }
    }
}
