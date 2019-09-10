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
     * <li>  0 - WordRouter
     * <li>  1 - WordReader
     * <li>  2 - WordWriter
     * <li>  3 - WordStorageRouter
     * <li>  4 - WordStorageReader
     * <li>  5 - WordStorageWriter
     * </ul>
     * @return 
     */
    protected static String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "WordRouter",
                "WordReader",
                "WordWriter",
                "WordStorageRouter",
                "WordStorageReader",
                "WordStorageWriter",
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
}
