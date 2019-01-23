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
public class ThDirListWorkWrite implements Runnable{
    //private ThreadLocal<ThDirListRule> ruleDirListWriteWork;
    private ThDirListRule ruleDirListWriteWork;
    
    ThDirListWorkWrite(final ThDirListRule outerRuleWriter){
        //this.ruleDirListWriteWork = new ThreadLocal<ThDirListRule>();
        //this.ruleDirListWriteWork.set(outerRuleWriter);
        this.ruleDirListWriteWork = outerRuleWriter;
    }
    
    @Override
    public void run(){
        System.out.println(ThDirListWorkWrite.class.getCanonicalName() + " run and say " + this.ruleDirListWriteWork.toString());
        this.ruleDirListWriteWork.setTrueRunnedDirListWorkWriter();
        ThreadLocal<ThDirListLogicWrite> logicWriter = new ThreadLocal<ThDirListLogicWrite>();
        try{
            
        } finally {
            logicWriter.remove();
            this.ruleDirListWriteWork.setFalseRunnedDirListWorkWriter();
        }
    }
}
