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
public enum NcStrExceptionMsg {
    DATA_FOR_RECORD_IS_NULL("Input data for record to storage is null"),
    DATA_FOR_RECORD_IS_EMPTY("Input data for record to storage is empty"),
    DATA_FOR_RECORD_NOT_HOMOGENEOUS(
        "Input data for record to storage are not homogeneous");
    
    private String strMsg;
    NcStrExceptionMsg(String strMsg){
        this.strMsg = strMsg;
    }
    protected String getStr(){
        return strMsg;
    }
}
