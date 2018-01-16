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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcIdxDirListFileReader {
    /**
     * Directory List
     * @param dirListID
     * @return 
     */ 
    public static TreeMap<Long, NcDcIdxDirListToFileAttr> ncReadFromDirListFile(long dirListID){
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
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }

    /**
     *
     * @param dirListID
     * @return
     */
    public static TreeMap<Long, NcDcIdxDirListToFileExist> ncReadFromDirListExist(long dirListID){
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
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
    public static TreeMap<Long, NcDcIdxDirListToFileAttr> ncReadFromDirListFileByName(String strCfgPath){
        TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataFromDirList;
        

        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileAttr>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
}
