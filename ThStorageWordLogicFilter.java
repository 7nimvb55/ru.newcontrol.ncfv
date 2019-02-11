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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicFilter {
    protected static void doFilterForIndexStorageWord(final ThStorageWordRule outerRuleStorageWord){
        //bus from FileListBusToNext throw NullPointerException
        ThIndexRule indexRule = outerRuleStorageWord.getIndexRule();
        ThIndexState indexState = indexRule.getIndexState();
        ThFileListRule ruleFileList = indexState.getRuleFileList();
        ThFileListState fileListState = ruleFileList.getFileListState();
        ThFileListBusToNext busJobForFileListToNext = fileListState.getBusJobForFileListToNext();
        ThStorageWordState storageWordState = outerRuleStorageWord.getStorageWordState();
        
        /**
         * funcReadedPath - 1506682974
         * funcNamePart - -589260798
         */
        do{
            while( !busJobForFileListToNext.isJobQueueEmpty() ){
                ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, ?>> jobForWrite = busJobForFileListToNext.getJobForWrite();
                resortInputedStructure(storageWordState, jobForWrite);

            }
        } while( ruleFileList.isRunnedFileListWorkBuild() );



    }
    private static void resortInputedStructure(
            final ThStorageWordState inputedStorageWordState,
            final ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, ?>> inputedStructure){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Path>> ouputStructure;
        ThStorageWordState valStorageWordState;
        try{
            valStorageWordState = inputedStorageWordState;
            if( valStorageWordState == null ){
                
            }
            //ouputStructure = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Path>>();
            for(Entry<UUID, ConcurrentHashMap<Integer, ?>> itemFromStructure : inputedStructure.entrySet()){
                UUID keyInputedData = (UUID) itemFromStructure.getKey();
                ConcurrentHashMap<Integer, Path> jobData = getJobData(itemFromStructure.getValue());
                //ouputStructure.put(keyInputedData, jobData);
                System.out.println("transfered UUID: " + keyInputedData.toString());
                transferDataToBusesWordLongWord(valStorageWordState, keyInputedData, jobData);
            }
            //return ouputStructure;
        } finally {
            ouputStructure = null;
            valStorageWordState = null;
        }
    }
    private static void transferDataToBusesWordLongWord(
        final ThStorageWordState inStorageWordState,
        UUID keyInProcessData,
        final ConcurrentHashMap<Integer, Path> forTransferData){
        String fVal;
        String sVal;
        try{
            /**
             * funcReadedPath - 1506682974
             * funcNamePart - -589260798
             */
            fVal = (String) forTransferData.get(1506682974).toString();
            sVal = (String) forTransferData.get(-589260798).toString();
            doWordForIndex(inStorageWordState, keyInProcessData, 
                    fVal,  
                    sVal);
        } finally {
            fVal = null;
            sVal = null;
        }
    }
    /**
     * @todo convert Path to String, extract name parts and more... do it
     * @param inputedData
     * @return 
     */
    private static ConcurrentHashMap<Integer, Path> getJobData(
            final ConcurrentHashMap<Integer, ?> inputedData){
        Path funcReadedPath;
        Path funcNamePart;
        ConcurrentHashMap<Integer, Path> forDataOutput;
        try{
            Path getReadedPath = (Path) inputedData.get(1506682974);
            forDataOutput = new ConcurrentHashMap<Integer, Path>();
            if( getReadedPath == null ){
                throw new NullPointerException(ThStorageWordLogicFilter.class.getCanonicalName()
                    + " read from bus null key value");
            }
            funcReadedPath = getReadedPath;
            forDataOutput.put(1506682974, funcReadedPath);
            Path getNamePart = (Path) inputedData.get(-589260798);
            if( getNamePart == null ){
                throw new NullPointerException(ThStorageWordLogicFilter.class.getCanonicalName()
                    + " read from bus null key value");
            }
            funcNamePart = getNamePart;
            forDataOutput.put(-589260798, funcNamePart);
            System.out.println("transfered funcReadedPath:  " + funcReadedPath.toString()
                    + " funcNamePart: " + funcNamePart.toString());
            return forDataOutput;
        } finally {
            funcReadedPath = null;
            funcNamePart = null;
            forDataOutput = null;
        }
        
    }
    private static Integer getWordCode(int codePoint){
        int forReturnType = 0;
        int unicodeBlockToString = Character.UnicodeBlock.of(codePoint).hashCode();
        forReturnType = unicodeBlockToString;
        boolean alphabetic = Character.isAlphabetic(codePoint);
        forReturnType = alphabetic ? forReturnType + 362898540 : forReturnType;
        boolean bmpCodePoint = Character.isBmpCodePoint(codePoint);
        forReturnType = bmpCodePoint ? forReturnType + 33656253 : forReturnType;
        boolean defined = Character.isDefined(codePoint);
        forReturnType = defined ? forReturnType + 326275242 : forReturnType;
        boolean digit = Character.isDigit(codePoint);
        forReturnType = digit ? forReturnType + 1510153202 : forReturnType;
        boolean letter = Character.isLetter(codePoint);
        forReturnType = letter ? forReturnType + 655627589 : forReturnType;
        boolean spaceChar = Character.isSpaceChar(codePoint);
        forReturnType = spaceChar ? forReturnType + 344234941 : forReturnType;
        
        return (int) forReturnType;
    }
    /**
     * 
     * @param recordId
     * @param storagePath
     * @param inputedNamePartPath 
     */
    private static void doWordForIndex(
            final ThStorageWordState inputedStorageWordState,
            final UUID recordId, 
            final String storagePath, 
            final String inputedNamePartPath){
        ConcurrentSkipListMap<UUID, TdataWord> forReturnLongWord;
        ConcurrentSkipListMap<UUID, TdataWord> forReturnWord;
        char[] toCharArray;
        int idexChar;
        int prevWordCodeType;
        String word;
        String heximalWord;
        String toHexString;
        int startPos;
        int lengthWord;
        TdataWord forLastAddData;
        TdataWord forAddData;
        try{
            //forReturnLongWord = new ConcurrentSkipListMap<UUID, TdataWord>();
            //forReturnWord = new ConcurrentSkipListMap<UUID, TdataWord>();
            toCharArray = inputedNamePartPath.toCharArray();
            idexChar = 0;
            prevWordCodeType = (int) ThStorageWordLogicFilter.getWordCode(inputedNamePartPath.codePointAt(idexChar));
            word = "";
            heximalWord = "";
            startPos = 0;
            lengthWord = 0;
            for(char item : toCharArray){
                int codePointAt = inputedNamePartPath.codePointAt(idexChar);
                int wordCodeType = (int) ThStorageWordLogicFilter.getWordCode(codePointAt);
                toHexString = Integer.toHexString(codePointAt);
                if( toHexString.length() == 2 ){
                    toHexString = "00" + toHexString;
                }
                if( (prevWordCodeType != wordCodeType) ){
                    lengthWord = word.length();
                    forAddData= new TdataWord(recordId, storagePath, word, prevWordCodeType, heximalWord, startPos, lengthWord);
                    /**
                     * put job to Bus by type ThWordState
                     */
                    
                    
                    if( lengthWord > 25){
                        //AppConstants.INDEX_DATA_TRANSFER_CODE_LONG_WORD
                        //forReturnLongWord.put(UUID.randomUUID(), forAddData);
                        ThStorageWordBusOutput busJobForLongWordWrite = inputedStorageWordState.getBusJobForLongWordWrite();
                        ArrayBlockingQueue<TdataWord> busForTypeLongWord = busJobForLongWordWrite.getBusForTypeWord(prevWordCodeType);
                        busForTypeLongWord.add(forAddData);
                    } else {
                        //AppConstants.INDEX_DATA_TRANSFER_CODE_WORD;
                        //forReturnWord.put(UUID.randomUUID(), forAddData);
                        ThStorageWordBusOutput busJobForWordWrite = inputedStorageWordState.getBusJobForWordWrite();
                        ArrayBlockingQueue<TdataWord> busForTypeWord = busJobForWordWrite.getBusForTypeWord(prevWordCodeType);
                        busForTypeWord.add(forAddData);
                    }
                    word = null;
                    heximalWord = null;
                    startPos = idexChar;
                }
                word = word + item;
                heximalWord = heximalWord + toHexString;
                idexChar++;
                prevWordCodeType = wordCodeType;
            }
            lengthWord = word.length();
            forLastAddData = new TdataWord(recordId, storagePath, word, prevWordCodeType, heximalWord, startPos, lengthWord);
            if( lengthWord > 25){
                //AppConstants.INDEX_DATA_TRANSFER_CODE_LONG_WORD
                //forReturnLongWord.put(UUID.randomUUID(), forLastAddData);
                ThStorageWordBusOutput busJobForLongWordWrite = inputedStorageWordState.getBusJobForLongWordWrite();
                ArrayBlockingQueue<TdataWord> busForTypeLongWord = busJobForLongWordWrite.getBusForTypeWord(prevWordCodeType);
                busForTypeLongWord.add(forLastAddData);
                int size = busForTypeLongWord.size();
                System.out.println(">    >    >    >    >    >    >    >    >    >    >LongWord bus for typeWord " 
                        + prevWordCodeType + " size " + size);
                
            } else {
                //AppConstants.INDEX_DATA_TRANSFER_CODE_WORD;
                //forReturnWord.put(UUID.randomUUID(), forLastAddData);
                ThStorageWordBusOutput busJobForWordWrite = inputedStorageWordState.getBusJobForWordWrite();
                ArrayBlockingQueue<TdataWord> busForTypeWord = busJobForWordWrite.getBusForTypeWord(prevWordCodeType);
                busForTypeWord.add(forLastAddData);
                int size = busForTypeWord.size();
                System.out.println(">    >    >    >    >    >Word bus for typeWord " 
                        + prevWordCodeType + " size " + size);
            }
            
            //return forReturnFinishedPut(forReturnWord, forReturnLongWord);
        } finally {
            forReturnLongWord = null;
            forReturnWord = null;
            toCharArray = null;
            word = null;
            heximalWord = null;
            toHexString = null;
            forLastAddData = null;
            forAddData = null;
        }
    }
}
