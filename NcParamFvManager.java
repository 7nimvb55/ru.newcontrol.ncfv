/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package ru.newcontrol.ncfv;

import java.io.File;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcParamFvManager {
    
    /**
     * 
     * @param inWorkCfg
     * @return 
     */
    public static boolean isNcParamFvDataEmpty(NcParamFv inWorkCfg){
        return (inWorkCfg.indexPath.length() == 0
        && inWorkCfg.keywordsOutOfSearch.length() == 0
        && inWorkCfg.keywordsInSearch.length() == 0
        && inWorkCfg.dirOutOfIndex.length() == 0
        && inWorkCfg.dirInIndex.length() == 0
        && inWorkCfg.diskUserAlias.isEmpty()
        && inWorkCfg.strHexMD5.length() == 0
        && inWorkCfg.strHexSHA1.length() == 0
        && inWorkCfg.strHexSHA256.length() == 0
        && inWorkCfg.strHexSHA512.length() == 0
        && inWorkCfg.tmIndexSubDirs.isEmpty());
    }
    /**
     * 
     * @param inWorkCfg
     * @return 
     */
    public static boolean isNcParamFvDataHashTrue(NcParamFv inWorkCfg){
        int calcHash = (""
            + inWorkCfg.indexPath
            + inWorkCfg.keywordsOutOfSearch
            + inWorkCfg.keywordsInSearch
            + inWorkCfg.dirOutOfIndex
            + inWorkCfg.diskUserAlias.hashCode()
            + inWorkCfg.strHexMD5
            + inWorkCfg.strHexSHA1
            + inWorkCfg.strHexSHA256
            + inWorkCfg.strHexSHA512
            + inWorkCfg.tmIndexSubDirs.hashCode()
            + inWorkCfg.recordTime).hashCode();
        boolean boolCompareResult = inWorkCfg.recordHash == calcHash;
        
        if( !boolCompareResult ){
            String strOut = NcStrLogMsgField.INFO.getStr()
                + NcStrServiceMsg.HASH_CALC.getStr()
                + calcHash
                + NcStrServiceMsg.TAB.getStr()
                + NcStrServiceMsg.HASH_RECORD.getStr()
                + inWorkCfg.recordHash
                + NcStrServiceMsg.TAB.getStr()
                + NcStrServiceMsg.RESULT.getStr() + boolCompareResult;
            NcAppHelper.outMessage(strOut);
        }
        
        
        return boolCompareResult;
    }
    public static NcParamFv setFileHashes(NcParamFv inWorkCfg, File fileCfg){
        String strHexMD5 = NcAppHelper.toHex(NcFileHash.MD5.checksum(fileCfg));
        String strHexSHA1 = NcAppHelper.toHex(NcFileHash.SHA1.checksum(fileCfg));
        String strHexSHA256 = NcAppHelper.toHex(NcFileHash.SHA256.checksum(fileCfg));
        String strHexSHA512 = NcAppHelper.toHex(NcFileHash.SHA512.checksum(fileCfg));
        NcParamFv forOutParam = new NcParamFv(
            inWorkCfg.indexPath,
            inWorkCfg.keywordsOutOfSearch,
            inWorkCfg.keywordsInSearch,
            inWorkCfg.dirOutOfIndex,
            inWorkCfg.dirInIndex,
            inWorkCfg.diskUserAlias,
            strHexMD5,
            strHexSHA1,
            strHexSHA256,
            strHexSHA512,
            inWorkCfg.tmIndexSubDirs
        );
        
        return forOutParam;
    }
    public static void ncParamFvDataOutPut(NcParamFv inWorkCfg){
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrServiceMsg.HASH_RECORD_IS.getStr()
            + isNcParamFvDataHashTrue(inWorkCfg));
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.INDEX_PATH.getStr()
            + inWorkCfg.indexPath);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.KEYWORD_OUT_SEARCH.getStr()
            + inWorkCfg.keywordsOutOfSearch);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.KEYWORD_IN_SEARCH.getStr()
            + inWorkCfg.keywordsInSearch);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.DIR_OUT_INDEX.getStr()
            + inWorkCfg.dirOutOfIndex);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.DIR_IN_INDEX.getStr()
            + inWorkCfg.dirInIndex);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.DISK_USER_ALIAS_SIZE.getStr()
            + inWorkCfg.diskUserAlias.size());
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.STR_HEX_MD5.getStr()
            + inWorkCfg.strHexMD5);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.STR_HEX_SHA1.getStr()
            + inWorkCfg.strHexSHA1);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.STR_HEX_SHA256.getStr()
            + inWorkCfg.strHexSHA256);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.STR_HEX_SHA512.getStr()
            + inWorkCfg.strHexSHA512);
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.TM_INDEX_SUBDIRS.getStr()
            + inWorkCfg.tmIndexSubDirs.size());
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.NCPARAMFV.getStr()
            + NcStrVarDescription.RECORD_TIME.getStr()
            + inWorkCfg.recordTime);
    }
    public static void checkToWrite(NcParamFv inWorkCfg){
        boolean isHash = isNcParamFvDataHashTrue(inWorkCfg);
        String strLvl = NcStrLogMsgField.INFO.getStr();
        if( !isHash ){
            strLvl = NcStrLogMsgField.ERROR.getStr();
        }
        NcAppHelper.outMessage(strLvl
        + NcStrLogMsgField.CHECK_RESULT.getStr()
        + NcStrServiceMsg.WORK_CFG_HASH.getStr()
        + NcStrServiceMsg.FOR_WRITE.getStr()
        + isHash);
        
    }
    public static void checkFromRead(NcParamFv inWorkCfg){
        boolean isHash = isNcParamFvDataHashTrue(inWorkCfg);
        String strLvl = NcStrLogMsgField.INFO.getStr();
        if( !isHash ){
            strLvl = NcStrLogMsgField.ERROR.getStr();
        }
        NcAppHelper.outMessage(strLvl
        + NcStrLogMsgField.CHECK_RESULT.getStr()
        + NcStrServiceMsg.WORK_CFG_HASH.getStr()
        + NcStrServiceMsg.FROM_READ.getStr()
        + isHash);
    }
}
