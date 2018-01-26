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
public class NcIdxWordFileReader {
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#putWord(java.util.TreeMap) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getWord(java.util.TreeMap) }
     * </ul>
     * @param readedWord
     * @param rID
     * @return 
     */    
    protected static TreeMap<Long, NcDcIdxWordToFile> ncReadFromWord(String readedWord, long rID){
        TreeMap<Long, NcDcIdxWordToFile> ncDataFromWordFile;
        String strCfgPath =  NcIdxFileManager.getFileNameToRecord(
                NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirWords()) + "/w-" + readedWord, rID);
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<Long, NcDcIdxWordToFile>();
        };
        //mcGetWorkCfgDirName() + workFileNames[0];
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromWordFile = (TreeMap<Long, NcDcIdxWordToFile>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxWordFileReader.class.getCanonicalName(), ex);
            return new TreeMap<Long, NcDcIdxWordToFile>();
        } 
        return ncDataFromWordFile;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getAllDataForWord(java.lang.String) }
     * </ul>
     * @param inFuncFile
     * @return 
     */
    protected static TreeMap<Long, NcDcIdxWordToFile> ncReadFromWordFile(File inFuncFile){
        TreeMap<Long, NcDcIdxWordToFile> ncDataFromWordFile;
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFuncFile)))
        {
            ncDataFromWordFile = (TreeMap<Long, NcDcIdxWordToFile>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxWordFileReader.class.getCanonicalName(), ex);
            return new TreeMap<Long, NcDcIdxWordToFile>();
        } 
        return ncDataFromWordFile;
    }
}
