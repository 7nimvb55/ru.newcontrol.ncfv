/*
 * Copyright 2018 wladimirowichbiaran.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxStorageStream {
    protected static void putData(TreeMap<Long, ?> dataToRecord) throws Exception{
        if( dataToRecord == null ){
            String strEx = NcStrExceptionMsg.DATA_FOR_RECORD_IS_NULL.getStr();
            throw new Exception(strEx);
        }
        if( dataToRecord.isEmpty() ){
            String strEx = NcStrExceptionMsg.DATA_FOR_RECORD_IS_NULL.getStr();
            throw new Exception(strEx);
        }
        if( !checkUniformity(dataToRecord) ){
            String strEx = NcStrExceptionMsg.DATA_FOR_RECORD_NOT_HOMOGENEOUS.getStr();
            throw new Exception(strEx);
        }
        if( dataToRecord.size() == 100 ){
            
        }
        if( dataToRecord.size() < 100 ){
            
        }
        if( dataToRecord.size() > 100 ){
            
        }
        
    }
    private static void routeStorageType(TreeMap<Long, ?> dataToRecord){
        Class<?> aClass = dataToRecord.firstEntry().getValue().getClass();
        if( aClass.isInstance(NcDcIdxDirListToFileAttr.class) ){
            
        } else if( aClass.isInstance(NcDcIdxDirListToFileExist.class) ) {
            
        } else if( aClass.isInstance(NcDcIdxDirListToFileHash.class) ) {
            
        } else if( aClass.isInstance(NcDcIdxDirListToFileType.class) ) {
            
        } else if( aClass.isInstance(NcDcIdxLongWordListToFile.class) ) {
            
        } else if( aClass.isInstance(NcDcIdxStorageWordToFile.class) ) {
            
        } else if( aClass.isInstance(NcDcIdxWordToFile.class) ) {
            
        } else {
            throw new IllegalArgumentException("Data type is wrong and not compatable type of class");
        }
        
    }
    private static boolean checkUniformity(TreeMap<Long, ?> dataToCheck){
        Object firstValue = dataToCheck.firstEntry().getValue();
        Class<?> firstValueClass = firstValue.getClass();
        boolean dataCorrect = true;
        for (Map.Entry<Long, ? extends Object> entry : dataToCheck.entrySet()) {
            Object value = entry.getValue();
            if( !firstValueClass.isInstance(value) ){
                dataCorrect = false;
            }
        }
        return dataCorrect;
    }
    private static int writeToStorage(TreeMap<Long, ?> dataToRecord, Path fileForRecord) throws Exception{
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(fileForRecord.toFile())))
        {
            oos.writeObject(dataToRecord);
        }
        catch(IOException ex){
            NcAppHelper.logException(
                    NcFsIdxStorageStream.class.getCanonicalName(), ex);
            throw ex;
        } 
        return dataToRecord.size();
    }
    private static TreeMap<Long, ?> readFromStorage(Path fileForRead){
        TreeMap<Long, ?> ncDataFromDirList;
        /*String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirList())+"/dl", dirListID);
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }*/
        try(ObjectInputStream ois = 
                new ObjectInputStream(
                        new FileInputStream(fileForRead.toFile())))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcFsIdxStorageStream.class.getCanonicalName(), ex);
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
}
