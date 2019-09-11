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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Adil
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>lines of string log to file
 * </ul>
 * @author wladimirowichbiaran
 */
public class AdilState {
    private final Long timeCreation;
    private final UUID objectLabel;
    private final ConcurrentSkipListMap<Integer, LinkedTransferQueue<String>> logLinesTypedBus;
    
    AdilState(final AdilRule outerRule){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.logLinesTypedBus = new ConcurrentSkipListMap<Integer, LinkedTransferQueue<String>>();
        createLogLinesBus();
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
            paramNames = AdilHelper.getParamNames();
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
            paramNames = AdilHelper.getParamNames();
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
            paramNames = AdilHelper.getParamNames();
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
    /**
     * generate list of typed bus for temporary cache lines for log
     */
    private void createLogLinesBus(){
        LinkedTransferQueue<String> returnedTrensferQueue;
        Integer paramCodeByNumber;
        Integer countParamsDataFsForSet;
        Integer idx;
        try {
            countParamsDataFsForSet = getParamCount();
            for(idx = 0; idx < countParamsDataFsForSet; idx++ ){
                returnedTrensferQueue = new LinkedTransferQueue<String>();
                paramCodeByNumber = getParamCodeByNumber(idx);
                this.logLinesTypedBus.put(paramCodeByNumber, returnedTrensferQueue);
            }
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsDataFsForSet = null;
            returnedTrensferQueue = null;
        }
    }
    /**
     * 
     * @param numberTypedBus
     * @return bus with lines for log by type of number
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    private LinkedTransferQueue<String> getLogLinesBusByNumber(final Integer numberTypedBus){
        Integer paramCodeByNumber;
        LinkedTransferQueue<String> getTypedBus;
        try {
            paramCodeByNumber = getParamCodeByNumber(numberTypedBus);
            getTypedBus = (LinkedTransferQueue<String>) this.logLinesTypedBus.get(paramCodeByNumber);
            return getTypedBus;
        } finally {
            paramCodeByNumber = null;
            getTypedBus = null;
        }
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
     * @param typeBus
     * @param strForLogInputed 
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    protected void putLogLineByProcessNumberMsg(Integer typeBus, String strForLogInputed){
        LinkedTransferQueue<String> logLinesBusByNumber;
        String strForInput = new String();
        try {
            String instanceStartTimeWithMS = AdilHelper.getNowTimeString();
            strForInput = strForLogInputed;
            logLinesBusByNumber = (LinkedTransferQueue<String>) getLogLinesBusByNumber(typeBus);
            logLinesBusByNumber.add(
                    AdilConstants.TIME 
                    + instanceStartTimeWithMS 
                    + AdilConstants.MSG 
                    + strForLogInputed);
        } finally {
            logLinesBusByNumber = null;
            ThWordHelper.utilizeStringValues(new String[]{strForInput});
        }
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
     * @param typeBus
     * @param strForLogInputed 
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    protected void putLogLineByProcessNumberMsgInfo(Integer typeBus, String strForLogInputed){
        LinkedTransferQueue<String> logLinesBusByNumber;
        String strForInput = new String();
        try {
            String instanceStartTimeWithMS = AdilHelper.getNowTimeString();
            strForInput = strForLogInputed;
            logLinesBusByNumber = (LinkedTransferQueue<String>) getLogLinesBusByNumber(typeBus);
            logLinesBusByNumber.add(
                    AdilConstants.TIME 
                    + instanceStartTimeWithMS 
                    + AdilConstants.MSG_INFO
                    + strForLogInputed);
        } finally {
            logLinesBusByNumber = null;
            ThWordHelper.utilizeStringValues(new String[]{strForInput});
        }
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
     * @param typeBus
     * @param strForLogInputed 
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    protected void putLogLineByProcessNumberMsgWarning(Integer typeBus, String strForLogInputed){
        LinkedTransferQueue<String> logLinesBusByNumber;
        String strForInput = new String();
        try {
            String instanceStartTimeWithMS = AdilHelper.getNowTimeString();
            strForInput = strForLogInputed;
            logLinesBusByNumber = (LinkedTransferQueue<String>) getLogLinesBusByNumber(typeBus);
            logLinesBusByNumber.add(
                    AdilConstants.TIME 
                    + instanceStartTimeWithMS 
                    + AdilConstants.MSG_WARNING
                    + strForLogInputed);
        } finally {
            logLinesBusByNumber = null;
            ThWordHelper.utilizeStringValues(new String[]{strForInput});
        }
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
     * @param typeBus
     * @param strForLogInputed 
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    protected void putLogLineByProcessNumberMsgError(Integer typeBus, String strForLogInputed){
        LinkedTransferQueue<String> logLinesBusByNumber;
        String strForInput = new String();
        try {
            String instanceStartTimeWithMS = AdilHelper.getNowTimeString();
            strForInput = strForLogInputed;
            logLinesBusByNumber = (LinkedTransferQueue<String>) getLogLinesBusByNumber(typeBus);
            logLinesBusByNumber.add(
                    AdilConstants.TIME 
                    + instanceStartTimeWithMS 
                    + AdilConstants.MSG_ERROR
                    + strForLogInputed);
        } finally {
            logLinesBusByNumber = null;
            ThWordHelper.utilizeStringValues(new String[]{strForInput});
        }
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
     * @param typeBus
     * @param strForLogInputed 
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    protected void putLogLineByProcessNumberMsgErrorCritical(Integer typeBus, String strForLogInputed){
        LinkedTransferQueue<String> logLinesBusByNumber;
        String strForInput = new String();
        try {
            String instanceStartTimeWithMS = AdilHelper.getNowTimeString();
            strForInput = strForLogInputed;
            logLinesBusByNumber = (LinkedTransferQueue<String>) getLogLinesBusByNumber(typeBus);
            logLinesBusByNumber.add(
                    AdilConstants.TIME 
                    + instanceStartTimeWithMS 
                    + AdilConstants.MSG_ERROR_CRITICAL
                    + strForLogInputed);
        } finally {
            logLinesBusByNumber = null;
            ThWordHelper.utilizeStringValues(new String[]{strForInput});
        }
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
     * @param typeBus
     * @param strForLogInputed 
     * @see ru.newcontrol.ncfv.AdilHelper#getParamNames AdilHelper.getParamNames()
     */
    private LinkedTransferQueue<String> pollLinesByProcessNumber(Integer typeBus){
        LinkedTransferQueue<String> logLinesBusByNumber;
        LinkedTransferQueue<String> strForInput;
        String poll = new String();
        try {
            strForInput = new LinkedTransferQueue<String>();
            logLinesBusByNumber = (LinkedTransferQueue<String>) getLogLinesBusByNumber(typeBus);
            do {
                poll = logLinesBusByNumber.poll();
                if( poll != null ){
                    strForInput.add(poll);
                }
            } while( !logLinesBusByNumber.isEmpty() );
            return strForInput;
        } finally {
            ThWordHelper.utilizeStringValues(new String[]{poll});
            logLinesBusByNumber = null;
        }
    }
    /**
     * 
     * @return 
     */
    protected ConcurrentSkipListMap<String, LinkedTransferQueue<String>> pollBusData(){
        ConcurrentSkipListMap<String, LinkedTransferQueue<String>> forReturnList = new ConcurrentSkipListMap<String, LinkedTransferQueue<String>>();
        LinkedTransferQueue<String> returnedArrayLinesByNumber = null;
        String paramNameByNumber = new String();
        Integer countParamsDataFsForSet;
        Integer idx;
        try {
            countParamsDataFsForSet = getParamCount();
            for(idx = 0; idx < countParamsDataFsForSet; idx++ ){
                returnedArrayLinesByNumber = pollLinesByProcessNumber(idx);
                paramNameByNumber = getParamNameByNumber(idx);
                forReturnList.put(paramNameByNumber, returnedArrayLinesByNumber);
            }
            return forReturnList;
        } finally {
            idx = null;
            countParamsDataFsForSet = null;
            ThWordHelper.utilizeStringValues(new String[]{paramNameByNumber});
            returnedArrayLinesByNumber = null;
            forReturnList = null;
        }
    }
}
