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

import java.util.concurrent.ConcurrentHashMap;

/**
 * ThWordWorkStorageWalk - runnable, run logic folder scan and move
 * has switch who get from rule current job number, run method from logic
 * 
 * ThWordLogicStorageWalk - release logic for methods 
 *  - scan not limit files
 *  - scan limit files
 *  - scan all files
 * 
 *  - move files, directories
 *  - crete new directory by type
 *  - get list typed dirictories
 * 
 * ThWordRuleStorage - release method for run ...Walk and flags, return bus 
 * 
 * @author wladimirowichbiaran
 */
public class ThWordWorkStorageWalker implements Runnable{
    private ThWordRule ruleWordWriteWork;
    
    ThWordWorkStorageWalker(final ThWordRule outerRuleWriter){
        this.ruleWordWriteWork = outerRuleWriter;
    }
    
    @Override
    public void run(){
        System.out.println(ThWordWorkStorageWalker.class.getCanonicalName() 
                + " run and say " 
                + this.ruleWordWriteWork.toString());
        this.ruleWordWriteWork.setTrueRunnedWordWorkWriter();
        ThreadLocal<ThWordLogicStorageWalker> logicWordStorageWalker;
        logicWordStorageWalker = new ThreadLocal<ThWordLogicStorageWalker>();
        try{
            
            logicWordStorageWalker.set(new ThWordLogicStorageWalker());
            
            /**
             * switch by type of work from rule job value
             * code/decode functions in ThWordWorkStorageHelper
             */
            ConcurrentHashMap<Integer, Integer> currentWalkerStorageJob 
                    = this.ruleWordWriteWork.getWordState().getJobStorageWalker();
            //"jobWalkerStorageType".hashCode()
            switch (currentWalkerStorageJob.get(1390592002)) { 
                case 810653276:     // jobWalkerStorageType - scanNotLimited - hashCode
                    logicWordStorageWalker.get().doScanListNotLimitedFiles(this.ruleWordWriteWork,
                            currentWalkerStorageJob);
                    break;
                case 1718988707:     // jobWalkerStorageType - scanLimited - hashCode
                    logicWordStorageWalker.get().doScanListLimitedFiles(this.ruleWordWriteWork,
                            currentWalkerStorageJob);
                    break;
                case 1138849037:     // jobWalkerStorageType - scanAllFiles - hashCode
                    logicWordStorageWalker.get().doScanListAllFiles(this.ruleWordWriteWork,
                            currentWalkerStorageJob);
                    break;
                /**
                 * Not released by bus system, impossible sed in integer type
                 * data about moved objects, if send key for list in list of files
                 * in next release iterations
                 * <Integer, Integer> (jobWalkerStorageType, TypeWord)
                 * idea *** if in type word released to move object list with
                 * contained info about src, destination object names...
                 */    
                    
                //case 3:     // jobWalkerStorageType - moveFilesDirectories - hashCode
                    //logicWordStorageWalker.get().doMoveFilesDirectories(this.ruleWordWriteWork,
                    //        currentWalkerStorageJob);
                //    break;
                case 658413909:     // jobWalkerStorageType - createDirectoryTypeWord - hashCode
                    logicWordStorageWalker.get().doCreateDirectoryTypeWord(this.ruleWordWriteWork,
                            currentWalkerStorageJob);
                    break;
            }
                    
            
        } finally {
            logicWordStorageWalker.remove();
            logicWordStorageWalker = null;
            this.ruleWordWriteWork.setFalseRunnedWordWorkWriter();
        }
    }
    
}
