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

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilHelper {
    /**
     * Used in generate code names for log typed bus string lines
     * <ul>
     * <li>   0 -   Main
     * <li>   1 -   Index
     * <li>   2 -   DirListManager
     * <li>   3 -   DirListRead
     * <li>   4 -   DirListWrite
     * <li>   5 -   FileListBuild
     *              
     * <li>   6 -   WordStorageFilter
     * <li>   7 -   WordStorageRouter
     * <li>   8 -   WordStorageReader
     * <li>   9 -   WordStorageWriter
     *              
     * <li>  10 -   WordRouter
     * <li>  11 -   WordReader
     * <li>  12 -   WordWriter
     * <li>  13 -   WordEvent
     * <li>  14 -   AdibWorker
     * </ul> 
     * @return 
     */
    protected static String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "Main",
                "Index",
                "DirListManager",
                "DirListRead",
                "DirListWrite",
                "FileListBuild",
                
                "WordStorageFilter",
                "WordStorageRouter",
                "WordStorageReader",
                "WordStorageWriter",
                
                "WordRouter",
                "WordReader",
                "WordWriter",
                "WordEvent",
                "ProcessControll",
            };
            return namesForReturn;
        } finally {
            namesForReturn = null;
        }
    }
    /**
     * 
     * @return 
     */
    protected static String getNowTimeString(){
        if(AdilConstants.LOGNOWTIMEINHUMANFORMAT){
            return AppFileOperationsSimple.getNowTimeStringWithMsHuman();
        }
        return AppFileOperationsSimple.getNowTimeStringWithMS();
    }
    protected static String variableNameValue(String[] inputedValues){
        String strForReturn = new String();
        Boolean isName = Boolean.TRUE;
        try {
            for(String itemVars : inputedValues){
                if( isName ){
                    strForReturn = strForReturn.concat(AdilConstants.VARNAME).concat(itemVars);
                    isName = Boolean.FALSE;
                } else {
                    strForReturn = strForReturn.concat(AdilConstants.VARVAL).concat(itemVars);
                    isName = Boolean.TRUE;
                }
            }
            return strForReturn;
        } finally {
            AdihUtilization.utilizeStringValues(new String[]{strForReturn});
            AdihUtilization.utilizeStringValues(inputedValues);
        }
    }
}
