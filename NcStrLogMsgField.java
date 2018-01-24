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
public enum NcStrLogMsgField {
    APP_LOGIC_NOW("[APPNOW]"),
    APP_LOGIC_NEXT_WAY_VAR("[APPNEXTWAYVAR]"),
    EXCEPTION_MSG("[EXCEPTION_MSG]"),
    ERROR("[ERROR]"),
    ERROR_CRITICAL("[ERROR][CRITICAL]"),
    WARNING("[WARNING]"),
    INFO("[INFO]"),
    INFO_LOGIC_POSITION("[INFOLOGICPOSITION]"),
    MSG_ERROR("[MSG][ERROR]"),
    MSG_ERROR_CRITICAL("[MSG][ERROR][CRITICAL]"),
    MSG_WARNING("[MSG][WARNING]"),
    MSG_INFO("[MSG][INFO]"),
    
    VARVAL("[VARVAL]"),
    VARNAME("[VARNAME]"),
    CHECK_RESULT("[CHECK_RESULT]"),
    
    TO_RETURN("[TO_RETURN]"),
    DISCARDED("[DISCARDED]"),
    IN_SET_DEFAULT_ERROR_GENERATE_ERROR_VAL("[INSETDEFAULTERRORGENERATEERRORVALUE]"),
    
    DELIMITER("[]"),
    TIME("[TIME]"),
    THREAD("[THREAD]"),
    THREAD_GROUP("[THREADGROUP]"),
    THREAD_GROUP_NAME("[THREADGROUPNAME]"),
    COUNT("[COUNT]"),
    CLASSLOADER("[CLASSLOADER]"),
    TOSTRING("[TOSTRING]"),
    NAME("[NAME]"),
    CANONICALNAME("[CANONICALNAME]"),
    ID("[ID]"),
    STATE("[STATE]"),
    STACK("[STACK]"),
    TRACE("[TRACE]"),
    FILENAME("[FILENAME]"),
    CLASSNAME("[CLASSNAME]"),
    METHODNAME("[METHODNAME]"),
    LINENUM("[LINENUM]"),
    NATIVE("[NATIVE]"),
    ELEMENTNUM("[ELEMENTNUM]"),
    MSG("[MSG]");
    private String strMsg;
    NcStrLogMsgField(String strMsg){
        this.strMsg = strMsg;
    }
    public String getStr(){
        return strMsg;
    }
}
