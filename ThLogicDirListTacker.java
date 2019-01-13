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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThLogicDirListTacker {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public ThLogicDirListTacker(AppThWorkDirListRule ruleForDirListWorkers) {
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    protected void doTacker(){
        outStatesOfWorkLogic(" Tacker start run part");
        do{
            outStatesOfWorkLogic(" Tacker start part wait for reader finished");
            //@todo all code to class
            do{
                try{
                    Thread.currentThread().sleep(5);
                } catch(InterruptedException ex){
                    outStatesOfWorkLogic(" sleep is interrupted with message" + ex.getMessage());
                }
            }while( !this.innerRuleForDirListWorkers.isDirListReaderLogicRunned() );
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipeReaderToTacker = this.innerRuleForDirListWorkers.getWorkDirListState().getPipeReaderToTacker();
            outStatesOfWorkLogic(pipeReaderToTacker.toString() + " size " + pipeReaderToTacker.size());
            if( pipeReaderToTacker != null){
                do{
                    ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> poll = pipeReaderToTacker.poll();
                    if( poll != null ){
                        outStatesOfWorkLogic(" polled from pipeReaderToTacker size is " + poll.size());
                    }
                } while( !pipeReaderToTacker.isEmpty() );
            } else {
                outStatesOfWorkLogic(" pipeReaderToTacker is null");
            }
        }while( !this.innerRuleForDirListWorkers.isDirListReaderLogicFinished() );
    }
    private void outStatesOfWorkLogic(String strForOutPut){
        String strRunLogicLabel = AppThWorkDirListTake.class.getCanonicalName() 
                            + "[THREADNAME]" + Thread.currentThread().getName()
                            + strForOutPut;
        NcAppHelper.outToConsoleIfDevAndParamTrue(strRunLogicLabel, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_TACKER_RUN);
    }
}
