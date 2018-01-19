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
public enum NcStrServiceMsg {
    ERROR_FILE_NOT_EXIST("notExistFileError"),
    ERROR_FILE_NOT_CANONICAL_PATH("Can not File.getCanonicalPath() for: "),
    ERROR_NOT_CREATE("Can not create: "),
    PATH_INDEX_DIRECTORY("Path of index directory: "),
    PATH_WORK_FILE("Path of work file: "),
    PATH_SUBDIR("Path of subDir: "),
    EXIST("Exist: "),
    CANREAD("canRead: "),
    CANWRITE("canWrite: "),
    NEWLINE("\n"),
    TAB("\t"),
    SPACE(" "),
    COLON(":"),
    HASH_CALC("Calculated hash: "),
    HASH_RECORD("in record hash: "),
    RESULT("result: "),
    HASH_RECORD_IS("RecordHash is: "),
    WORK_CFG_HASH("Work config hash: "),
    FOR_WRITE("for write: "),
    FROM_READ("from read: "),
    
    STRING_EQUAL("String equal"),
    PATH_INPUT("Path input: "),
    PATH_DEFAULT("Path default: "),
    PATH_CONTINUE_NOT_VALID("Path continue not valid"),
    PATH_START_NOT_VALID("Path start not valid"),
    PATH_FOR_NOT_WINDOWS_SYSTEM("Path for not windows system"),
    DEFAULT_STAGE("default stage");
    
    private String strMsg;
    NcStrServiceMsg(String strMsg){
        this.strMsg = strMsg;
    }
    public String getStr(){
        return strMsg;
    }
}
