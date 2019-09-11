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
import java.nio.file.Paths;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilStorage {
    /**
     * <code>/...applicationpath/adilog/currentDateTimeStamp/</code>
     * @return directory for iteration log or null if it is not created or checked
     */
    protected static Path getStorageLogIterationDir(){
        String nowTimeStringMillisFsNames = new String();
        Path iterationLogSubDir;
        try {
            nowTimeStringMillisFsNames = AdihGetvalues.getNowTimeStringMillisFsNames();
            iterationLogSubDir = getIterationLogSubDir(nowTimeStringMillisFsNames);
            if( iterationLogSubDir == null ){
                return null;
            }
            return iterationLogSubDir;
        } finally {
            iterationLogSubDir = null;
            AdihUtilization.utilizeStringValues(new String[]{nowTimeStringMillisFsNames});
        }
    }
    /**
     * <code>/...applicationpath/adilog/currentDateTimeStamp/</code>
     * @param currentDateTimeStamp
     * @return directory for iteration log or null if it is not created or checked
     */
    protected static Path getIterationLogSubDir(String currentDateTimeStamp){
        String iterationTimeStamp = new String();
        String logAppSubDir = new String();
        Path toReturn;
        Boolean isCreated;
        Boolean isReadWriteNotLink;
        try{
            iterationTimeStamp = (String) currentDateTimeStamp;
            if( iterationTimeStamp.isEmpty() ){
                return null;
            }
            Path logSubDir = getLogSubDir();
            if( logSubDir == null ){
                return null;
            }    
            logAppSubDir = logSubDir.toString();
            if( logAppSubDir.isEmpty() ){
                return null;
            }
            toReturn = Paths.get(logAppSubDir, iterationTimeStamp);
            isCreated = AdihFileOperations.createDirIfNotExist(toReturn);
            if( !isCreated ){
                return null;
            }
            isReadWriteNotLink = AdihFileOperations.pathIsReadWriteNotLink(toReturn);
            if( !isReadWriteNotLink ){
                return null;
            }
            return toReturn;
        } finally {
            toReturn = null;
            AdihUtilization.utilizeStringValues(new String[]{iterationTimeStamp, logAppSubDir});
            isCreated = null;
            isReadWriteNotLink = null;
        }    
    }
    /**
     * 
     * @return subdirectory for logging or null if it is not created or checked
     */
    protected static Path getLogSubDir(){
        Path appCheckedPath;
        String strAppCheckedPath = new String();
        String subDirPrefix = new String();
        Path toReturn;
        Boolean isCreated;
        Boolean isReadWriteNotLink;
        try {
            appCheckedPath = AdihFileOperations.getForLogDirectory();
            if( appCheckedPath == null ){
                return null;
            }
            strAppCheckedPath = appCheckedPath.toString();
            subDirPrefix = getSubDirPrefix();
            toReturn = Paths.get(strAppCheckedPath, subDirPrefix);
            isCreated = AdihFileOperations.createDirIfNotExist(toReturn);
            if( !isCreated ){
                return null;
            }
            isReadWriteNotLink = AdihFileOperations.pathIsReadWriteNotLink(toReturn);
            if( !isReadWriteNotLink ){
                return null;
            }
            return toReturn;
        } finally {
            appCheckedPath = null;
            AdihUtilization.utilizeStringValues(new String[]{strAppCheckedPath, subDirPrefix});
            toReturn = null;
            isCreated = null;
            isReadWriteNotLink = null;
        }
    }
    
    /**
     * 
     * @return 
     */
    private static String getSubDirPrefix(){
        return new String(AdilConstants.LOG_SUB_DIR_PREFIX);
    }
}
