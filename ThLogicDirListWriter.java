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
public class ThLogicDirListWriter {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public ThLogicDirListWriter(AppThWorkDirListRule ruleForDirListWorkers) {
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    protected void doWriter(){
        do{
            this.innerRuleForDirListWorkers.setDirListWriterLogicRunned();
        } while( !this.innerRuleForDirListWorkers.isDirListWriterLogicRunned() );
        final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipePackerToWriter = 
                this.innerRuleForDirListWorkers.getWorkDirListState().getPipePackerToWriter();
        outStatesOfWorkLogic(" Writer start run part");
        do{
            outStatesOfWorkLogic(" Writer start part wait for Packer finished");
            //@todo all code to class
            do{
                try{
                    Thread.currentThread().sleep(5);
                } catch(InterruptedException ex){
                    outStatesOfWorkLogic(" sleep is interrupted with message" + ex.getMessage());
                }
            }while( !this.innerRuleForDirListWorkers.isDirListPackerLogicRunned() );
            
            
            outStatesOfWorkLogic(pipePackerToWriter.toString() + " size " + pipePackerToWriter.size());
            if( pipePackerToWriter != null){
                do{
                    ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> poll = pipePackerToWriter.poll();
                    if( poll != null ){
                        outStatesOfWorkLogic(" polled from pipePackerToWriter size is " + poll.size());
                    }
                } while( !pipePackerToWriter.isEmpty() );
            } else {
                outStatesOfWorkLogic(" pipePackerToWriter is null");
            }
        }while( !this.innerRuleForDirListWorkers.isDirListPackerLogicFinished() );
        do{
            this.innerRuleForDirListWorkers.setDirListWriterLogicFinished();
        } while( !this.innerRuleForDirListWorkers.isDirListWriterLogicFinished() );
        outStatesOfWorkLogic(" Writer end run part");
    }
    private void outStatesOfWorkLogic(String strForOutPut){
        String strRunLogicLabel = ThLogicDirListPacker.class.getCanonicalName() 
                            + "[THREADNAME]" + Thread.currentThread().getName()
                            + strForOutPut;
        NcAppHelper.outToConsoleIfDevAndParamTrue(strRunLogicLabel, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_WRITER_RUN);
    }
}
