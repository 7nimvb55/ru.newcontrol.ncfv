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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThLogicDirListPacker {
    private AppThWorkDirListRule innerRuleForDirListWorkers;
    private ThreadLocal<Long> counterReadedData;
    private ThreadLocal<Long> counterWritedData;

    public ThLogicDirListPacker(AppThWorkDirListRule ruleForDirListWorkers) {
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    protected void doPacker(){
        this.counterReadedData = new ThreadLocal<Long>();
        this.counterReadedData.set(0L);
        
        this.counterWritedData = new ThreadLocal<Long>();
        this.counterWritedData.set(0L);
        
        do{
            this.innerRuleForDirListWorkers.setDirListPackerLogicRunned();
        } while( !this.innerRuleForDirListWorkers.isDirListPackerLogicRunned() );
        final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipeTackerToPacker = 
                this.innerRuleForDirListWorkers.getWorkDirListState().getPipeTackerToPacker();
        
        final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipePackerToWriter = 
                                this.innerRuleForDirListWorkers.getWorkDirListState().getPipePackerToWriter();
        
        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> packetForOut = new ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>();
        
        outStatesOfWorkLogic(" +++++ Packer start run part");
        do{
            outStatesOfWorkLogic(" +++++ Packer start part wait for Tacker finished");
            //@todo all code to class
            do{
                try{
                    Thread.currentThread().sleep(5);
                } catch(InterruptedException ex){
                    outStatesOfWorkLogic(" sleep is interrupted with message" + ex.getMessage());
                }
            }while( !this.innerRuleForDirListWorkers.isDirListTackerLogicRunned() );
            
            if( AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_PACKER_PIPE_TO_STRING ){
                outStatesOfWorkLogic(pipeTackerToPacker.toString() + " +++++ size " + pipeTackerToPacker.size());
            }
            if( pipeTackerToPacker != null){
                // @todo need fix
                if( !pipeTackerToPacker.isEmpty() ){
                    do{
                        
                        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> pollFromTacker = null;
                        
                        //ReentrantLock forGetDataFromPipeTackerToPacker = new ReentrantLock();
                        //forGetDataFromPipeTackerToPacker.lock();
                        //try{
                            pollFromTacker = pipeTackerToPacker.poll();
                        //} finally {
                        //    forGetDataFromPipeTackerToPacker.unlock();
                        //}
                        
                        if( pollFromTacker != null ){
                            Long tmpSum = this.counterReadedData.get() + (long) pollFromTacker.size();
                            this.counterReadedData.set( tmpSum );
                            pipeTackerToPacker.add(pollFromTacker);
                            outStatesOfWorkLogic(" Packer side _*_*_*_*_*_ polled from pipeTackerToPacker size is " 
                                    + pipeTackerToPacker.size() 
                                    + " _+_+_+_+_+_+_+_+_+_ all recivied size "
                                    + this.counterReadedData.get()
                                    + " from TACKER"
                            );
                            
                            do{
                                outStatesOfWorkLogic(" +P+A+C+K+++S+I+D+E+ polled from pipeTackerToPacker size is "
                                        + "-|||-      " + pollFromTacker.size());
                                if( (pollFromTacker.size() + packetForOut.size()) < 101 ){
                                    packetForOut.putAll(pollFromTacker);
                                    
                                    Long tmpSumWrite = this.counterWritedData.get() + (long) pollFromTacker.size();
                                    this.counterWritedData.set( tmpSumWrite );
                                    
                                    pollFromTacker.clear();
                                }
                                if( (pollFromTacker.size() + packetForOut.size()) > 100 ){
                                    do{
                                        Map.Entry<UUID, TdataDirListFsObjAttr> pollFirstEntry = pollFromTacker.pollFirstEntry();
                                        if( pollFirstEntry != null ){
                                            packetForOut.put(pollFirstEntry.getKey(), pollFirstEntry.getValue());
                                            
                                            Long tmpSumWrite = this.counterWritedData.get() + 1L;
                                            this.counterWritedData.set( tmpSumWrite );
                                            
                                        }
                                    } while ( packetForOut.size() != 100 );
                                }

                                if( packetForOut.size() == 100 ){
                                    pipePackerToWriter.add(packetForOut);
                                    outStatesOfWorkLogic(" +P+A+C+K+++S+I+D+E+ TRANSFERED "
                                        + "-|******|******|******|-" + packetForOut.size());
                                    packetForOut.clear();
                                }
                            } while( !pollFromTacker.isEmpty() );    
                        }
                        outDataProcessedOfWorkLogic(this.counterReadedData.get(), 
                                this.counterWritedData.get(), 
                                (long) packetForOut.size());
                        
                    } while( !pipeTackerToPacker.isEmpty() );
                } else {
                    try{
                        outStatesOfWorkLogic(" +++++ pipeTackerToPacker isEmpty sleep ");
                        Thread.currentThread().sleep(5);
                    } catch(InterruptedException ex){
                        outStatesOfWorkLogic(" sleep is interrupted with message" + ex.getMessage());
                    }
                }
            } else {
                outStatesOfWorkLogic(" +++++ pipeTackerToPacker is null");
            }
        } while( !this.innerRuleForDirListWorkers.isDirListTackerLogicFinished() );
        do{
            this.innerRuleForDirListWorkers.setDirListPackerLogicFinished();
        } while( !this.innerRuleForDirListWorkers.isDirListPackerLogicFinished() );
        outStatesOfWorkLogic(" Packer end run part");
    }
    private void outStatesOfWorkLogic(String strForOutPut){
        String strRunLogicLabel = ThLogicDirListPacker.class.getCanonicalName() 
                            + "[THREADNAME]" + Thread.currentThread().getName()
                            + strForOutPut;
        NcAppHelper.outToConsoleIfDevAndParamTrue(strRunLogicLabel, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_PACKER_RUN);
    }
    private void outDataProcessedOfWorkLogic(Long reciveIn, Long sendOut, Long packetsOut ){
        String strRunLogicLabel = ThLogicDirListPacker.class.getCanonicalName() 
                            + "[THREADNAME]" + Thread.currentThread().getName()
                            + "                          in    " 
                            + String.valueOf(reciveIn) 
                            + "  out   "
                            + String.valueOf(sendOut)
                            + "  pack   "
                            + String.valueOf(packetsOut);
        NcAppHelper.outToConsoleIfDevAndParamTrue(strRunLogicLabel, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_PACKER_DATA_COUNT);
    }
}