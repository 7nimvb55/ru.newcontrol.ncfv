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

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcIdxDirListFileWriter {
/**
 * Directory List
     * @param ncDataToDirListFile
     * @param recID
 * @return 
 */    
    public static int ncWriteToDirListFile(TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataToDirListFile, long recID){
        if( ncDataToDirListFile == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirList().getAbsolutePath()+"/dl",recID))))
        {
            oos.writeObject(ncDataToDirListFile);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return ncDataToDirListFile.size();
    }

    /**
     *
     * @param ncDataToDirListFile
     * @param dirListID
     * @return
     */
    public static int ncWriteToDirListExist(TreeMap<Long, NcDcIdxDirListToFileExist> ncDataToDirListFile, long dirListID){
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirListExist().getAbsolutePath() + "/e", dirListID);
        if( ncDataToDirListFile == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(strCfgPath)))
        {
            oos.writeObject(ncDataToDirListFile);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return ncDataToDirListFile.size();
    }
}
