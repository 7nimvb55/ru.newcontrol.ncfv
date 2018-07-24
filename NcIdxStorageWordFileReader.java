/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcIdxStorageWordFileReader {

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxStorageWordManager#putInStorageWord(java.lang.String, java.util.TreeMap) }
     * </ul>
     * @param inFuncFile
     * @return
     */
    protected static TreeMap<Long, NcDcIdxStorageWordToFile> ncReadFileContainedId(File inFuncFile){
        TreeMap<Long, NcDcIdxStorageWordToFile> ncReadedData;
        
        if ( !NcIdxFileManager.fileExistRWAccessChecker(inFuncFile)){
            return new TreeMap<Long, NcDcIdxStorageWordToFile>();
        };
        //mcGetWorkCfgDirName() + workFileNames[0];
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFuncFile)))
        {
            ncReadedData = (TreeMap<Long, NcDcIdxStorageWordToFile>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxStorageWordFileReader.class.getCanonicalName(), ex);
            return new TreeMap<Long, NcDcIdxStorageWordToFile>();
        } 
        return ncReadedData;
    }
}
