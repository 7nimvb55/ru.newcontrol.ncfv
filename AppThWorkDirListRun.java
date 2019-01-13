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

    public AppThWorkDirListRun(final AppThWorkDirListRule ruleForDirListWorkers) {
        super();
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    
    @Override
    public void run() {
        this.innerRuleForDirListWorkers.startDirlistTacker();
        Boolean needFinishStateDirlistReader = innerRuleForDirListWorkers.getNeedFinishStateDirlistReader();
        Path currentPathForMakeIndex = this.innerRuleForDirListWorkers.getCurrentPathForMakeIndex();
        try{
            ThreadLocal<ThLogicDirListWalker> logicWalker = new ThreadLocal<ThLogicDirListWalker>();
            logicWalker.set(new ThLogicDirListWalker(this.innerRuleForDirListWorkers));
            logicWalker.get().doReadFsToPipe();
            NcAppHelper.outToConsoleIfDevAndParamTrue("ThLogicDirListWalker.doReadFsToPipe end", 
                    AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_WALKER_DO_READ_FS_TO_PIPE);
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
}
