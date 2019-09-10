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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilStorage {
    /**
     * 
     * @param currentDateTimeStamp
     * @return 
     */
    protected static Path getIterationLogSubDir(String currentDateTimeStamp){
        String iterationTimeStamp = new String();
        String logAppSubDir = new String();
        Path toReturn;
        try{
            iterationTimeStamp = (String) currentDateTimeStamp;
            if( iterationTimeStamp.isEmpty() ){
                return null;
            }
            logAppSubDir = getLogSubDir().toString();
            if( logAppSubDir.isEmpty() ){
                return null;
            }
            toReturn = Paths.get(logAppSubDir, iterationTimeStamp);
            if( AdihFileOperations.createDirIfNotExist(toReturn) ){
               if( AdihFileOperations.pathIsReadWriteNotLink(toReturn) ){
                   return toReturn;
               }
            }
            return null;
        } finally {
            toReturn = null;
            AdihUtilization.utilizeStringValues(new String[]{iterationTimeStamp, logAppSubDir});
        }    
    }
    /**
     * 
     * @return 
     */
    protected static Path getLogSubDir(){
        String appCheckedPath = getAppCheckedPath().toString();
        String subDirPrefix = getSubDirPrefix();
        Path toReturn = Paths.get(appCheckedPath, subDirPrefix);
        if( Files.notExists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
            try {
                Files.createDirectories(toReturn);
            } catch (IOException ex) {
                System.err.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                ex.printStackTrace();
                System.exit(0);
            }
        }
        try {
            AdihFileOperations.pathIsReadWriteNotLink(toReturn);
        } catch (IOException ex) {
            System.err.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            ex.printStackTrace();
            System.exit(0);
        }
        return toReturn;
    }
    
    /**
     * 
     * @return 
     */
    private static String getSubDirPrefix(){
        return new String(AdilConstants.LOG_SUB_DIR_PREFIX);
    }
}
