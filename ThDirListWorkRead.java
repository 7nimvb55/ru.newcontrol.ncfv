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
public class ThDirListWorkRead implements Runnable{
    //private ThreadLocal<ThDirListRule> ruleDirListReadWork;
    private ThDirListRule ruleDirListReadWork;
    
    ThDirListWorkRead(final ThDirListRule outerRuleReader){
        //this.ruleDirListReadWork = new ThreadLocal<ThDirListRule>();
        //this.ruleDirListReadWork.set(outerRuleReader);
        this.ruleDirListReadWork = outerRuleReader;
    }
    
    @Override
    public void run(){
        System.out.println(ThDirListWorkRead.class.getCanonicalName() + " run and say " + this.ruleDirListReadWork.toString());
        this.ruleDirListReadWork.setTrueRunnedDirListWorkReader();
        ThreadLocal<ThDirListLogicRead> logicReader = new ThreadLocal<ThDirListLogicRead>();
        try{
            logicReader.set(new ThDirListLogicRead());
            /**
             * send to constructor state object, increment statistic, in manager thread look for count readed
             * records and call need sleep method in rule, logic in state object get and read this flag
             * in the if part and go to sleep
             * also for need stop
             */
            logicReader.get().doIndexStorage(this.ruleDirListReadWork.getDirListState().getBusJobForRead());
        } finally {
            logicReader.remove();
            this.ruleDirListReadWork.setFalseRunnedDirListWorkReader();
        }
    }
}
