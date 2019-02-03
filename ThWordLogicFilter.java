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
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicFilter {
    protected static ArrayList<String> getFilterForSplit(){
        ArrayList<String> forReturnList = new ArrayList<String>();
        forReturnList.add(AppIndexFilterConstants.NCLVLABC);
        forReturnList.add(AppIndexFilterConstants.NCLVLRABC);
        forReturnList.add(AppIndexFilterConstants.NCLVLNUM);
        forReturnList.add(AppIndexFilterConstants.NCLVLSYM);
        forReturnList.add(AppIndexFilterConstants.NCLVLSPACE);
        return forReturnList;
    }
    /**
     * for tests in strings classifity
     * @param inputedPath 
     */
    protected static void processFilterInputedString(String inputedPath){
        
        char[] toCharArray = inputedPath.toCharArray();
        int idexChar = 0;
        int prevIntType = Character.getType(inputedPath.codePointAt(idexChar));
        int prevWordCodeType = (int) ThWordLogicFilter.getWordCode(inputedPath.codePointAt(idexChar));
        String prevNameTypeShort = "";
        String prevNameType = "";
        String word = "";
        String heximalWord = "";
        int startPos = 0;
        int lengthWord = 0;
        for(char item : toCharArray){
            Character valueOf = Character.valueOf(item);
            //int intItem = item;
            int codePointAt = inputedPath.codePointAt(idexChar);
            int intType = Character.getType(codePointAt);
            int wordCodeType = (int) ThWordLogicFilter.getWordCode(codePointAt);
            String nameType = Character.getName(codePointAt);
            String nameTypeShort = Character.UnicodeBlock.of(codePointAt).toString();
            String nameTypeScriptShort = Character.UnicodeScript.of(codePointAt).toString();
            String toHexString = Integer.toHexString(codePointAt);
            if( toHexString.length() == 2 ){
                toHexString = "00" + toHexString;
            }
            if( (prevWordCodeType != wordCodeType) ){
                lengthWord = word.length();
                System.out.println(word + " - startPos - " + startPos + " - lengthWord - " + lengthWord 
                        + " - prevNameTypeShort - " + prevNameTypeShort
                        + " -|- prevWordCodeType - " + prevWordCodeType);
                System.out.println(heximalWord 
                        + " - heximalWord.length() - " 
                        + heximalWord.length() + " ("
                        + (heximalWord.length()/4) + ") - prevNameType - " 
                        + prevNameType + " - prevIntType " + prevIntType);
                System.out.println("--- --- --- --- ---");
                word = "";
                heximalWord = "";
                startPos = idexChar;
                
                
            }
            word = word + item;
            heximalWord = heximalWord + toHexString;
            idexChar++;
            prevIntType = intType;
            prevNameType = nameType;
            prevWordCodeType = wordCodeType;
            prevNameTypeShort = nameTypeShort;
        }
        lengthWord = word.length();
        System.out.println(word + " - startPos - " + startPos + " - lengthWord - " + lengthWord 
                + " - prevNameTypeShort - " + prevNameTypeShort
                + " -|- prevWordCodeType - " + prevWordCodeType);
        System.out.println(heximalWord 
                + " - heximalWord.length() - " 
                + heximalWord.length() + " - prevNameType - " 
                + prevNameType + " - prevIntType " + prevIntType);
        System.out.println("--- --- --- --- ---");
        
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
     * private static String getWordCode(int codePoint){
        String forReturnType = "";
        String unicodeBlockToString = Character.UnicodeBlock.of(codePoint).toString();
        forReturnType = unicodeBlockToString;
        boolean alphabetic = Character.isAlphabetic(codePoint);
        forReturnType = alphabetic ? forReturnType + "_" + "alphabetic".toUpperCase() : forReturnType;
        boolean bmpCodePoint = Character.isBmpCodePoint(codePoint);
        forReturnType = bmpCodePoint ? forReturnType + "_" + "bmpCodePoint".toUpperCase() : forReturnType;
        boolean defined = Character.isDefined(codePoint);
        forReturnType = defined ? forReturnType + "_" + "defined".toUpperCase() : forReturnType;
        boolean digit = Character.isDigit(codePoint);
        forReturnType = digit ? forReturnType + "_" + "digit".toUpperCase() : forReturnType;
        boolean letter = Character.isLetter(codePoint);
        forReturnType = letter ? forReturnType + "_" + "letter".toUpperCase() : forReturnType;
        boolean spaceChar = Character.isSpaceChar(codePoint);
        forReturnType = spaceChar ? forReturnType + "_" + "spaceChar".toUpperCase() : forReturnType;
        String strReturn = new String(forReturnType.toCharArray());
        return strReturn;
    }
     * @param strToEncode
     * @return 
     */
    private static byte[] strEncodeUTF8(String strToEncode){
        // Create the encoder and decoder for ISO-8859-1
        ByteBuffer utfbytes;
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();
        try {
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(strToEncode));
            utfbytes = bbuf;
        } catch (CharacterCodingException e) {
            return null;
        }
        return utfbytes.array();
    }
    protected static ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>  doWordForIndex(final UUID recordId, 
            final String storagePath, 
            final String inputedPath){
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
            forReturnLongWord = new ConcurrentSkipListMap<UUID, TdataWord>();
            forReturnWord = new ConcurrentSkipListMap<UUID, TdataWord>();
            toCharArray = inputedPath.toCharArray();
            idexChar = 0;
            prevWordCodeType = (int) ThWordLogicFilter.getWordCode(inputedPath.codePointAt(idexChar));
            word = "";
            heximalWord = "";
            startPos = 0;
            lengthWord = 0;
            for(char item : toCharArray){
                int codePointAt = inputedPath.codePointAt(idexChar);
                int wordCodeType = (int) ThWordLogicFilter.getWordCode(codePointAt);
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
                        forReturnLongWord.put(UUID.randomUUID(), forAddData);
                    } else {
                        //AppConstants.INDEX_DATA_TRANSFER_CODE_WORD;
                        forReturnWord.put(UUID.randomUUID(), forAddData);
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
                forReturnLongWord.put(UUID.randomUUID(), forLastAddData);
            } else {
                //AppConstants.INDEX_DATA_TRANSFER_CODE_WORD;
                forReturnWord.put(UUID.randomUUID(), forLastAddData);
            }
            return forReturnFinishedPut(forReturnWord, forReturnLongWord);
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
    private static ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> forReturnFinishedPut(
            final ConcurrentSkipListMap<UUID, TdataWord> toWordJobData,
            final ConcurrentSkipListMap<UUID, TdataWord> toLongWordJobData
    ){
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> doubleListOfWord;
        try{
            doubleListOfWord = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
            doubleListOfWord.put(AppConstants.INDEX_DATA_TRANSFER_CODE_WORD, toWordJobData);
            doubleListOfWord.put(AppConstants.INDEX_DATA_TRANSFER_CODE_LONG_WORD, toLongWordJobData);
            return doubleListOfWord;
        } finally {
            doubleListOfWord = null;
        }
        
    }
    
}
