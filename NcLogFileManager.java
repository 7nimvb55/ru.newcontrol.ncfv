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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import static ru.newcontrol.ncfv.NcPreRunFileViewer.getDefaultParametersForCfg;
import static ru.newcontrol.ncfv.NcPreRunFileViewer.getRemTextForCfgFile;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcLogFileManager {
    public static File getLogFile(){
        String strAppDataSubDir = NcIdxFileManager.getOrCreateAppDataSubDir();
        String strLogFilePath = NcIdxFileManager.strPathCombiner(strAppDataSubDir, "/app.log");
        File fileLog = new File(strLogFilePath);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileLog) ){
            createLogFile(strLogFilePath);
        }
        return fileLog;
    }
    private static void createLogFile(String ncStrCfgPath){
        String strTime = java.time.LocalDateTime.now().toString();
        String text = ": [time]: " + strTime + ": log file created";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ncStrCfgPath)))
        {
            bw.write(text);
            bw.newLine();
        }
        catch(IOException ex){
            String strMsg = "Can not create log file in:\n"
                    + ncStrCfgPath + "\n";
            NcAppHelper.appExitWithMessage(strMsg + ex.getMessage());
        }
    }
    public static void putToLog(String strToLog){
        if( strToLog.length() > 0 ){
            int logCountLines = NcfvRunVariables.getLogLinesCount();
            TreeMap<Long, String> strCurrentLog = new TreeMap<Long, String>();
            strCurrentLog.putAll(readFromLog());
            if( strCurrentLog.size() == logCountLines ){
                long idx = 0;
                TreeMap<Long, String> strNewLog = new TreeMap<Long, String>();
                strNewLog.putAll(strCurrentLog.tailMap(idx));
                strCurrentLog.clear();
                strCurrentLog = null;
                strCurrentLog.putAll(strNewLog);
            }
            strCurrentLog.put((long) strCurrentLog.size(), strToLog);
            writeLogLines(strCurrentLog);
        }
    }
    public static void putToLog(TreeMap<Long, String> toLogStr){
        if( toLogStr.size() > 0 ){
            int logCountLines = NcfvRunVariables.getLogLinesCount();
            TreeMap<Long, String> strCurrentLog = new TreeMap<Long, String>();
            strCurrentLog.putAll(readFromLog());
            if( strCurrentLog.size() == logCountLines ){
                long idx = toLogStr.size() - 1;
                TreeMap<Long, String> strNewLog = new TreeMap<Long, String>();
                strNewLog.putAll(strCurrentLog.tailMap(idx));
                strCurrentLog.clear();
                strCurrentLog = null;
                strCurrentLog.putAll(strNewLog);
            }
            strCurrentLog.putAll(toLogStr);
            writeLogLines(strCurrentLog);
        }
        
    }
    public static TreeMap<Long, String> readFromLog(){
        TreeMap<Long, String> strForReturn = new TreeMap<Long, String>();
        try(BufferedReader br = new BufferedReader(new FileReader(getLogFile())))
        {
            String s;
            long strIdx = 0;
            while((s=br.readLine())!=null){
                strForReturn.put(strIdx, s);
            }
        }
         catch(IOException ex){
            NcAppHelper.outMessage(ex.getMessage());
        }
        return strForReturn;
    }
    public static void writeLogLines(TreeMap<Long, String> toLogStr){
        File fileLog = getLogFile();
        if( toLogStr.size() > 0 ){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileLog)))
            {
                for( Map.Entry<Long, String> itemStr : toLogStr.entrySet() ){
                    bw.write(itemStr.getValue());
                    bw.newLine();
                }
                
            }
            catch(IOException ex){
                String strMsg = "Can not create log file in:\n"
                        + fileLog.getAbsolutePath() + "\n";
                NcAppHelper.appExitWithMessage(strMsg + ex.getMessage());
            }
        }
    }
}
