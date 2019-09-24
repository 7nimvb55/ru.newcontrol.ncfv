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
import java.util.concurrent.LinkedTransferQueue;

/**
 * Adib
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>Bus Process Command
 * </ul>
 * Contains Bus with commands for runners (runnable inside workers), 
 * Type bus:  wait, do, ready
 * process number: 0..13 from {@link AdihHelper#getProcessNames() AdihHelper.getProcessNames()}
 * command code: 0..13 from {@link AdihHelper#getCommandNames() AdihHelper.getCommandNames()}
 * {@code typedProcessCode = this.timeCreation + this.objectLabel + sufFix = ([busTypeCode]*[processCode])}
 * {@code <Integer  typedProcessCode, <Integer commandCode>>}
 * @author wladimirowichbiaran
 */
public class AdibProcessCommand {
    private final Long timeCreation;
    private final UUID objectLabel;
    private ConcurrentSkipListMap<Integer, LinkedTransferQueue<Integer>> busProcessCommand;
    
    public AdibProcessCommand(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.busProcessCommand = new ConcurrentSkipListMap<Integer, LinkedTransferQueue<Integer>>();
        initEmptyLists();
    }
    /**
     * 
     */
    private void initEmptyLists(){
        Integer typedProcessCodeByNumber;
        try {
            for( Integer idxBusType = 0; idxBusType < getBusTypeNamesCount(); idxBusType++ ){
                for(Integer idxProcess = 0; idxProcess < getProcessNamesCount(); idxProcess++ ){
                    typedProcessCodeByNumber = getTypedProcessCodeByNumber(idxBusType, idxProcess);
                    this.busProcessCommand.put(typedProcessCodeByNumber, new LinkedTransferQueue<Integer>());
                }
            }
        } finally {
            typedProcessCodeByNumber = null;
        }
    }
    /**
     * 
     * @param inputedBusType
     * <ul>
     * <li>   0 -   waitBus
     * <li>   1 -   doBus
     * <li>   2 -   readyBus
     * </ul>
     * @param inputedProcess
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
     * @param commandForPut
     * <ul>
     * <li>   0 -   Start - - - not released
     * <li>   1 -   Stop - - - not released
     * <li>   2 -   PauseFromUser - - - not released
     * <li>   3 -   Cancel - - - not released
     * <li>   4 -   Status - - - not released
     * <li>   5 -   Statistic - - - not released
     * <li>   6 -   ReinitVariables - - - not released
     *              
     * <li>   7 -   ReinitDataBus - - - not released
     * <li>   8 -   ReinitDataBusInput - - - not released
     * <li>   9 -   ReinitDataBusOutput - - - not released
     * 
     * <li>  10 -   ReinitSelf - - - not released
     * <li>  11 -   ClaculatedSlowStart - - - not released
     * <li>  12 -   ClaculatedSlowEnd - - - not released
     * <li>  13 -   UserSlowStart - - - not released
     * <li>  14 -   UserSlowEnd - - - not released
     * </ul>
     * @return 
     */
    protected Boolean commandPut(final Integer inputedBusType, 
            final Integer inputedProcess,
            final Integer commandForPut){
        Integer typedProcessCodeByNumber;
        LinkedTransferQueue<Integer> typedProcessCommandList;
        try {
            if( commandForPut == null ){
                return Boolean.FALSE;
            }
            if( commandForPut == Integer.MIN_VALUE ){
                return Boolean.FALSE;
            }
            if( inputedBusType == null ){
                return Boolean.FALSE;
            }
            if( inputedBusType == Integer.MIN_VALUE ){
                return Boolean.FALSE;
            }
            if( inputedProcess == null ){
                return Boolean.FALSE;
            }
            if( inputedProcess == Integer.MIN_VALUE ){
                return Boolean.FALSE;
            }
            typedProcessCodeByNumber = getTypedProcessCodeByNumber(inputedBusType, inputedProcess);
            typedProcessCommandList = this.busProcessCommand.get(typedProcessCodeByNumber);
            if( typedProcessCommandList == null ){
                return Boolean.FALSE;
            }
            typedProcessCommandList.put(getCommandCodeByNumber(commandForPut));
            return Boolean.TRUE;
        } finally {
            typedProcessCodeByNumber = null;
            typedProcessCommandList = null;
        }
    }
    /**
     * 
     * @param inputedBusType
     * <ul>
     * <li>   0 -   waitBus
     * <li>   1 -   doBus
     * <li>   2 -   readyBus
     * </ul>
     * @param inputedProcess
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
     * @return 
     */
    protected Integer commandPoll(final Integer inputedBusType, final Integer inputedProcess){
        Integer typedProcessCodeByNumber;
        LinkedTransferQueue<Integer> typedProcessCommandList;
        Integer returnedCommand = null;
        try {
            typedProcessCodeByNumber = getTypedProcessCodeByNumber(inputedBusType, inputedProcess);
            typedProcessCommandList = this.busProcessCommand.get(typedProcessCodeByNumber);
            if( typedProcessCommandList == null ){
                return Integer.MIN_VALUE;
            }
            returnedCommand = typedProcessCommandList.poll();
            if( returnedCommand == null ){
                return Integer.MIN_VALUE;
            }
            return new Integer(returnedCommand);
        } finally {
            typedProcessCodeByNumber = null;
            typedProcessCommandList = null;
            returnedCommand = null;
        }
    }
    /**
     * 
     * @param inputedBusType
     * <ul>
     * <li>   0 -   waitBus
     * <li>   1 -   doBus
     * <li>   2 -   readyBus
     * </ul>
     * @param inputedProcess
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
     * @return 
     */
    protected Integer commandSizeQueue(final Integer inputedBusType, final Integer inputedProcess){
        Integer typedProcessCodeByNumber;
        LinkedTransferQueue<Integer> typedProcessCommandList;
        try {
            typedProcessCodeByNumber = getTypedProcessCodeByNumber(inputedBusType, inputedProcess);
            typedProcessCommandList = this.busProcessCommand.get(typedProcessCodeByNumber);
            if( typedProcessCommandList == null ){
                return Integer.MIN_VALUE;
            }
            return new Integer(typedProcessCommandList.size());
        } finally {
            typedProcessCodeByNumber = null;
            typedProcessCommandList = null;
        }
    }
    /**
     * see {@link ru.newcontrol.ncfv.AdihHelper#getBusTypeNames() AdihHelper.getBusTypeNames()}
     * @return 
     */
    private String[] getBusTypeNames(){
        return AdihHelper.getBusTypeNames();
    }
    /**
     * see {@link ru.newcontrol.ncfv.AdihHelper#getProcessNames() AdihHelper.getProcessNames()}
     * @return 
     */
    private String[] getProcessNames(){
        return AdihHelper.getProcessNames();
    }
    /**
     * see {@link ru.newcontrol.ncfv.AdihHelper#getCommandNames() AdihHelper.getCommandNames()}
     * @return 
     */
    private String[] getCommandNames(){
        return AdihHelper.getCommandNames();
    }
    /**
     * @todo comand list not valide
     * check on validate run if workers recived Integer.MIN_VALUE for next command
     * or other not associeted value
     * if comand list not valide, algoritm run process rebuild all objects
     * @return 
     */
    protected Boolean isCommandListValide(ConcurrentSkipListMap<Integer, Integer> inputedListForCheck){
        if( inputedListForCheck == null ){
            return Boolean.FALSE;
        }
        if( inputedListForCheck.isEmpty() ){
            return Boolean.FALSE;
        }
        Integer prevKey = 0;
        Boolean isFirstKey = Boolean.TRUE;
        try {
            for(Map.Entry<Integer, Integer> itemChecked : inputedListForCheck.entrySet()){
                if( isFirstKey ){
                    if( itemChecked.getKey() != 0 ){
                        return Boolean.FALSE;
                    }
                    isFirstKey = Boolean.FALSE;
                }
                if( (itemChecked.getKey() - 1 - prevKey) != 0 ){
                    return Boolean.FALSE;
                }
                prevKey = itemChecked.getKey();
                if( getCommandCodeByNumber(prevKey) != itemChecked.getValue() ){
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } finally {
            prevKey = null;
            isFirstKey = null;
        }
    }
    /**
     * <ul>
     * <li>   0 -   Start - - - not released
     * <li>   1 -   Stop - - - not released
     * <li>   2 -   PauseFromUser - - - not released
     * <li>   3 -   Cancel - - - not released
     * <li>   4 -   Status - - - not released
     * <li>   5 -   Statistic - - - not released
     * <li>   6 -   ReinitVariables - - - not released
     *              
     * <li>   7 -   ReinitDataBus - - - not released
     * <li>   8 -   ReinitDataBusInput - - - not released
     * <li>   9 -   ReinitDataBusOutput - - - not released
     * 
     * <li>  10 -   ReinitSelf - - - not released
     * <li>  11 -   ClaculatedSlowStart - - - not released
     * <li>  12 -   ClaculatedSlowEnd - - - not released
     * <li>  13 -   UserSlowStart - - - not released
     * <li>  14 -   UserSlowEnd - - - not released
     * </ul>
     * @param inputedCommandNum
     * @return 
     */
    private String commandNameByNumber(Integer inputedCommandNum){
        if( inputedCommandNum == null ){
            return new String();
        }
        if( inputedCommandNum < 0 ){
            return new String();
        }
        Integer commandNum = inputedCommandNum;
        String[] oneOfReturnedCommand;
        oneOfReturnedCommand = getCommandNames();
        if( oneOfReturnedCommand == null ){
            return new String();
        }
        try {
            
            if( commandNum < 0 ){
                return new String();
            }
            if( oneOfReturnedCommand.length == 0 ){
                return new String();
            }
            if( commandNum > (oneOfReturnedCommand.length - 1) ){
                return new String();
            }
            return new String(oneOfReturnedCommand[commandNum]);
        }finally {
            commandNum = null;
            AdihUtilization.utilizeStringValues(oneOfReturnedCommand);
        }
    }
    /**
     * <ul>
     * <li>   0 -   Start - - - not released
     * <li>   1 -   Stop - - - not released
     * <li>   2 -   PauseFromUser - - - not released
     * <li>   3 -   Cancel - - - not released
     * <li>   4 -   Status - - - not released
     * <li>   5 -   Statistic - - - not released
     * <li>   6 -   ReinitVariables - - - not released
     *              
     * <li>   7 -   ReinitDataBus - - - not released
     * <li>   8 -   ReinitDataBusInput - - - not released
     * <li>   9 -   ReinitDataBusOutput - - - not released
     * 
     * <li>  10 -   ReinitSelf - - - not released
     * <li>  11 -   ClaculatedSlowStart - - - not released
     * <li>  12 -   ClaculatedSlowEnd - - - not released
     * <li>  13 -   UserSlowStart - - - not released
     * <li>  14 -   UserSlowEnd - - - not released
     * </ul>
     * @param inputedCommandNum
     * @return 
     */
    private Integer getCommandCodeByNumber(Integer inputedCommandNum){
        if( inputedCommandNum == null ){
            return Integer.MIN_VALUE;
        }
        if( inputedCommandNum < 0 ){
            return Integer.MIN_VALUE;
        }
        Integer commandNum = inputedCommandNum;
        String[] oneOfReturnedCommand;
        oneOfReturnedCommand = getCommandNames();
        if( oneOfReturnedCommand == null ){
            return Integer.MIN_VALUE;
        }
        Integer forReturnCodeCommand = null;
        try {
            
            if( commandNum < 0 ){
                return Integer.MIN_VALUE;
            }
            if( oneOfReturnedCommand.length == 0 ){
                return Integer.MIN_VALUE;
            }
            if( commandNum > (oneOfReturnedCommand.length - 1) ){
                return Integer.MIN_VALUE;
            }
            forReturnCodeCommand = oneOfReturnedCommand[commandNum]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            if( forReturnCodeCommand == Integer.MIN_VALUE ){
                    forReturnCodeCommand = oneOfReturnedCommand[commandNum]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).concat(String.valueOf(Integer.MIN_VALUE)).hashCode();
            }
            if( forReturnCodeCommand == Integer.MIN_VALUE ){
                forReturnCodeCommand = oneOfReturnedCommand[commandNum]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).concat(String.valueOf(Integer.MIN_VALUE)).concat(String.valueOf(Integer.MAX_VALUE)).hashCode();
            }
            return forReturnCodeCommand;
        }finally {
            commandNum = null;
            forReturnCodeCommand = null;
            AdihUtilization.utilizeStringValues(oneOfReturnedCommand);
        }
    }
    /**
     * <ul>
     * <li>   0 -   Start - - - not released
     * <li>   1 -   Stop - - - not released
     * <li>   2 -   PauseFromUser - - - not released
     * <li>   3 -   Cancel - - - not released
     * <li>   4 -   Status - - - not released
     * <li>   5 -   Statistic - - - not released
     * <li>   6 -   ReinitVariables - - - not released
     *              
     * <li>   7 -   ReinitDataBus - - - not released
     * <li>   8 -   ReinitDataBusInput - - - not released
     * <li>   9 -   ReinitDataBusOutput - - - not released
     * 
     * <li>  10 -   ReinitSelf - - - not released
     * <li>  11 -   ClaculatedSlowStart - - - not released
     * <li>  12 -   ClaculatedSlowEnd - - - not released
     * <li>  13 -   UserSlowStart - - - not released
     * <li>  14 -   UserSlowEnd - - - not released
     * </ul>
     * @return 
     */
    protected ConcurrentSkipListMap<Integer, Integer> getCommandsList(){
        String[] oneOfReturnedCommand;
        oneOfReturnedCommand = getCommandNames();
        if( oneOfReturnedCommand == null ){
            return null;
        }
        if( oneOfReturnedCommand.length == 0 ){
                return null;
        }
        ConcurrentSkipListMap<Integer, Integer> listCommandsForReturn = new ConcurrentSkipListMap<Integer, Integer>();
        Integer idxForList;
        Integer commandValue;
        try {
            for( idxForList = 0; idxForList < oneOfReturnedCommand.length; idxForList++ ){
                commandValue = oneOfReturnedCommand[idxForList]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
                if( commandValue == Integer.MIN_VALUE ){
                    commandValue = oneOfReturnedCommand[idxForList]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).concat(String.valueOf(Integer.MIN_VALUE)).hashCode();
                }
                if( commandValue == Integer.MIN_VALUE ){
                    commandValue = oneOfReturnedCommand[idxForList]
                        .concat(String.valueOf(this.timeCreation))
                        .concat(this.objectLabel.toString()).concat(String.valueOf(Integer.MIN_VALUE)).concat(String.valueOf(Integer.MAX_VALUE)).hashCode();
                }
                listCommandsForReturn.put(idxForList, commandValue);
            }
            return listCommandsForReturn;
        }finally {
            idxForList = null;
            commandValue = null;
            AdihUtilization.utilizeStringValues(oneOfReturnedCommand);
        }
    }
    /**
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param typeNum
     * @return 
     */
    private String busTypeName(Integer inputedTypeNum){
        if( inputedTypeNum == null ){
            return new String();
        }
        String[] oneOfReturnedType;
        if( inputedTypeNum < 0 ){
            return new String();
        }
        Integer typeNum = inputedTypeNum;
        oneOfReturnedType = getBusTypeNames();
        if( oneOfReturnedType == null ){
            return new String();
        }
        try {
            if( typeNum < 0 ){
                return new String();
            }
            if( oneOfReturnedType.length == 0 ){
                return new String();
            }
            if( typeNum > (oneOfReturnedType.length - 1) ){
                return new String();
            }
            return new String(oneOfReturnedType[typeNum]);
        }finally {
            typeNum = null;
            AdihUtilization.utilizeStringValues(oneOfReturnedType);
        }
    }
    /**
     * {@code getTypedProcessCodeByNumber = this.timeCreation + this.objectLabel + sufFix = ([busTypeCode]*[processCode])}
     * @param typeBusNumber
     * <ul>
     * <li>   0 -   waitBus
     * <li>   1 -   doBus
     * <li>   2 -   readyBus
     * </ul>
     * @param numProcessNameInputed
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
     * @return 
     */
    private Integer getTypedProcessCodeByNumber(final Integer typeBusNumber, final Integer numProcessNameInputed){
        String[] processNamesArray;
        Integer codeForEventReadyName;
        Integer numProcessNameFunc;
        String typeBusNameFunc = busTypeName(typeBusNumber);
        try {
            numProcessNameFunc = (Integer) numProcessNameInputed;
            if( numProcessNameFunc < 0 ){
                return Integer.MIN_VALUE;
            }
            processNamesArray = getProcessNames();
            if( processNamesArray == null ){
                return Integer.MIN_VALUE;
            }
            if( processNamesArray.length == 0 ){
                return Integer.MIN_VALUE;
            } 
            if( numProcessNameFunc > (processNamesArray.length - 1) ){
                return Integer.MIN_VALUE;
            } 
            if( typeBusNameFunc == null ){
                return Integer.MIN_VALUE;
            }
            if( typeBusNameFunc.isEmpty() ){
                return Integer.MIN_VALUE;
            }
            codeForEventReadyName = typeBusNameFunc.concat(processNamesArray[numProcessNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString())).hashCode();
            if( codeForEventReadyName == Integer.MIN_VALUE ){
                codeForEventReadyName = typeBusNameFunc.concat(processNamesArray[numProcessNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).concat(String.valueOf(Integer.MIN_VALUE))).hashCode();
            }
            if( codeForEventReadyName == Integer.MIN_VALUE ){
                codeForEventReadyName = typeBusNameFunc.concat(processNamesArray[numProcessNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).concat(String.valueOf(Integer.MIN_VALUE)).concat(String.valueOf(Integer.MAX_VALUE))).hashCode();
            }
            return new Integer(codeForEventReadyName);
        } finally {
            processNamesArray = null;
            codeForEventReadyName = null;
            numProcessNameFunc = null;
            AdihUtilization.utilizeStringValues(new String[]{typeBusNameFunc});
        }
    }
    /**
     * 
     * @return 
     */
    private Integer getProcessNamesCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getProcessNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * 
     * @return 
     */
    private Integer getBusTypeNamesCount(){
        String[] busTypeNamesArray;
        try {
            busTypeNamesArray = getBusTypeNames();
            return new Integer(busTypeNamesArray.length);
        } finally {
            busTypeNamesArray = null;
        }
    }
}
