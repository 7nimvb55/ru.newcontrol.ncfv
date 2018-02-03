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
public enum NcStrFileDir {
    DIR_IDX("/ncfvdi"),
    DIR_TMP("/t"),
    DIR_DIR_LIST("/di"),
    DIR_JOURNAL("/j"),
    DIR_FILE_LIST("/fl"),
    DIR_FILE_TYPE("/ft"),
    DIR_FILE_HASH("/fh"),
    DIR_FILE_EXIST("/fx"),
    DIR_WORD("/w"),
    DIR_STORAGE_WORD("/sw"),
    DIR_LONG_WORD_LIST("/lw"),
    DIR_LONG_WORD_DATA("/ln"),
    
    DIR_APP_DATA("/appdata"),
    FILE_JOURNAL_DISK("/jdisk.dat"),
    FILE_DISK_DATA("/disks.dat"),
    FILE_ID_DATA("/ids.dat"),
    
    FILE_APP_LOG("/app.log"),
    
    DIR_APP_CFG("/etc"),
    FILE_APP_CFG("/ncfv.conf"),
    
    FILE_SRCH_KEY_OUT("/keywordout.list"),
    FILE_SRCH_KEY_IN("/keywordin.list"),
    FILE_SRCH_DIR_OUT("/dirout.list"),
    FILE_SRCH_DIR_IN("/dirin.list");

    private String fileSubDirName;
    /**
     * Constructor
     * @param fileSubDirName 
     */
    NcStrFileDir(String fileSubDirName){
        this.fileSubDirName = fileSubDirName;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#getLogFile() }
     * </ul>
     * @return 
     */
    protected String getStr(){
        return fileSubDirName;
    }
}
