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
public class NcLogLogicApp {
    /**
     * Used in 
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcParamFvReader#readDataFromWorkCfg() }
     * </ul>
     */
    protected static void NcAppLoaderLoadApp(){
        if( NcfvRunVariables.isLALRNcParamFvReaderReadDataFromWorkCfg() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogMsgText.APP_LOADER_START.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
    protected static void NcIdxFileManagerFileExistRWAccessChecker(String strFile){
        if( NcfvRunVariables.isLALRNcIdxFileManagerFileExistRWAccessChecker() ){
            String strLogMsg = NcStrLogMsgField.ERROR.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogMsgText.NOT_EXIST_OR_READ_WRITE_PERMISSIONS_FOR_FILE.getStr()
                + strFile;
            NcAppHelper.outMessage(strLogMsg);
        }
    }
}
