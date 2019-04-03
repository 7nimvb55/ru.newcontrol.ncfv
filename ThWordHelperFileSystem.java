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
 * Provades for open index storage file in zip fs, read folders structure, and read or write files
 * from word index folder
 * @author wladimirowichbiaran
 */
public class ThWordHelperFileSystem {
    /**
     * create strinc for directory by typeWord
     * buildName for word Store in format:
     * heximalViewForWord-SizeDataRecords-Volume
     * 
     */
    /**
     * build strings for directories by format
     * <code>/typeWord/hexTagName.substring(0,3)/subString.length() </code>
     * @param inputCodePoinType
     * @param partHexTagName
     * @param lengSubString 
     */
    protected static String buildTypeWordStoreSubDirictories(
            int inputCodePoinType,
            final String partHexTagName,
            final int lengSubString){
        Path toReturnSubDirictoriesName;
        try {
            toReturnSubDirictoriesName = Paths.get(
                    AppFileNamesConstants.DIR_IDX_ROOT, 
                    String.valueOf(inputCodePoinType), 
                    partHexTagName, String.valueOf(lengSubString));
            return toReturnSubDirictoriesName.toString();
        } finally {
            toReturnSubDirictoriesName = null;
        }
        
    }
}
