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

/**
 *
 * @author wladimirowichbiaran
 */
public class NcLogLogicCfg {
    /**
     * Used in 
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcParamFvReader#readDataFromWorkCfg() }
     * </ul>
     */
    protected static void NcParamFvReaderReadDataRead(){
        if( NcfvRunVariables.isLALRNcParamFvReaderReadDataFromWorkCfg() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_CFG_WORK_READ_FROM_FILE.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
    /**
     * Used in 
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcParamFvReader#readDataFromWorkCfg() }
     * </ul>
     */
    protected static void NcParamFvReaderReadDataGenerate(){
        if( NcfvRunVariables.isLALRNcParamFvReaderReadDataFromWorkCfg() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_CFG_WORK_GENERATE_ZERO.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
    /**
     * Used in 
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getCurrentWorkCfg() }
     * </ul>
     */
    protected static void NcPreRunFileViewerGetCurrentWorkCfg(){
        if( NcfvRunVariables.isLALRNcPreRunFileViewerGetCurrentWorkCfg() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_CFG_WORK_GET_CURRENT.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
    /**
     * Used in 
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getCurrentWorkCfg() }
     * </ul>
     */
    protected static void NcPreRunFileViewerValidateAndApplyCfg(){
        if( NcfvRunVariables.isLALRNcPreRunFileViewerValidateAndApplyCfg() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_CFG_WORK_VALIDATE_APPLY.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }    
}
