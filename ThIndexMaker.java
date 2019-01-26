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

import java.util.UUID;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThIndexMaker extends Thread{
    private ThIndexRule ruleThIndex;
    
    ThIndexMaker(final ThIndexRule outerRule){
        super(UUID.randomUUID().toString());
        this.ruleThIndex = outerRule;
    }
    @Override
    public void run(){
        this.ruleThIndex.setTrueRunnedThreadIndexMaker();
        AppObjectsList objectsForApp = new AppObjectsList();
        //@todo AppThManager, AppObjectsManagerState create one it two or... ?
        AppThManager loggerByThreadsMain = new AppThManager(objectsForApp);
        Ncfv.logInitState(loggerByThreadsMain);
        AppObjectsManagerState withOutLogger = new AppObjectsManagerState(loggerByThreadsMain);
        withOutLogger.runWorkMakeDirList();
        loggerByThreadsMain.getListOfObjects().getWorkerList().clear();
        //runVersionOfAppBeforeThreadsInUse(args);
        this.ruleThIndex.setFalseRunnedThreadIndexMaker();
    }
}
