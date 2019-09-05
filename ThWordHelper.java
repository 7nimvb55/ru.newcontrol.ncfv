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

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Provades for open index storage file in zip fs, read folders structure, and read or write files
 * from word index folder
 * @author wladimirowichbiaran
 */
public class ThWordHelper {
    private static String storagePrefix = AppFileNamesConstants.SZFS_WORD_FILE_PREFIX;
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
    protected static String getStoragePrefix(){
        return new String(storagePrefix);
    }
    /**
     * 
     * @param inputCodePointType
     * @param partHexTagName
     * @param lengSubString
     * @return 
     */
    protected static String buildTypeWordStoreSubDirictories(
            int inputCodePointType,
            final String partHexTagName,
            final int lengSubString){
        Path toReturnSubDirictoriesName;
        try {
            toReturnSubDirictoriesName = Paths.get(
                    AppFileNamesConstants.DIR_IDX_ROOT, 
                    String.valueOf(inputCodePointType), 
                    partHexTagName, String.valueOf(lengSubString));
            return toReturnSubDirictoriesName.toString();
        } finally {
            toReturnSubDirictoriesName = null;
        }
        
    }
    /**
     * 
     * @param namePrefixFileNameFromFlowInputed
     * @param recordsCountInputed
     * @param volumeNumberInputed
     * @return 
     */
    protected static String fileNameBuilder(
            final String namePrefixFileNameFromFlowInputed,
            final Integer recordsCountInputed,
            final Integer volumeNumberInputed){
        String namePrefixFunc;
        Integer recordsCountFunc;
        Integer volumeNumberFunc;
        String buildedFileName;
        try {
            namePrefixFunc = new String(namePrefixFileNameFromFlowInputed);
            recordsCountFunc = (Integer) recordsCountInputed;
            volumeNumberFunc = (Integer) volumeNumberInputed;
            buildedFileName = new String()
                .concat(getStoragePrefix())
                .concat(namePrefixFunc.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                .concat(String.valueOf(recordsCountFunc))
                .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                .concat(String.valueOf(volumeNumberFunc));
            return buildedFileName;
        } finally {
            namePrefixFunc = null;
            recordsCountFunc = null;
            volumeNumberFunc = null;
            buildedFileName = null;
        }
    }
    /**
     * TRUE if directory exist or create
     * @param inputedDirName
     * @return FALSE if directory not exist and not create
     */
    protected static Boolean createDirIfNotExist(Path inputedDirName){
         if( Files.notExists(inputedDirName, LinkOption.NOFOLLOW_LINKS) ){
            try{
                Files.createDirectories(inputedDirName);
                return Boolean.TRUE;
            } catch (FileAlreadyExistsException exAlreadyExist) {
                System.err.println(exAlreadyExist.getMessage());
                exAlreadyExist.printStackTrace();
            } catch (SecurityException exSecurity) {
                System.err.println(exSecurity.getMessage());
                exSecurity.printStackTrace();
            } catch (UnsupportedOperationException exUnSupp) {
                System.err.println(exUnSupp.getMessage());
                exUnSupp.printStackTrace();
            } catch (IOException exIoExist) {
                System.err.println(exIoExist.getMessage());
                exIoExist.printStackTrace();
            } 
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    /**
     * 
     * @param nowWritedFile
     * @param dataForWrite
     * @return 
     */
    protected static Boolean writeDataToStorage(Path nowWritedFile, ConcurrentSkipListMap<UUID, TdataWord> dataForWrite){
        try( ObjectOutputStream oos = 
            new ObjectOutputStream(Files.newOutputStream(nowWritedFile)) )
        {
            oos.writeObject(dataForWrite);
            oos.flush();
            return Boolean.TRUE;
        } catch(NotSerializableException exNotSer){
            System.err.println(exNotSer.getMessage());
            exNotSer.printStackTrace();
        } catch(InvalidClassException exInvClass){
            System.err.println(exInvClass.getMessage());
            exInvClass.printStackTrace();
        } catch(IOException exIo){
            System.err.println(exIo.getMessage());
            exIo.printStackTrace();
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @return 
     */
    protected static Boolean moveAfterWrite(Path srcFileName, Path destFileName){
        try {
            Files.move(srcFileName, destFileName, StandardCopyOption.ATOMIC_MOVE);
            //after delete oldFile
            return Boolean.TRUE;
        } catch(SecurityException exSecurity) {
            System.err.println(exSecurity.getMessage());
            exSecurity.printStackTrace();
        } catch(AtomicMoveNotSupportedException exAtomic) {
            System.err.println(exAtomic.getMessage());
            exAtomic.printStackTrace();
        } catch(FileAlreadyExistsException exAlreadyExists) {
            System.err.println(exAlreadyExists.getMessage());
            exAlreadyExists.printStackTrace();
        } catch(UnsupportedOperationException exUnsupported) {
            System.err.println(exUnsupported.getMessage());
            exUnsupported.printStackTrace();
        } catch(IOException exIO) {
            System.err.println(exIO.getMessage());
            exIO.printStackTrace();
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @param deleteFile
     * @return 
     */
    protected static Boolean deleteFileFromStorage(Path deleteFile){
        Boolean resultOperation;
        try {
            resultOperation = Boolean.TRUE;
            try {
                resultOperation = Files.deleteIfExists(deleteFile);
                return resultOperation;
            } catch (DirectoryNotEmptyException exNotEmptyDir) {
                exNotEmptyDir.printStackTrace();
            } catch (SecurityException exSecurity) {
                exSecurity.printStackTrace();
            } catch (IOException exInOut) {
                exInOut.printStackTrace();
            }
            return Boolean.FALSE;
        } finally {
            resultOperation = null;
        }
    }
    protected static ConcurrentSkipListMap<UUID, TdataWord> readFromFile(Path forReadFileName){
        ConcurrentSkipListMap<UUID, TdataWord> readedFromFileData = new ConcurrentSkipListMap<UUID, TdataWord>();
        try{
            try( ObjectInputStream ois =
                new ObjectInputStream(Files.newInputStream(forReadFileName)) )
            {
                readedFromFileData.putAll((ConcurrentSkipListMap<UUID, TdataWord>) ois.readObject());
                return readedFromFileData;
            } catch(ClassNotFoundException exCnf){
                System.err.println(exCnf.getMessage());
                exCnf.printStackTrace();
            } catch(InvalidClassException exIce){
                System.err.println(exIce.getMessage());
                exIce.printStackTrace();
            } catch(StreamCorruptedException exSce){
                System.err.println(exSce.getMessage());
                exSce.printStackTrace();
            } catch(OptionalDataException exOde){
                System.err.println(exOde.getMessage());
                exOde.printStackTrace();
            } catch(IOException exIo){
                System.err.println(exIo.getMessage());
                exIo.printStackTrace();
            }
            return new ConcurrentSkipListMap<UUID, TdataWord>();
        } finally {
            ThWordHelper.utilizeTdataWord(readedFromFileData);
            readedFromFileData = null;
        }
    }
    /**
     * 
     * @param forValidateInputed
     * @return 
     */
    protected static Boolean isTdataWordValid(final TdataWord forValidateInputed){
        TdataWord forValidateFunction;
        Integer recordHash;
        Integer calculatedHash;
        try {
            forValidateFunction = (TdataWord) forValidateInputed;
            recordHash = (Integer) forValidateFunction.recordHash;
            calculatedHash = (
                new String("")
                .concat(forValidateFunction.randomUUID.toString())
                .concat(forValidateFunction.recordUUID.toString())
                .concat(forValidateFunction.dirListFile)
                .concat(forValidateFunction.strSubString)
                .concat(String.valueOf(forValidateFunction.strSubStringHash))
                .concat(forValidateFunction.hexSubString)
                .concat(String.valueOf(forValidateFunction.typeWord))
                .concat(String.valueOf(forValidateFunction.hexSubStringHash))
                .concat(String.valueOf(forValidateFunction.positionSubString))
                .concat(String.valueOf(forValidateFunction.lengthSubString))
                .concat(String.valueOf(forValidateFunction.recordTime))).hashCode();
            if( recordHash == calculatedHash ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        finally {
            forValidateFunction = null;
            recordHash = null;
            calculatedHash = null;
        }
    }
    /**
     * 
     * @param prevData
     * @return 
     */
    protected static ConcurrentSkipListMap<UUID, TdataWord> doUtilizationDataInitNew(ConcurrentSkipListMap<UUID, TdataWord> prevData){
        utilizeTdataWord(prevData);
        return new ConcurrentSkipListMap<UUID, TdataWord>();
    }
    /**
     * 
     * @param forUtilizationData 
     */
    protected static void utilizeTdataWord(ConcurrentSkipListMap<UUID, TdataWord> forUtilizationData){
        UUID keyForDelete;
        TdataWord removedData;
        try {
            for( Map.Entry<UUID, TdataWord> deletingItem : forUtilizationData.entrySet() ){
                keyForDelete = deletingItem.getKey();
                removedData = forUtilizationData.remove(keyForDelete);
                removedData.dirListFile = null;
                removedData.hexSubString = null;
                removedData.hexSubStringHash = null;
                removedData.lengthSubString = null;
                removedData.positionSubString = null;
                removedData.randomUUID = null;
                removedData.recordHash = null;
                removedData.recordTime = null;
                removedData.recordUUID = null;
                removedData.strSubString = null;
                removedData.strSubStringHash = null;
                removedData.typeWord = null;
                removedData = null;
                keyForDelete = null;
            }
            forUtilizationData = null;
        } finally {
            keyForDelete = null;
            removedData = null;
        }
    }
    /**
     * 
     * @param valuesForDelete 
     */
    protected static void utilizeStringValues(String[] valuesForDelete){
        for(String deletedItem : valuesForDelete){
            deletedItem = null;
        }
        valuesForDelete = null;
    }
}
