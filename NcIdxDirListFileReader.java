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
public class NcIdxDirListFileReader {
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListManager#putToDirectoryList(ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSearchInIndex#getWordSearchResult(java.util.ArrayList, java.util.ArrayList) }
     * </ul>
     * Directory List
     * @param dirListID
     * @return 
     */ 
    protected static TreeMap<Long, NcDcIdxDirListToFileAttr> ncReadFromDirListFile(long dirListID){
        TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataFromDirList;
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirList())+"/dl", dirListID);
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileAttr>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxDirListFileReader.class.getCanonicalName(), ex);
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListExistManager#putToDirListExistStart(ru.newcontrol.ncfv.NcDcIdxDirListToFileExist, long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListExistManager#putToDirListExistStop(ru.newcontrol.ncfv.NcDcIdxDirListToFileExist, long) }
     * </ul>
     * @param dirListID
     * @return
     */
    protected static TreeMap<Long, NcDcIdxDirListToFileExist> ncReadFromDirListExist(long dirListID){
        TreeMap<Long, NcDcIdxDirListToFileExist> ncDataFromDirList;
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirListExist()) + "/e", dirListID);
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileExist>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxDirListFileReader.class.getCanonicalName(), ex);
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListManager#getByListIDs(java.util.TreeMap) }
     * </ul>
     * @param strCfgPath
     * @return 
     */
    protected static TreeMap<Long, NcDcIdxDirListToFileAttr> ncReadFromDirListFileByName(String strCfgPath){
        TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataFromDirList;
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileAttr>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxDirListFileReader.class.getCanonicalName(), ex);
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
}
