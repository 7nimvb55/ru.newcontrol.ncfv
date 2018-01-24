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
public enum NcStrGUILabel {
    TITLE_APP("Search in index"),
    TITLE_MODAL_LOG_VIEW("Log view"),
    TITLE_MODAL_ENV_VIEW("System environment"),
    TITLE_MODAL_PROP_VIEW("System properties"),
    UPDATE("Update"),
    SEARCH("Search"),
    LOG_VIEW("Log View"),
    ENV_VIEW("Env View"),
    PROPERTIES_VIEW("Properties View"),
    SETTINGS("Settings"),
    CHECK_SUBDIR("Check SubDir"),
    DIR_IN_INDEX("Dir in index"),
    DIR_OUT_INDEX("Dir out index"),
    APP_EXIT("Exit"),
    ABOUT("About");
    private String strMsg;
    NcStrGUILabel(String strMsg){
        this.strMsg = strMsg;
    }
    public String getStr(){
        return strMsg;
    }
}
