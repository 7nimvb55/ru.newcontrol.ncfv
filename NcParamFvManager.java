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
            String strOut = "Calculated hash: "
                + calcHash
                + "\tin record hash: "
                + inWorkCfg.recordHash
                + "\tresult: " + boolCompareResult;
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
        NcAppHelper.outMessage("RecordHash is: " + isNcParamFvDataHashTrue(inWorkCfg));
        NcAppHelper.outMessage("indexPath: " + inWorkCfg.indexPath);
        NcAppHelper.outMessage("keywordsOutOfSearch: " + inWorkCfg.keywordsOutOfSearch);
        NcAppHelper.outMessage("keywordsInSearch: " + inWorkCfg.keywordsInSearch);
        NcAppHelper.outMessage("dirOutOfIndex: " + inWorkCfg.dirOutOfIndex);
        NcAppHelper.outMessage("diskUserAlias.size: " + inWorkCfg.diskUserAlias.size());
        NcAppHelper.outMessage("For cfg file strHexMD5: " + inWorkCfg.strHexMD5);
        NcAppHelper.outMessage("For cfg file strHexSHA1: " + inWorkCfg.strHexSHA1);
        NcAppHelper.outMessage("For cfg file strHexSHA256: " + inWorkCfg.strHexSHA256);
        NcAppHelper.outMessage("For cfg file strHexSHA512: " + inWorkCfg.strHexSHA512);
        NcAppHelper.outMessage("tmIndexSubDirs.size: " + inWorkCfg.tmIndexSubDirs.size());
        NcAppHelper.outMessage("recordTime: " + inWorkCfg.recordTime);
    }
    public static void checkToWrite(NcParamFv inWorkCfg){
        boolean isHash = isNcParamFvDataHashTrue(inWorkCfg);
        /*if( isHash ){
            NcAppHelper.outMessage("Work config hash for write is ok");
        }
        else{
            NcAppHelper.outMessage("Work config hash for write has error");
        }*/
    }
    public static void checkFromRead(NcParamFv inWorkCfg){
        boolean isHash = isNcParamFvDataHashTrue(inWorkCfg);
        /*if( isHash ){
            NcAppHelper.outMessage("Work config hash from read is ok");
        }
        else{
            NcAppHelper.outMessage("Work config hash from read has error");
        }*/
    }
}
