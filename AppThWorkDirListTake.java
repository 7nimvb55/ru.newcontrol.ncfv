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

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThWorkDirListTake implements Runnable {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public AppThWorkDirListTake(AppThWorkDirListRule ruleForDirListWorkers) {
        super();
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    
    @Override
    public void run() {
        Boolean needFinishStateDirlistTacker = innerRuleForDirListWorkers.getNeedFinishStateDirlistTacker();
        this.innerRuleForDirListWorkers.startDirListPacker();
        System.out.println("Tacker run");
        do{
            System.out.println("Tacker wait for reader finished");
            //@todo all code to class
            do{
                try{
                    Thread.currentThread().sleep(5);
                } catch(InterruptedException ex){
                    System.out.println("Tacker sleep InterruptedException" + ex.getMessage());
                }
            }while( !this.innerRuleForDirListWorkers.isDirListReaderLogicRunned() );
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipeReaderToTacker = this.innerRuleForDirListWorkers.getWorkDirListState().getPipeReaderToTacker();
            System.out.println(pipeReaderToTacker.toString() + " size " + pipeReaderToTacker.size());
            if( pipeReaderToTacker != null){
                do{
                    ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> poll = pipeReaderToTacker.poll();
                    if( poll != null ){
                        System.out.println(poll.toString() + " size " + poll.size());
                    }
                } while( !pipeReaderToTacker.isEmpty() );

                
            } else {
                String strNullPipe = "pipeReaderToTacker is null";
                NcAppHelper.outToConsoleIfDevAndParamTrue(strNullPipe, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_TACKER_RUN);
            }
        }while( !this.innerRuleForDirListWorkers.isDirListReaderLogicFinished() );
    }
    
}
