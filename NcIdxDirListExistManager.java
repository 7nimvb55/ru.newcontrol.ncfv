/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcIdxDirListExistManager {

    /**
     *
     * @param toWriteData
     * @param dirListID
     * @return
     */
    public static long putToDirListExistStart(NcDcIdxDirListToFileExist toWriteData, long dirListID){
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
     *
     * @param toWriteData
     * @param dirListID
     * @return
     */
    public static long putToDirListExistStop(NcDcIdxDirListToFileExist toWriteData, long dirListID){
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
    public static boolean isDirListExistDataWrong(NcDcIdxDirListToFileExist inFuncData){
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
    public static boolean isDirListExistDataHasEmptyFiled(NcDcIdxDirListToFileExist inFuncData){
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
    public static boolean isDirListExistDataDataEmpty(NcDcIdxDirListToFileExist inFuncData){
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
    public static boolean isDirListExistDataDataHashTrue(NcDcIdxDirListToFileExist inFuncData){
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
