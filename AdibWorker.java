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
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>bus Worker
 * <ul>
 * get typedcode name from static functions
 * - create runners
 * - create and start threads
 * - utilize threads after use
 * 
 * @author wladimirowichbiaran
 */
public class AdibWorker {
    private final Long timeCreation;
    private final UUID objectLabel;
    private final Integer numberProcessIndexSystem;
    
    private final ConcurrentSkipListMap<Integer, Thread> workerListCreated;
    private final ConcurrentSkipListMap<Integer, Thread> workerListRunned;
    private final ConcurrentSkipListMap<Integer, Thread> workerListFinished;
    private final ConcurrentSkipListMap<Integer, AdihTemplateRunnable> runnerTypedList;
    
    private final AdimRule ruleAdim;
    private final AdilState adilState;
    /**
     * 
     * @param ruleMechanics 
     * @throws NullPointerException if <code>ruleMechanics</code> is null
     */
    public AdibWorker(final AdimRule ruleMechanics){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        
        this.numberProcessIndexSystem = 10;
        
        this.workerListCreated = new ConcurrentSkipListMap<Integer, Thread>();
        this.workerListRunned = new ConcurrentSkipListMap<Integer, Thread>();
        this.workerListFinished = new ConcurrentSkipListMap<Integer, Thread>();
        this.runnerTypedList = new ConcurrentSkipListMap<Integer, AdihTemplateRunnable>();
        
        if(  ruleMechanics != null ) {
            this.ruleAdim = (AdimRule) ruleMechanics;
            this.adilState = (AdilState) this.ruleAdim.getAdilRule().getAdilState();
        } else {
            throw new NullPointerException(AdilRule.class.getCanonicalName() 
                    + " object for set in " 
                    + AdibWorker.class.getCanonicalName() 
                    + " is null");
        }
        createWorker();
    }
    /**
     * create new thread object for typed runner and add it into workerListCreated
     */
    private void createWorker(){
        AdihTemplateRunnable createdRunner;
        AdihTemplateThread createdWorker;
        
        Boolean isWorkerCreated = Boolean.FALSE;
        Boolean isWorkerRunned = Boolean.FALSE;
        Boolean isWorkerFinished = Boolean.FALSE;
        
        Integer paramCodeByNumber;
        Integer countParamsDataFsForSet;
        Integer idx;
        
        Thread preRemoveWorker;
        Thread removeWorker;
        try {
            countParamsDataFsForSet = getParamCount();
            for(idx = 0; idx < countParamsDataFsForSet; idx++ ){
                paramCodeByNumber = getParamCodeByNumber(idx);
                if( !runnerInTypedList(paramCodeByNumber) ){
                    createdRunner = new AdihTemplateRunnable(idx, this.ruleAdim);
                    this.runnerTypedList.put(paramCodeByNumber, createdRunner);
                } else {
                    createdRunner = this.runnerTypedList.get(paramCodeByNumber);
                }
                isWorkerCreated = workerInCreated(paramCodeByNumber);
                isWorkerRunned = workerInRunned(paramCodeByNumber);
                isWorkerFinished = workerInFinished(paramCodeByNumber);
                if( !isWorkerCreated ){
                    if( !isWorkerRunned ){
                        if( isWorkerFinished ){
                            preRemoveWorker = this.workerListCreated.get(paramCodeByNumber);
                            if( preRemoveWorker.getState() == Thread.State.TERMINATED ){
                                removeWorker = this.workerListCreated.remove(paramCodeByNumber);
                                AdihUtilization.utilizeFinishedThread(removeWorker);
                            }
                        }
                        createdWorker = new AdihTemplateThread(idx, this.ruleAdim, createdRunner);
                        this.workerListCreated.put(paramCodeByNumber, createdWorker);
                    }
                }
            }
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsDataFsForSet = null;
            preRemoveWorker = null;
            removeWorker = null;
        }
    }
    protected void runAllWorker(){
        Map.Entry<Integer, Thread> pollFirstEntry;
        Integer keyWorker;
        Boolean isWorkerRunned;
        Thread value;
        try {
            do{
                pollFirstEntry = this.workerListCreated.pollFirstEntry();
                keyWorker = pollFirstEntry.getKey();
                isWorkerRunned = workerInRunned(keyWorker);
                if( !isWorkerRunned ){
                    value = pollFirstEntry.getValue();
                    if( Thread.State.NEW == value.getState() ){
                        this.workerListRunned.put(keyWorker, value);
                        value.start();
                    } else {
                        keyWorker = null;
                        AdihUtilization.utilizeFinishedThread(value);
                    }
                }
            }while( !this.workerListCreated.isEmpty() );
        } finally {
            pollFirstEntry = null;
            keyWorker = null;
            isWorkerRunned = null;
            value = null;
        }
    }
    /**
     * 
     * @param inputedKeyForThread
     * @return true if list contains <code>inputedKeyForThread</code>
     */
    private Boolean workerInCreated(Integer inputedKeyForThread){
        try {
            return this.workerListCreated.containsKey(inputedKeyForThread);
        } catch (ClassCastException exClass) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exClass.getMessage());
            
        } catch (NullPointerException exNull) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exNull.getMessage());
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @param inputedKeyForThread
     * @return true if list contains <code>inputedKeyForThread</code>
     */
    private Boolean workerInRunned(Integer inputedKeyForThread){
        try {
            return this.workerListRunned.containsKey(inputedKeyForThread);
        } catch (ClassCastException exClass) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exClass.getMessage());
            
        } catch (NullPointerException exNull) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exNull.getMessage());
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @param inputedKeyForThread
     * @return true if list contains <code>inputedKeyForThread</code>
     */
    private Boolean workerInFinished(Integer inputedKeyForThread){
        try {
            return this.workerListFinished.containsKey(inputedKeyForThread);
        } catch (ClassCastException exClass) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exClass.getMessage());
            
        } catch (NullPointerException exNull) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exNull.getMessage());
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @param inputedKeyForThread
     * @return true if list contains <code>inputedKeyForThread</code>
     */
    private Boolean runnerInTypedList(Integer inputedKeyForThread){
        try {
            return this.runnerTypedList.containsKey(inputedKeyForThread);
        } catch (ClassCastException exClass) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exClass.getMessage());
            
        } catch (NullPointerException exNull) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(this.numberProcessIndexSystem, 
                    AdibWorker.class.getCanonicalName(), 
                    "threadInCreated()", 
                    exNull.getMessage());
        }
        return Boolean.FALSE;
    }
    /**
     * poll thread object from workerListCreated, add it into workerListRunned
     */
    private void startNextWorker(){
        
    }
    /**
     * poll thread object from workerListRunned,  add it into workerListFinished
     */
    private void markWorkerFinished(){
        
    }
    private String[] getNamesArray(){
        return AdihHelper.getProcessNames();
    }
    /**
     * <ul>
     * <li>   0 -   Main
     * <li>   1 -   Index
     * <li>   2 -   DirListManager
     * <li>   3 -   DirListRead
     * <li>   4 -   DirListWrite
     * <li>   5 -   FileListBuild
     *              
     * <li>   6 -   WordStorageFilter
     * <li>   7 -   WordStorageRouter
     * <li>   8 -   WordStorageReader
     * <li>   9 -   WordStorageWriter
     *              
     * <li>  10 -   WordRouter
     * <li>  11 -   WordReader
     * <li>  12 -   WordWriter
     * <li>  13 -   WordEvent
     * </ul> 
     * This list of parameters changed in {@link ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()}
     * Return code of parameter by his number, calculeted from some fileds
     * @param numParam
     * @return hashCode for Parameter by his number
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds or not natural number <code>numParam &lt 0 (Zero)</code>
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    private Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getNamesArray();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + " 0 (zero) > , need for return " + numParam + "count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            int codeForParameter = paramNames[numParam]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return codeForParameter;
        } finally {
            paramNames = null;
        }
    }
    /**
     * Count records (array.length) returned from {@link #getParamNames }
     * @return 
     */
    private Integer getParamCount(){
        String[] paramNames;
        try {
            paramNames = getNamesArray();
            return paramNames.length;
        } finally {
            paramNames = null;
        }
    }
    /**
     * 
     * @param numParam
     * @return name of param by his number
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds or not natural number <code>numParam &lt 0 (Zero)</code>
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    private String getParamNameByNumber(int numParam){
        String[] paramNames;
        String paramName;
        try {
            paramNames = getNamesArray();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + " 0 (zero) > , need for return " + numParam + "count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            paramName = new String(paramNames[numParam]);
            return paramName;
        } finally {
            paramNames = null;
            paramName = null;
        }
    }
}
