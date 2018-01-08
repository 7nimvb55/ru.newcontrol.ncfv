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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Администратор
 */
public enum NcPathToArrListStr {
    
    /**
     *
     */
    NCLVLABC("[^a-zA-Z]"),

    /**
     *
     */
    NCLVLRABC("[^а-яА-Я]"),

    /**
     *
     */
    NCLVLNUM("[^0-9]"),

    /**
     *
     */
    NCLVLSYM("[0-9a-zA-Zа-яА-Я ]"),

    /**
     *
     */
    NCLVLSPACE("[^ ]");

    
    private String filtername;
    
    NcPathToArrListStr(String filtername){
        this.filtername = filtername;
    }
    
    /**
     *
     * @return
     */
    public String getName(){
        return filtername;
    }
/**
 * Method for split String (for example File Path) to subStrings, filtered by 
 * regular expression and concated before return
 * @param fstrti - splitted String
 * @return
 */    
    public String retStr(String fstrti){
        String fstro = "";
        for (String istr : fstrti.split(getName())){
           fstro += istr;
        } 
        return fstro;
    }
/**
 * Method for split String (for example File Path) to subStrings, filtered by 
 * regular expression and return in ArrayList format, with out for Nulled
 * subStrings
 * @param fstrti
 * @return 
 */    
    public ArrayList<String> retArrListStr(String fstrti){
        String[] tmpStr = fstrti.split(getName());
        ArrayList<String> fstro = new ArrayList<String>();
        for (String i : tmpStr){
            if(i.length() > 0){
                fstro.add(i);
            }
        }
        return fstro;
    }
    /**
     * Method for search subString in Source String (for example File Path) and
     * detect positions of subStrings in the Source String and his lengs, append
     * detected information into result concated string, released for development
     * @param strArr
     * @param strPath
     * @param fileID
     * @return 
     */    
    public static TreeMap<Long, NcDcIdxSubStringToOperationUse> getStructureToRecord(
            ArrayList<String> strArr,
            String strPath,
            long fileID){
        TreeMap<Long, NcDcIdxSubStringToOperationUse> codeStr = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        int i = 0;
        int[] inResultIndexOf = new int[i + 1];
        int[] inPrevResultIndexOf = new int[0];
        String[] inResultStr = new String[i + 1];
        String[] inOldResultStr = new String[0];
        

        int eqI = 0;
        int IdxPrev = 0;
        int StartSearchPos = 0;
        int CurrentSearchedIndex = 0;
        int ComparedIdx = 0;
        /**
         * Search in ArrayList subString and his position in Source String
         */        
        for(String subStrfArr : strArr){
            
            inPrevResultIndexOf = Arrays.copyOf(inResultIndexOf,inResultIndexOf.length);
            inOldResultStr = Arrays.copyOf(inResultStr,inResultStr.length);
        /**
         * subString compare with early processed strings (oldStr), if that founded than, get
         * index in two list and save him to (ComparedIdx), also flag (eqI) incremented
         * eqI - count of early used subStrings
         */            
            for(String oldStr : inOldResultStr){
                
                if(subStrfArr.equals(oldStr)){
                    eqI++;
                    ComparedIdx = IdxPrev;
                }
                IdxPrev++;
            }
            /**
             * If count of old used subString not zero, than get Index of last used subStrings
             * and increment him for found next positions of subString in source string
             */
            if(eqI > 0){
                StartSearchPos = inPrevResultIndexOf[ComparedIdx];
                if (strPath.length() > StartSearchPos){
                    StartSearchPos++;
                }
            }
            
            CurrentSearchedIndex = strPath.indexOf(subStrfArr, StartSearchPos);
            /**
             * Append to returned string information about as founded subStrings in format
             * subString{position in sourceString : subString.length()}
             */            
            //codeStr = codeStr + subStrfArr + "{" + CurrentSearchedIndex + ":" + subStrfArr.length() + "}";
            codeStr.put(
                (long) i,
                new NcDcIdxSubStringToOperationUse(
                    (long) i,
                    fileID,
                    subStrfArr,
                    toStrUTFinHEX(subStrfArr),
                    CurrentSearchedIndex,
                    subStrfArr.length()
                )
            );
            /**
             * Add to Array index of found subString, method (Array.copyof) changed him size
             */            
            
            inResultIndexOf = new int[i + 1];
            for (int tmpcpi = 0; tmpcpi < inPrevResultIndexOf.length; tmpcpi++){
                inResultIndexOf[tmpcpi] = inPrevResultIndexOf[tmpcpi];
            }
           
            inResultIndexOf[i] = CurrentSearchedIndex;
            /**
             * Add this subStrings to Array of used subStrings
             */            
            
            inResultStr = new String[i + 1];
            
            for (int tmpcpi = 0; tmpcpi < inOldResultStr.length; tmpcpi++){
                inResultStr[tmpcpi] = inOldResultStr[tmpcpi];
            }
            inResultStr[i] = subStrfArr;
            /**
             * Changed flags and counters for next iteration
             */            
            i++;
            StartSearchPos = 0;
            IdxPrev = 0;
            eqI = 0;
            CurrentSearchedIndex = 0;
            ComparedIdx = 0;
        }
        return codeStr;
    }
/**
 * Part of technological process of conversion string to his Heximal vision
 * @param toEncodeStr
 * @return 
 */    
    public static String toStrUTFinHEX(String toEncodeStr){
        byte[] utfBytes = strEncodeUTF8(toEncodeStr);
        String afterRetStr = toHex(utfBytes);
        afterRetStr = cleanResults(afterRetStr, toEncodeStr);
        return afterRetStr;
    }
/**
 * Method for correct results of conversion Strings to UTF8 and return his Hex
 * Some string after Java autoappend Buffer size contained some Nulls in end of
 * strings
 * 
 * Symbols in standart ASCII converted in simple one byte length
 * format for example this class convert "ZZZ" in UTF-8 with strEncodeUTF8() and
 * return it by toHex method, his result is: "5A5A5A" in UTF-8 notation usely
 * "\u005A\u005A\u005A", for this project need simple 005A005A005A
 * 
 * Also, for example, symbols in: :\.-«». returned: 3A5C2E2DC2ABC2BB2E
 * Length source string 7 bytes x 4 (UTF-8) = 28 theoretical, in prctics with
 * out cleanResults() method, result length is 9 bytes
 *
 * cleanResults() works in something of algoritm parts
 *
 * Part, of mixed bytes, where algoritm compare integer value of symbols in
 * string and if his values < 256, append before bytes '00' to return in
 * result string see example 003A005C002E002DC2ABC2BB002E
 * 
 * Part, compare Source String length x 4 with result string length and take
 * form start string index to index srcLength x 4
 * 
 * @param inBytes
 * @param srcStr
 * @return 
 */    
    private static String cleanResults(String inBytes, String srcStr){
        int srcStrLength = srcStr.length();
        String tmpStrToNullAdd = new String(inBytes);
        String backToOper = "";
/**
 * Part, of mixed bytes, where algoritm compare integer value of symbols in
 * string and if his values < 256, append before bytes '00' to return in
 * result string see example 003A005C002E002DC2ABC2BB002E
 */
        if (srcStrLength*4 != inBytes.length()){
            int di = 0;
            for(int i = 0 ; i < srcStrLength ; i++ ){
                if(srcStr.codePointAt(i) < 256){
                    backToOper = new String(backToOper + "00" + tmpStrToNullAdd.charAt(di)+tmpStrToNullAdd.charAt(di+1));
                    di++;
                }
                else{
                    backToOper = new String(backToOper + tmpStrToNullAdd.charAt(di)+tmpStrToNullAdd.charAt(di+1)
                            + tmpStrToNullAdd.charAt(di+2)+tmpStrToNullAdd.charAt(di+3));
                    di++;
                    di++;
                    di++;
                }
                di++;
            }
            inBytes = backToOper;
        }
/**
 * Part, compare Source String length x 4 with result string length and take
 * form start string index to index srcLength x 4
 */        
        if(inBytes.length() > srcStrLength * 4){
            inBytes = inBytes.substring(0, srcStrLength*4);
        }
        return inBytes;
    }
/**
 * Method for Encode source string, return byte Array, this returned Array
 * contained some Nulls after needed bytes in
 * this source (https://metanit.com/java/tutorial/7.3.php) writed about 
 * Java autoappend Buffer size
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
/**
 * Convert String in byte Array format to Heximal format
 * code founded in one of internet sources
 * @param bytes
 * @return 
 */    
    private static String toHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
/**
 * Method compare (length of source string) x 4 and length of string in his
 * HEXimal view
 * @param inBytes
 * @param tmpInBytes
 * @return 
 */    
    public static boolean checkStrUtfHex(String inBytes, String tmpInBytes){
        if(inBytes.length() < tmpInBytes.length() * 4){
            return false;
        }
        return true;
    }    
    
/**
 * Method for return extention, extention is all symbols after last dot in the Path
 * 
 * @param ncFile
 * @return String of file.Extention or "" Nulled string if it is a Directory or File has not Extention
 */
    public static String getExtention(File ncFile){
        String outExt = "";
        if(ncFile.isFile()){
            String inStrPath = ncFile.getAbsolutePath();
            String[] Ext = inStrPath.split("\\.");
            if( Ext.length != 1 ){   
                for(String strExt : Ext){
                    outExt = strExt;
                }
            }
            else{
                return "";
            }
        }
        else{
            return "";
        }
        return outExt;
    }
    
    
/**
 * Method for search subString in Source String (for example File Path) and
 * detect positions of subStrings in the Source String and his lengs, append
 * detected information into result concated string, released for development
 * @param strArr
 * @param strPath
 * @return 
 */    
    public static String getResultStr(ArrayList<String> strArr, String strPath){
        String codeStr = "";
        int i = 0;
        int[] inResultIndexOf = new int[i + 1];
        int[] inPrevResultIndexOf = new int[0];
        String[] inResultStr = new String[i + 1];
        String[] inOldResultStr = new String[0];
        

        int eqI = 0;
        int IdxPrev = 0;
        int StartSearchPos = 0;
        int CurrentSearchedIndex = 0;
        int ComparedIdx = 0;
/**
 * Search in ArrayList subString and his position in Source String
 */        
        for(String subStrfArr : strArr){
            
            inPrevResultIndexOf = Arrays.copyOf(inResultIndexOf,inResultIndexOf.length);
            inOldResultStr = Arrays.copyOf(inResultStr,inResultStr.length);
/**
 * subString compare with early processed strings (oldStr), if that founded than, get
 * index in two list and save him to (ComparedIdx), also flag (eqI) incremented
 * eqI - count of early used subStrings
 */            
            for(String oldStr : inOldResultStr){
                
                if(subStrfArr.equals(oldStr)){
                    eqI++;
                    ComparedIdx = IdxPrev;
                }
                IdxPrev++;
            }
/**
 * If count of old used subString not zero, than get Index of last used subStrings
 * and increment him for found next positions of subString in source string
 */
            if(eqI > 0){
                StartSearchPos = inPrevResultIndexOf[ComparedIdx];
                if (strPath.length() > StartSearchPos){
                    StartSearchPos++;
                }
            }
            
            CurrentSearchedIndex = strPath.indexOf(subStrfArr,StartSearchPos);
/**
 * Append to returned string information about as founded subStrings in format
 * subString{position in sourceString : subString.length()}
 */            
            codeStr = codeStr + subStrfArr + "{" + CurrentSearchedIndex + ":" + subStrfArr.length() + "}";
/**
 * Add to Array index of found subString, method (Array.copyof) changed him size
 */            
            
            inResultIndexOf = new int[i + 1];
            for (int tmpcpi = 0; tmpcpi < inPrevResultIndexOf.length; tmpcpi++){
                inResultIndexOf[tmpcpi] = inPrevResultIndexOf[tmpcpi];
            }
           
            inResultIndexOf[i] = CurrentSearchedIndex;
/**
 * Add this subStrings to Array of used subStrings
 */            
            
            inResultStr = new String[i + 1];
            
            for (int tmpcpi = 0; tmpcpi < inOldResultStr.length; tmpcpi++){
                inResultStr[tmpcpi] = inOldResultStr[tmpcpi];
            }
            inResultStr[i] = subStrfArr;
/**
 * Changed flags and counters for next iteration
 */            
            i++;
            StartSearchPos = 0;
            IdxPrev = 0;
            eqI = 0;
            CurrentSearchedIndex = 0;
            ComparedIdx = 0;
        }
        return codeStr;
    }
    
/**
 * Method copied from some (https://stackoverflow.com/a/229022) Internet resurse
 * and save for example
 * @param strToDecode
 * @return 
 */    
    private static String strEnDecodeUTF8(String strToDecode){
                 // Create the encoder and decoder for ISO-8859-1
        byte[] utfbytes = {};
        String decodeStr = "";
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();
        try {
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(strToDecode));
            CharBuffer cbuf = decoder.decode(bbuf);
            decodeStr = cbuf.toString();
        } catch (CharacterCodingException e) {
            return null;
        }
        return decodeStr;
    }
/**
 * Method from early stady of development, deleted Nulls from HEXimal
 * This method deleted all nulls in end of Hex String, result string is damaged
 * after use this method, not used, algoritm need for Super Developer and history
 * @param afterRetStr
 * @param SrcLength
 * @return 
 */    
    private static String ShrinkNulls(String afterRetStr, int SrcLength){
        if(afterRetStr.length() > 2){
            int chi = afterRetStr.length() - 1;
            char ch = afterRetStr.charAt(chi);
            if(ch == '0'){
                while((ch == '0') && (chi > 0)){
                    chi--;
                    ch = afterRetStr.charAt(chi);
                }
            }
            if((chi % 2) != 0){
                chi++;
            }
            afterRetStr = afterRetStr.substring(0,chi);
            if(afterRetStr.length() < SrcLength * 2){
                while((SrcLength - afterRetStr.length()) != 0){
                    afterRetStr += "0";
                }
            }
        }
        return afterRetStr;
    }
}
