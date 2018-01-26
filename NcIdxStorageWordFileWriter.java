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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcIdxStorageWordFileWriter {

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxStorageWordManager#putInStorageWord(java.lang.String, java.util.TreeMap) }
     * </ul>
     * @param inFuncWrite
     * @param updatedRecords
     * @return
     */
    protected static int ncUpdateData(File inFuncWrite, TreeMap<Long, NcDcIdxStorageWordToFile> updatedRecords){
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(inFuncWrite)))
        {
            oos.writeObject(updatedRecords);
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxStorageWordFileWriter.class.getCanonicalName(), ex);
            return -1;
        } 
        return updatedRecords.size();
    }
}
