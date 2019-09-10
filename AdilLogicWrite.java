/*
 * Copyright 2019 wladimirowichbiaran.
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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilLogicWrite {
    private String iterationStartTime;
    AdilLogicWrite(){
        iterationStartTime = AppFileOperationsSimple.getNowTimeStringWithMS();
    }
    /**
     * 
     * @param ruleAdil 
     */
    protected void doWriteLinesIntoLog(AdilRule ruleAdil){
        AdilState adilState = ruleAdil.getAdilState();
        TreeMap<String, ArrayList<String>> pollBusData = adilState.pollBusData();
        updateLogDirectory();
        TreeMap<String, Path> listLogFiles;
        listLogFiles = AdilHelper.createLogFilesIteration(pollBusData);
        for(Map.Entry<String, ArrayList<String>> itemBusName : pollBusData.entrySet()){
            String keyBusName = itemBusName.getKey();
            ArrayList<String> valueBusLines = itemBusName.getValue();
            listLogFiles.get(keyBusName);
            for(String itemLogLine : valueBusLines){
                AdilHelper.writeLineIntoFile(keyBusName, itemLogLine);
            }
        }
    }
    /**
     * @todo in other directory get prefixes from AdilHelper and constants
     */
    protected void updateLogDirectory(){
        this.iterationStartTime = AppFileOperationsSimple.getNowTimeStringWithMS();
        Path logForHtmlCurrentLogSubDir = 
            AppFileOperationsSimple.getLogForHtmlCurrentLogSubDir(this.iterationStartTime);
        this.listLogStorageFiles = 
            AppFileOperationsSimple.getNewLogFileInLogHTML(logForHtmlCurrentLogSubDir);
        this.listLogStorageFiles.put(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR, logForHtmlCurrentLogSubDir);
    }
    
}
