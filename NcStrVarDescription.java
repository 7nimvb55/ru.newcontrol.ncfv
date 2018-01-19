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
public enum NcStrVarDescription {
    IDX("[INDEX]"),
    ID("[IDENTIFICATION_NUMBER]"),
    INDEX_PATH("[indexPath]"),
    KEYWORD_OUT_SEARCH("[keywordOutSearch]"),
    KEYWORD_IN_SEARCH("[keywordInSearch]"),
    DIR_OUT_INDEX("[dirOutIndex]"),
    DIR_IN_INDEX("[dirInIndex]"),
    DISK_USER_ALIAS_SIZE("[diskUserAlias.size]"),
    STR_HEX_MD5("[strHexMD5]"),
    STR_HEX_SHA1("[strHexSHA1]"),
    STR_HEX_SHA256("[strHexSHA256]"),
    STR_HEX_SHA512("[strHexSHA512]"),
    TM_INDEX_SUBDIRS("[tmIndexSubDirs.size]"),
    RECORD_TIME("[recordTime]"),
    NCPARAMFV("[NcParamFv]"),
    STR_DEFAULT("[strDefault]"),
    STR_INPUT("[strInput]"),
    STR_GENERATE_ERROR_VAL("[strGenerateErrorVal]"),
    TMP_IDS_FILE("[TmpIDsFile]"),
    FILE_FOR_CHECK("[fileForCheck]"),
    
    DISK_ID("[diskID]"),
    HUMAN_ALIAS("[humanAlias]"),
    PROGRAM_ALIAS("[programAlias]"),
    STR_FILE_STORE("[strFileStore]"),
    DISK_LETTER("[diskLetter]"),
    LONG_SERIAL_NUMBER("[longSerialNumber]"),
    STR_HEX_SERIAL_NUMBER("[strHexSerialNumber]"),
    DISK_FS_TYPE("[diskFStype]"),
    IS_READ_ONLY("[isReadonly]"),
    AVAIL_SPACE("[availSpace]"),
    TOTAL_SPACE("[totalSpace]"),
    UN_ALLOCATED_SPACE("[unAllocatedSpace]"),
    USED_SPACE("[usedSpace]"),
    STR_CFG_PATH("[strCfgPath]"),
    CLEAN_RESULT("[CleanResult]");
    private String strMsg;
    NcStrVarDescription(String strMsg){
        this.strMsg = strMsg;
    }
    public String getStr(){
        return strMsg;
    }
}
