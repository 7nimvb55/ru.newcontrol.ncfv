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
public class NcIdxLongWordListFileReader {
    /**
     * Directory List Word Long
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListManager#getOrCreateLongWordID(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#getForSearchLongWordID(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * </ul>
     * @param dataForRead
     * @param rID
     * @return 
     */      
    protected static TreeMap<Long, NcDcIdxLongWordListToFile> ncReadFileContainedId(NcDcIdxLongWordListToFile dataForRead, long rID){
        TreeMap<Long, NcDcIdxLongWordListToFile> ncDataFromDirList;
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(
                NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWordList()) + "/wl-"
                + dataForRead.name.substring(0, 4),rID);
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<Long, NcDcIdxLongWordListToFile>();
        }
        //mcGetWorkCfgDirName() + workFileNames[0];
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxLongWordListToFile>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxLongWordListFileReader.class.getCanonicalName(), ex);
            return new TreeMap<Long, NcDcIdxLongWordListToFile>();
        } 
        return ncDataFromDirList;
    }
    
}
