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

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcIdxDirListExistManager {

    /**
     * Used in {@link ru.newcontrol.ncfv.NcIndexPreProcessFiles#makeIndexForFile(java.io.File) }
     * @param toWriteData
     * @param dirListID
     * @return
     */
    protected static long putToDirListExistStart(NcDcIdxDirListToFileExist toWriteData, long dirListID){
        TreeMap<Long, NcDcIdxDirListToFileExist> ncReadFromFileData;
        TreeMap<Long, NcDcIdxDirListToFileExist> ncWriteToFileData;
        ncWriteToFileData = new  TreeMap<>();
        ncReadFromFileData = NcIdxDirListFileReader.ncReadFromDirListExist(dirListID);

        if( ncReadFromFileData.isEmpty() ){
            ncWriteToFileData.put(dirListID, toWriteData);
        }
        else{
            ncWriteToFileData.putAll(ncReadFromFileData);
            ncWriteToFileData.put(dirListID, toWriteData);
        }
        NcIdxDirListFileWriter.ncWriteToDirListExist(ncWriteToFileData, dirListID);
        return ncWriteToFileData.size();
    }

    /**
     * Used in {@link ru.newcontrol.ncfv.NcIndexPreProcessFiles#makeIndexForFile(java.io.File) }
     * @param toWriteData
     * @param dirListID
     * @return
     */
    protected static long putToDirListExistStop(NcDcIdxDirListToFileExist toWriteData, long dirListID){
        TreeMap<Long, NcDcIdxDirListToFileExist> ncReadFromFileData;
        TreeMap<Long, NcDcIdxDirListToFileExist> ncWriteToFileData;
        ncWriteToFileData = new  TreeMap<>();
        ncReadFromFileData = NcIdxDirListFileReader.ncReadFromDirListExist(dirListID);

        if( ncReadFromFileData.isEmpty() ){
            return -1;
        }
        else{
            ncWriteToFileData.put(dirListID, toWriteData);
            for(Map.Entry<Long, NcDcIdxDirListToFileExist> itemIDSearch : ncReadFromFileData.entrySet()){
                if( itemIDSearch.getValue().dirListID == dirListID ){
                    ncReadFromFileData.put(itemIDSearch.getKey(), toWriteData);
                }
            }
            ncWriteToFileData.putAll(ncReadFromFileData);
        }
        NcIdxDirListFileWriter.ncWriteToDirListExist(ncWriteToFileData, dirListID);
        return ncWriteToFileData.size();
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    private static boolean isDirListExistDataWrong(NcDcIdxDirListToFileExist inFuncData){
        if( inFuncData == null ){
            return true;
        }
        if( !isDirListExistDataDataHashTrue(inFuncData) ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    private static boolean isDirListExistDataHasEmptyFiled(NcDcIdxDirListToFileExist inFuncData){
        if( inFuncData == null ){
            return true;
        }
        boolean dirListIdIsEmpty = inFuncData.dirListID == -777;
        boolean diskIdIsEmpty = inFuncData.diskID == -777;
        boolean pathWithOutDiskLetterIsEmpty = inFuncData.pathWithOutDiskLetter == "";
        boolean pathHashIsEmpty = inFuncData.pathHash == -777;
        boolean nanoTimeStartAddToIndexIsEmpty = inFuncData.nanoTimeStartAddToIndex == -777;
        boolean nanoTimeEndAddToIndexIsEmpty = inFuncData.nanoTimeEndAddToIndex == -777;
        return dirListIdIsEmpty
                || diskIdIsEmpty
                || pathWithOutDiskLetterIsEmpty
                || pathHashIsEmpty
                || nanoTimeStartAddToIndexIsEmpty
                || nanoTimeEndAddToIndexIsEmpty;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    private static boolean isDirListExistDataDataEmpty(NcDcIdxDirListToFileExist inFuncData){
        if( inFuncData == null ){
            return true;
        }
        boolean dirListIdIsEmpty = inFuncData.dirListID == -777;
        boolean diskIdIsEmpty = inFuncData.diskID == -777;
        boolean pathWithOutDiskLetterIsEmpty = inFuncData.pathWithOutDiskLetter == "";
        boolean pathHashIsEmpty = inFuncData.pathHash == -777;
        boolean nanoTimeStartAddToIndexIsEmpty = inFuncData.nanoTimeStartAddToIndex == -777;
        boolean nanoTimeEndAddToIndexIsEmpty = inFuncData.nanoTimeEndAddToIndex == -777;
        boolean hashIsTrue =  isDirListExistDataDataHashTrue(inFuncData);
        return dirListIdIsEmpty
                && diskIdIsEmpty
                && pathWithOutDiskLetterIsEmpty
                && pathHashIsEmpty
                && nanoTimeStartAddToIndexIsEmpty
                && nanoTimeEndAddToIndexIsEmpty
                && hashIsTrue;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    private static boolean isDirListExistDataDataHashTrue(NcDcIdxDirListToFileExist inFuncData){
        return inFuncData.recordHash == (
                ""
                + inFuncData.dirListID
                + inFuncData.diskID
                + inFuncData.pathWithOutDiskLetter
                + inFuncData.pathHash
                + inFuncData.nanoTimeStartAddToIndex
                + inFuncData.nanoTimeEndAddToIndex
                + inFuncData.recordTime).hashCode();
    }
    
}
