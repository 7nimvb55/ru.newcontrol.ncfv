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
public class ThFileListWorkBuild implements Runnable{
    private ThFileListRule ruleFileListBuild;
    
    ThFileListWorkBuild(final ThFileListRule outerRuleFileListBuild){
        this.ruleFileListBuild = outerRuleFileListBuild;
    }
    
    @Override
    public void run(){
        System.out.println(ThFileListWorkBuild.class.getCanonicalName() 
                + " run and say " 
                + this.ruleFileListBuild.toString());
        this.ruleFileListBuild.setTrueRunnedFileListWorkBuild();
        ThreadLocal<ThFileListLogicBuild> logicFileListWorkBuild = new ThreadLocal<ThFileListLogicBuild>();
        try{
            logicFileListWorkBuild.set(new ThFileListLogicBuild());
            logicFileListWorkBuild.get().doBuildToIndexFileList(this.ruleFileListBuild);
        } finally {
            logicFileListWorkBuild.remove();
            logicFileListWorkBuild = null;
            this.ruleFileListBuild.setFalseRunnedFileListWorkBuild();
        }
    }
}