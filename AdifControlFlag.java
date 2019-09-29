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
 *
 * @author wladimirowichbiaran
 */
public class AdifControlFlag {
    private final Long timeCreation;
    private final UUID objectLabel;
    private final Integer numberProcessIndexSystem;
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Boolean>> poolRunnerFlag;
    private final AdimRule ruleAdim;
    private final AdilState adilState;
    
    public AdifControlFlag(final Integer processIndexSystemNumber,
            final AdimRule outerRule){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        if( outerRule == null ){
            throw new UnsupportedOperationException(AdimRule.class.getCanonicalName() 
                    + " object for set in "
                    + AdihTemplateRunnable.class.getCanonicalName()
                    + " is null");
        }
        this.ruleAdim = (AdimRule) outerRule;
        if( processIndexSystemNumber == null ){
            throw new UnsupportedOperationException("processIndexSystemNumber for set in "
                    + AdihTemplateRunnable.class.getCanonicalName()
                    + " is null");
        }
        if( processIndexSystemNumber < 0 ){
            throw new UnsupportedOperationException("processIndexSystemNumber for set in "
                    + AdihTemplateRunnable.class.getCanonicalName()
                    + " is not natural ( processIndexSystemNumber < 0 (Zero) )");
        }
        this.numberProcessIndexSystem = processIndexSystemNumber;
        this.adilState = (AdilState) this.ruleAdim.getAdilRule().getAdilState();
        this.poolRunnerFlag = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Boolean>>();
    }
    /**
     * 
     * @param keyFlowForRunner
     * @return 
     */
    private ConcurrentSkipListMap<Integer, Boolean> getRunnerFlagForKeyPointFlow(final UUID keyFlowForRunner){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Boolean> getRunnerFlagFormPool;
        try{
            inputedVal = (UUID) keyFlowForRunner;
            getRunnerFlagFormPool = getFromListRunnerFlag(inputedVal);
            if( getRunnerFlagFormPool == null ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getRunnerFlagForKeyPointFlow()".concat(
                        " key in list FlowControl not exist for Runner UUID ".concat(inputedVal.toString()))));
            }
            return getRunnerFlagFormPool;
        } finally {
            inputedVal = null;
            getRunnerFlagFormPool = null;
        }
    }
    /**
     * 
     * @param keyValue
     * @return 
     */
    private ConcurrentSkipListMap<Integer, Boolean> getFromListRunnerFlag(UUID keyValue){
        try {
            return this.poolRunnerFlag.get(keyValue);
        } catch (ClassCastException exClCast) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(
                    this.numberProcessIndexSystem, 
                    ClassCastException.class.getCanonicalName(),
                    AdifControlFlag.class.getCanonicalName(), 
                    "getFromListRunnerFlag()", 
                    exClCast.getMessage());
            this.adilState.logStackTrace(this.numberProcessIndexSystem);
            exClCast = null;
        } catch (NullPointerException exPointer) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(
                    this.numberProcessIndexSystem, 
                    NullPointerException.class.getCanonicalName(),
                    AdifControlFlag.class.getCanonicalName(), 
                    "getFromListRunnerFlag()", 
                    exPointer.getMessage());
            this.adilState.logStackTrace(this.numberProcessIndexSystem);
            exPointer = null;
        }
        return null;
    }
    /**
     * 
     * @param listFlags
     * @param numberInList
     * @return 
     */
    private Boolean getFromListValues(
            ConcurrentSkipListMap<Integer, Boolean> listFlags,
            Integer numberInList){
        try {
            return listFlags.get(numberInList);
        } catch (ClassCastException exClCast) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(
                    this.numberProcessIndexSystem, 
                    ClassCastException.class.getCanonicalName(), 
                    AdifControlFlag.class.getCanonicalName(), 
                    "getFromListValues()", 
                    exClCast.getMessage());
            this.adilState.logStackTrace(this.numberProcessIndexSystem);
            exClCast = null;
        } catch (NullPointerException exPointer) {
            this.adilState.putLogLineByProcessNumberMsgExceptions(
                    this.numberProcessIndexSystem, 
                    NullPointerException.class.getCanonicalName(), 
                    AdifControlFlag.class.getCanonicalName(), 
                    "getFromListValues()", 
                    exPointer.getMessage());
            this.adilState.logStackTrace(this.numberProcessIndexSystem);
            exPointer = null;
        }
        return null;
    }
    /**
     * 
     * @param removedKeyFlowForRunner
     * @return true if found and delete data
     */
    protected Boolean removeRunnerFlagForKeyPointFlow(final UUID removedKeyFlowForRunner){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Boolean> getRemovedRunnerFlagFormPool;
        Boolean removedVal;
        Integer removedKey;
        try{
            inputedVal = (UUID) removedKeyFlowForRunner;
            getRemovedRunnerFlagFormPool = (ConcurrentSkipListMap<Integer, Boolean>) this.poolRunnerFlag.remove(inputedVal);
            if( getRemovedRunnerFlagFormPool == null ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "removeRunnerFlagForKeyPointFlow()".concat(
                        " key in list FlowControl not exist for Runner UUID ".concat(inputedVal.toString()))));
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Boolean> itemOfPoint : getRemovedRunnerFlagFormPool.entrySet() ){
                removedKey = itemOfPoint.getKey();
                removedVal = getRemovedRunnerFlagFormPool.remove(removedKey);
                removedVal = null;
                removedKey = null;
            }
            getRemovedRunnerFlagFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedRunnerFlagFormPool = null;
        }
    }
    /**
     * 
     * @param checkedKeyFlowForRunner
     * @return 
     */
    protected Boolean isRunnerFlagNotExist(final UUID checkedKeyFlowForRunner){
        UUID inputedVal;
        try{
            inputedVal = (UUID) checkedKeyFlowForRunner;
            if( !this.poolRunnerFlag.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * <ul>
     * <li>0 - isDoCommandStop
     * <li>1 - isSetPauseFromUser
     * </ul>
     * @param keyRunnerInputed
     * @param paramNumber
     * @return 
     */
    protected Boolean getRunnerFlagByNumber(
            final UUID keyRunnerInputed, 
            final Integer paramNumber){
        ConcurrentSkipListMap<Integer, Boolean> getListValues;
        Integer returnedParamValue;
        UUID keyForRunnerUuid;
        Integer flagCodeByNumber;
        Boolean getParamForReturn;
        try{
            keyForRunnerUuid = (UUID) keyRunnerInputed;
            returnedParamValue = (Integer) paramNumber;
            if( isRunnerFlagNotExist(keyForRunnerUuid) ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getRunnerFlagByNumber()".concat(
                        " key in list FlowControl not exist for Runner UUID ".concat(keyForRunnerUuid.toString()))));
                return null;
            }
            //all get into new procedure with exceptions
            getListValues = getFromListRunnerFlag(keyForRunnerUuid);
            if( getListValues == null){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getRunnerFlagByNumber()".concat(
                        " key in list FlowControl not exist for Runner UUID ".concat(keyForRunnerUuid.toString()))));
                return null;
            }
            flagCodeByNumber = getFlagCodeByNumber(returnedParamValue);
            getParamForReturn = getFromListValues(getListValues, flagCodeByNumber);
            return new Boolean(getParamForReturn.booleanValue());
        } finally {
            getListValues = null;
            returnedParamValue = null;
            keyForRunnerUuid = null;
            flagCodeByNumber = null;
            getParamForReturn = null;
        }
    }
    /**
     * create new structure for UUID, and set all values to Boolean.FALSE
     * @param keyRunnerUuidInputed
     * @see buildDefaultFlagList()
     */
    protected void createForRunnerUuidFlagList(
                        final UUID keyRunnerUuidInputed){
        ConcurrentSkipListMap<Integer, Boolean> defaultFlagList;
        UUID keyRunnerFunc;
        try{
            keyRunnerFunc = (UUID) keyRunnerUuidInputed;
            if( keyRunnerFunc != null ){
                if( isRunnerFlagNotExist(keyRunnerFunc) ){
                    defaultFlagList = buildDefaultFlagList();
                    this.poolRunnerFlag.put(keyRunnerFunc, defaultFlagList);
                }
            }
        } finally {
            defaultFlagList = null;
            keyRunnerFunc = null;
        }
    }
    /**
     * Create list of flags and set all values to Boolean.FALSE
     * @return {@code ConcurrentSkipListMap<Integer flagCode, Boolean Value>}
     * @see getFlagCodeByNumber(int) 
     */
    private ConcurrentSkipListMap<Integer, Boolean> buildDefaultFlagList(){
        ConcurrentSkipListMap<Integer, Boolean> returnedHashMap;
        Integer flagCodeByNumber;
        Integer countParamsWorkersForSet;
        Integer idx;
        try {
            returnedHashMap = new ConcurrentSkipListMap<Integer, Boolean>();
            countParamsWorkersForSet = getFlagCount();
            for(idx = 0; idx < countParamsWorkersForSet; idx++ ){
                flagCodeByNumber = getFlagCodeByNumber(idx);
                returnedHashMap.put(flagCodeByNumber, Boolean.FALSE);
            }

            return returnedHashMap;
        } finally {
            idx = null;
            flagCodeByNumber = null;
            countParamsWorkersForSet = null;
            returnedHashMap = null;
        }
    }
    /**
     * <ul>
     * <li>0 - isNotDoCommandStop
     * <li>1 - isSetPauseFromUser
     * </ul>
     * @param changedRunnerUuidKey
     * @param flagNumber
     * @param changedVal 
     */
    protected void changeFlagValueByNumber(final UUID changedRunnerUuidKey, final Integer flagNumber, final Boolean changedVal){
        UUID changedKeyPointFlowWorkersFunc;
        Integer flagNumberFunc;
        Boolean changedValFunc;
        Integer flagCodeByNumber;
        ConcurrentSkipListMap<Integer, Boolean> fromCurrentFlow;
        Boolean isFlagListValid;
        try{
            changedKeyPointFlowWorkersFunc = (UUID) changedRunnerUuidKey;
            isFlagListValid = isValidFlagList(changedKeyPointFlowWorkersFunc);
            if( isFlagListValid ){
                flagNumberFunc = (Integer) flagNumber;
                changedValFunc = (Boolean) changedVal;
                fromCurrentFlow = (ConcurrentSkipListMap<Integer, Boolean>) getFromListRunnerFlag(changedKeyPointFlowWorkersFunc);
                flagCodeByNumber = (Integer) getFlagCodeByNumber(flagNumberFunc);
                fromCurrentFlow.put(flagCodeByNumber, changedValFunc);
                try {
                    this.poolRunnerFlag.put(changedKeyPointFlowWorkersFunc, fromCurrentFlow);
                } catch (ClassCastException exClCast) {
                    this.adilState.putLogLineByProcessNumberMsgExceptions(
                            this.numberProcessIndexSystem, 
                            ClassCastException.class.getCanonicalName(), 
                            AdifControlFlag.class.getCanonicalName(), 
                            "changeFlagValueByNumber()", 
                            exClCast.getMessage());
                    this.adilState.logStackTrace(this.numberProcessIndexSystem);
                    exClCast = null;
                } catch (NullPointerException exPointer) {
                    this.adilState.putLogLineByProcessNumberMsgExceptions(
                            this.numberProcessIndexSystem, 
                            NullPointerException.class.getCanonicalName(), 
                            AdifControlFlag.class.getCanonicalName(), 
                            "changeFlagValueByNumber()", 
                            exPointer.getMessage());
                    this.adilState.logStackTrace(this.numberProcessIndexSystem);
                    exPointer = null;
                }
            }
        } finally {
            changedKeyPointFlowWorkersFunc = null;
            flagNumberFunc = null;
            changedValFunc = null;
            flagCodeByNumber = null;
            fromCurrentFlow = null;
            isFlagListValid = null;
        }
    }
    /**
     * default values for this list Boolean.FALSE
     * <ul>
     * <li>0 - isDoCommnadStop
     * <li>1 - isSetPauseFromUser
     * </ul>
     * see {@link ru.newcontrol.ncfv.AdifControlHelper#getRunnerFlagNames() AdifControlHelper.getRunnerFlagNames()}
     * @return 
     */
    private String[] getFlagNames(){
        return AdifControlHelper.getRunnerFlagNames();
    }
    /**
     * Return code of parameter by his number, calculeted from some fileds
     * default values for this list Boolean.FALSE
     * <ul>
     * <li>0 - isDoCommnadStop
     * <li>1 - isSetPauseFromUser
     * </ul>
     * @param numParam
     * @return hashCode for Parameter by his number
     * @see getParamNames()
     */
    private Integer getFlagCodeByNumber(int numParam){
        String[] flagNames;
        try {
            flagNames = getFlagNames();
            if( numParam < 0 ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getFlagCodeByNumber()".concat(
                        " negative index sended, 0 (zero) > "
                        .concat(String.valueOf(numParam))
                        .concat(", count flageters: ")
                        .concat(String.valueOf(flagNames.length)))));
            }
            if( numParam > (flagNames.length - 1) ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getFlagCodeByNumber()".concat(
                        " out of bounds index "
                        .concat(String.valueOf(numParam))
                        .concat(", count flageters: ")
                        .concat(String.valueOf(flagNames.length)
                        .concat(" from 0 to ").concat(String.valueOf(flagNames.length - 1))))));
            } 
            int codeForParameter = flagNames[numParam]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return codeForParameter;
        } finally {
            flagNames = null;
        }
    }
    /**
     * Count records (array.length) returned from {@link #getFlagNames }
     * @return 
     */
    private Integer getFlagCount(){
        String[] flagNames;
        try {
            flagNames = getFlagNames();
            return new Integer(flagNames.length);
        } finally {
            flagNames = null;
        }
    }
    /**
     * default values for this list Boolean.FALSE
     * <ul>
     * <li>0 - isDoCommnadStop
     * <li>1 - isSetPauseFromUser
     * </ul>
     * @param numParam
     * @return name of param by his number
     */
    private String getFlagNameByNumber(int numParam){
        String[] flagNames;
        String flagName;
        try {
            flagNames = getFlagNames();
            if( numParam < 0 ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getFlagNameByNumber()".concat(
                        " negative index sended, 0 (zero) > "
                        .concat(String.valueOf(numParam))
                        .concat(", count parameters: ")
                        .concat(String.valueOf(flagNames.length)))));
            }
            if( numParam > (flagNames.length - 1) ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "getFlagNameByNumber()".concat(
                        " out of bounds index "
                        .concat(String.valueOf(numParam))
                        .concat(", count parameters: ")
                        .concat(String.valueOf(flagNames.length)
                        .concat(" from 0 to ").concat(String.valueOf(flagNames.length - 1))))));
            } 
            flagName = new String(flagNames[numParam]);
            return flagName;
        } finally {
            flagNames = null;
            flagName = null;
        }
    }
    /**
     *
     * @param keyRunnerFlowFlag
     */
    
    protected Boolean isValidFlagList(final UUID keyRunnerFlowFlag){
        UUID keyPointFlowWorkersFunc;
        ConcurrentSkipListMap<Integer, Boolean> flagsForRunner;
        Integer sizeRec;
        Integer flagCount;
        Integer idxFlag;
        Integer flagCodeByNumber;
        String flagNameByNumber;
        try {
            keyPointFlowWorkersFunc = (UUID) keyRunnerFlowFlag;
            if( keyPointFlowWorkersFunc == null ){
                this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "isValidFlagList()".concat(
                        " runner Uuid for check is null ")));
                return Boolean.FALSE;
            }
            if( !isRunnerFlagNotExist(keyPointFlowWorkersFunc) ){
                flagsForRunner = (ConcurrentSkipListMap<Integer, Boolean>) getRunnerFlagForKeyPointFlow(keyPointFlowWorkersFunc);
                sizeRec = (Integer) flagsForRunner.size();
                flagCount = (Integer) getFlagCount();
                if( !sizeRec.equals(flagCount) ){
                    this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "isValidFlagList()".concat(
                        " runner Uuid for check is "
                        .concat(keyPointFlowWorkersFunc.toString())
                        .concat(", default count parameters: ")
                        .concat(String.valueOf(flagCount)
                        .concat(", flow return count records: ")
                        .concat(String.valueOf(sizeRec))))));
                    return Boolean.FALSE;
                }
                for(idxFlag = 0; idxFlag < flagCount; idxFlag++ ){
                    flagCodeByNumber = getFlagCodeByNumber(idxFlag);
                    if( !flagsForRunner.containsKey(flagCodeByNumber) ){
                        flagNameByNumber = getFlagNameByNumber(idxFlag);
                        this.adilState.putLogLineByProcessNumberMsgWarning(
                        this.numberProcessIndexSystem, 
                        AdifControlFlag.class.getCanonicalName().concat( 
                        "isValidFlagList()".concat(
                        " runner Uuid for check is "
                        .concat(keyPointFlowWorkersFunc.toString())
                        .concat(", flag number: ")
                        .concat(String.valueOf(idxFlag)
                        .concat(", with code: ")
                        .concat(String.valueOf(flagCodeByNumber)
                        .concat(" and name: ")
                        .concat(flagNameByNumber)
                        .concat(" not exist in list"))))));
                        return Boolean.FALSE;
                    }
                }
            }
            return Boolean.TRUE;
        } finally {
            sizeRec = null;
            flagCount = null;
            idxFlag = null;
            flagsForRunner = null;
            keyPointFlowWorkersFunc = null;
            flagCodeByNumber = null;
            flagNameByNumber = null;
        }
    }
}
