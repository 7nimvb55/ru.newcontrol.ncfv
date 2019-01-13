/*
 * Copyright 2018 wladimirowichbiaran.
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

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThWorkDirListRun implements Runnable {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public AppThWorkDirListRun(AppThWorkDirListRule ruleForDirListWorkers) {
        super();
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    
    @Override
    public void run() {
        Boolean needFinishStateDirlistReader = innerRuleForDirListWorkers.getNeedFinishStateDirlistReader();
        Path currentPathForMakeIndex = this.innerRuleForDirListWorkers.getCurrentPathForMakeIndex();
        try{
            ThLogicDirListWalker logicWalker = new ThLogicDirListWalker(this.innerRuleForDirListWorkers);
            logicWalker.doReadFsToPipe();
        } catch(IOException ex){
            ex.printStackTrace();
        }

        this.innerRuleForDirListWorkers.startDirlistTacker();
        
    }
    
}
