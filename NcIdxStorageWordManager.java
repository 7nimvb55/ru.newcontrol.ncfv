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
public class NcIdxStorageWordManager {

    /**
     *
     * @param typeWords
     * @param WordToStorage
     */
    public static void putInStorageWord(String typeWords, TreeMap<Long, NcDcIdxSubStringToOperationUse> WordToStorage){
        
        TreeMap<Long, File> listFilesInStorage = new TreeMap<Long, File>();
        TreeMap<Long, NcDcIdxStorageWordToFile> readedData = new TreeMap<Long, NcDcIdxStorageWordToFile>();
        TreeMap<Long, NcDcIdxStorageWordToFile> foundedData = new TreeMap<Long, NcDcIdxStorageWordToFile>();
        TreeMap<Long, NcDcIdxStorageWordToFile> readedDataFormLastFile = new TreeMap<Long, NcDcIdxStorageWordToFile>();
        boolean foundAndUpdated = false;
        boolean appendNewRecord = false;
        long lastRecordId = -1;
        File lastRecordFile = new File("notInitFile");
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> itemWord : WordToStorage.entrySet() ){
            String wordInHex = itemWord.getValue().hexSubString;
            String word = itemWord.getValue().strSubString;
            listFilesInStorage = getStorageWordExistFiles(typeWords, wordInHex, word);
            if( !listFilesInStorage.isEmpty() ){
                for(Map.Entry<Long, File> itemFile : listFilesInStorage.entrySet() ){
                    readedData.putAll(NcIdxStorageWordFileReader.ncReadFileContainedId(itemFile.getValue()));
                    foundedData.clear();
                    foundedData.putAll(searchRecordInStorageWord(readedData, wordInHex, word));
                    if( !foundedData.isEmpty() ){
                        foundedData.firstEntry().getValue().wordCount = foundedData.firstEntry().getValue().wordCount + 1;
                        readedData.put(foundedData.firstEntry().getKey(), foundedData.firstEntry().getValue());
                        int countOfWrited = NcIdxStorageWordFileWriter.ncUpdateData(itemFile.getValue(), readedData);
                        foundAndUpdated = countOfWrited == readedData.size();
                        NcAppHelper.outMessage("In Exist file: " + itemFile.getValue().getAbsolutePath());
                        NcAppHelper.outMessage("Update record for id: " + foundedData.firstEntry().getValue().wordId + " word: "
                                + word + ", wordInHex: " + wordInHex + ", inFileRecordId: "
                                + foundedData.firstEntry().getKey() + ", count words change to: "
                                + foundedData.firstEntry().getValue().wordCount);
                    }
                    if( !readedData.isEmpty() ){
                        lastRecordId = readedData.lastKey();
                        lastRecordFile = itemFile.getValue();
                        readedDataFormLastFile.clear();
                        readedDataFormLastFile.putAll(readedData);
                    }
                    readedData.clear();
                }
                if( !foundAndUpdated ){
                    long forNewRecordId = lastRecordId + 1;
                    String strFileNameForRecord = getStorageWordByIdFile(typeWords, wordInHex, word, forNewRecordId);
                    if( lastRecordFile.getAbsolutePath().equalsIgnoreCase(strFileNameForRecord) ){
                        long nextWordId = readedDataFormLastFile.lastEntry().getValue().wordId++;
                        NcDcIdxStorageWordToFile toRecordData = convertFormOperationToFileData(itemWord.getValue(), nextWordId);
                        readedDataFormLastFile.put(forNewRecordId, toRecordData);
                        int countOfWrited = NcIdxStorageWordFileWriter.ncUpdateData(lastRecordFile, readedDataFormLastFile);
                        appendNewRecord = countOfWrited == readedDataFormLastFile.size();
                    }
                    else{
                        long nextWordId = 0;
                        if( !readedDataFormLastFile.isEmpty() ){
                            nextWordId = readedDataFormLastFile.lastEntry().getValue().wordId++;
                        }
                        NcDcIdxStorageWordToFile toRecordData = convertFormOperationToFileData(itemWord.getValue(), nextWordId);
                        forNewRecordId = 0;
                        readedData.clear();
                        readedData.put(forNewRecordId, toRecordData);
                        File forRecord = new File(strFileNameForRecord);
                        int countOfWrited = NcIdxStorageWordFileWriter.ncUpdateData(forRecord, readedData);
                        appendNewRecord = countOfWrited == readedDataFormLastFile.size();
                    }
                }
                //code to append
            }
            else{
                if( lastRecordId == -1 ){
                    long forNewRecordId = lastRecordId + 1;
                    String strFileNameForRecord = getStorageWordByIdFile(typeWords, wordInHex, word, forNewRecordId);
                    lastRecordFile = new File(strFileNameForRecord);
                    readedDataFormLastFile.clear();
                    long nextWordId = 0;
                    NcDcIdxStorageWordToFile toRecordData = convertFormOperationToFileData(itemWord.getValue(), nextWordId);
                    readedDataFormLastFile.put(forNewRecordId, toRecordData);
                    int countOfWrited = NcIdxStorageWordFileWriter.ncUpdateData(lastRecordFile, readedDataFormLastFile);
                    appendNewRecord = countOfWrited == readedDataFormLastFile.size();
                }    
            }
            listFilesInStorage.clear();
        }        
    }

    /**
     *
     * @param inFuncReadedData
     * @param wordInHex
     * @param word
     * @return
     */
    public static TreeMap<Long, NcDcIdxStorageWordToFile> searchRecordInStorageWord(TreeMap<Long, NcDcIdxStorageWordToFile> inFuncReadedData, String wordInHex, String word){
        TreeMap<Long, NcDcIdxStorageWordToFile> toReturnFoundedData = new TreeMap<Long, NcDcIdxStorageWordToFile>();
        for(Map.Entry<Long, NcDcIdxStorageWordToFile> itemReadedData : inFuncReadedData.entrySet() ){
                boolean compareWordResult = word.hashCode() == itemReadedData.getValue().wordHash;
                boolean compareWordInHexResult = wordInHex.hashCode() == itemReadedData.getValue().wordInHexHash;
                if( compareWordResult && compareWordInHexResult ){
                    toReturnFoundedData.put(itemReadedData.getKey(), itemReadedData.getValue());
                    return toReturnFoundedData;
                }
            }
        return new TreeMap<Long, NcDcIdxStorageWordToFile>();
    }

    /**
     *
     * @param typeWords
     * @param inFuncWordInHex
     * @param inFuncWord
     * @return
     */
    public static TreeMap<Long, File> getStorageWordExistFiles(String typeWords, String inFuncWordInHex, String inFuncWord){
        switch (typeWords){
            case "NCLVLABC":
                return NcTypeOfWord.NCLVLABC.getStorageWordExistFileName(inFuncWordInHex, inFuncWord);
            case "NCLVLRABC":
                return NcTypeOfWord.NCLVLRABC.getStorageWordExistFileName(inFuncWordInHex, inFuncWord);
            case "NCLVLNUM":
                return NcTypeOfWord.NCLVLNUM.getStorageWordExistFileName(inFuncWordInHex, inFuncWord);
            case "NCLVLSYM":
                return NcTypeOfWord.NCLVLSYM.getStorageWordExistFileName(inFuncWordInHex, inFuncWord);
            case "NCLVLSPACE":
                return NcTypeOfWord.NCLVLSPACE.getStorageWordExistFileName(inFuncWordInHex, inFuncWord);
        }
        return new TreeMap<Long, File>();
    }

    /**
     *
     * @param typeWords
     * @param inFuncWordInHex
     * @param inFuncWord
     * @param inFuncId
     * @return
     */
    public static String getStorageWordByIdFile(String typeWords, String inFuncWordInHex, String inFuncWord, long inFuncId){
        switch (typeWords){
            case "NCLVLABC":
                return NcTypeOfWord.NCLVLABC.getStorageWordByIdFileName(inFuncWordInHex, inFuncWord, inFuncId);
            case "NCLVLRABC":
                return NcTypeOfWord.NCLVLRABC.getStorageWordByIdFileName(inFuncWordInHex, inFuncWord, inFuncId);
            case "NCLVLNUM":
                return NcTypeOfWord.NCLVLNUM.getStorageWordByIdFileName(inFuncWordInHex, inFuncWord, inFuncId);
            case "NCLVLSYM":
                return NcTypeOfWord.NCLVLSYM.getStorageWordByIdFileName(inFuncWordInHex, inFuncWord, inFuncId);
            case "NCLVLSPACE":
                return NcTypeOfWord.NCLVLSPACE.getStorageWordByIdFileName(inFuncWordInHex, inFuncWord, inFuncId);
        }
        return "canNotCreateName";
    }

    /**
     *
     * @param inFuncData
     * @param inFuncNextWordId
     * @return
     */
    public static NcDcIdxStorageWordToFile convertFormOperationToFileData(NcDcIdxSubStringToOperationUse inFuncData, long inFuncNextWordId){
        NcDcIdxStorageWordToFile toReturn = new NcDcIdxStorageWordToFile(
                inFuncNextWordId,
                NcIdxLongWordManager.isLongWord(inFuncData.strSubString),
                inFuncData.hexSubString,
                inFuncData.strSubString,
                1
        );
        return toReturn;
    }
}
