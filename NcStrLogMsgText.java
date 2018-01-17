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
public enum NcStrLogMsgText {
    APP_GUI_START("Application start in GUI mode"),
    APP_ERROR_EXIT("Application stop with critical error"),
    LOG_CREATE("Log file created"),
    LOG_RECORD_APPEND("Append new record");
    private String strMsg;
    NcStrLogMsgText(String strMsg){
        this.strMsg = strMsg;
    }
    public String getStr(){
        return strMsg;
    }
}
