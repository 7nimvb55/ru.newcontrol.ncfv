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
 * Adih
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>helper Helper for set and get simply variables and constants
 * </ul>
 * @author wladimirowichbiaran
 */
public class AdihHelper {
    protected static String[] getParamNames(){
        String[] namesForReturn = new String[] {};
        try {
            namesForReturn = null;
            namesForReturn = new String[] {
                "UserHome",
                "ClassPathApplicationDirectory",
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_DIR_LIST).concat("indexDirList"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_TMP).concat("indexTempData"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_JOURNAL).concat("indexJournal"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_LIST).concat("indexFileList"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_TYPE).concat("indexFileType"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_HASH).concat("indexFileHash"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_EXIST).concat("indexFileExist"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD).concat("indexWord"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD).concat("indexStorageWord"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_LONG_WORD_LIST).concat("indexLongWordList"),
                new String(AppFileNamesConstants.FILE_INDEX_PREFIX_LONG_WORD_DATA).concat("indexLongWordData"),
            };
            return namesForReturn;
        } finally {
            AdihUtilization.utilizeStringValues(namesForReturn);
            namesForReturn = null;
        }
    }
}
