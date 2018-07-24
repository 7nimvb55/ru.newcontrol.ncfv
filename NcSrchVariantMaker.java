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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSrchVariantMaker {
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchKeyWordInput#getIdDataForSplittedKeyWord(java.util.ArrayList) }
     * </ul>
     * @param strInFunc
     * @return
     */
    protected static TreeMap<Long, NcIdxSubStringVariant> getUpperLowerCaseVariant(ArrayList<String> strInFunc){
        TreeMap<Long, NcIdxSubStringVariant> strToRet = new TreeMap<Long, NcIdxSubStringVariant>();
        if( strInFunc.isEmpty() ){
            return strToRet;
        }
        long idx = 0;
        for(String itemStr : strInFunc ){
            String toLowerResultStr = itemStr.toLowerCase().toString();
            String toUpperResultStr = itemStr.toUpperCase().toString();
            NcIdxSubStringVariant toAddToReturn = new NcIdxSubStringVariant(toLowerResultStr, toUpperResultStr);
            strToRet.put(idx, toAddToReturn);
            idx++;
        }
        return strToRet;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchKeyWordInput#getIdDataForSplittedKeyWord(java.util.ArrayList) }
     * </ul>
     * @param toSearchABC
     */
    protected static ArrayList<String> getUpLowerCaseCombineKeyWord(TreeMap<Long, NcIdxSubStringVariant> toSearchABC){
        ArrayList<String> strWordsVar = new ArrayList<String>();
        
        for(Map.Entry<Long, NcIdxSubStringVariant> itemKeyWord : toSearchABC.entrySet()){
            String strLower = itemKeyWord.getValue().hexForLowerCase;
            String strUpper = itemKeyWord.getValue().hexForUpperCase;
            String toChange = "";
            
            strWordsVar.add(strLower);
            
            String[] strArrLower = strToArray(strLower);
            String[] strArrUpper = strToArray(strUpper);
            String[] strArrChange = new String[strArrUpper.length];
            strArrChange = Arrays.copyOf(strArrLower, strArrLower.length);
            int idx = 0;
            do{
                strArrChange = strArrChangeState(strArrChange, strArrLower, strArrUpper);
                toChange = arrToStr(strArrChange);
                strWordsVar.add(toChange);
                idx++;
            }
            while( !strUpper.equalsIgnoreCase(toChange) ); 
        }
        return strWordsVar;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchVariantMaker#getUpLowerCaseCombineKeyWord(java.util.TreeMap) }
     * </ul>
     * @param strArrChange
     * @param strArrDown
     * @param strArrUp
     * @return 
     */
    private static String[] strArrChangeState(String[] strArrChange, String[] strArrDown, String[] strArrUp){
        if( (strArrDown.length == strArrUp.length)
                && (strArrChange.length == strArrDown.length) ){
            boolean changeFlow = false;
            boolean changeNext = false;
            boolean changeFar  = false;
            int prevElSay = -1;
            int nextElSay = -1;
            for(int arrIdx = 0; arrIdx < strArrChange.length; arrIdx++){
                if(arrIdx == 0){
                    changeFlow = strIfUpToDownTrue(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                    if( changeFlow ){
                        prevElSay = arrIdx + 1;
                        changeFlow = false;
                    }
                    strArrChange[arrIdx] = strArrChangeElement(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                }
                if( arrIdx == prevElSay ){
                    changeNext = strIfUpToDownTrue(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                    if( changeNext ){
                        prevElSay = 0;
                        nextElSay = arrIdx + 1;
                        changeNext = false;
                    }
                    strArrChange[arrIdx] = strArrChangeElement(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                }
                if( arrIdx == nextElSay ){
                    changeFar = strIfUpToDownTrue(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                    if( changeFar ){
                        prevElSay = arrIdx + 1;
                        changeFar = false;
                    }
                    strArrChange[arrIdx] = strArrChangeElement(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                }
            }
        }
        return strArrChange;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchVariantMaker#getUpLowerCaseCombineKeyWord(java.util.TreeMap) }
     * </ul>
     * @param strChange
     * @param strDown
     * @param strUp
     * @return 
     */
    private static String strArrChangeElement(String strChange, String strDown, String strUp){
        if(strDown.equalsIgnoreCase(strChange)){
            return strUp;
        }
        return strDown;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchVariantMaker#strArrChangeState(java.lang.String[], java.lang.String[], java.lang.String[]) }
     * </ul>
     * @param strForCompare
     * @param strDown
     * @param strUp
     * @return 
     */
    private static boolean strIfUpToDownTrue(String strForCompare, String strDown, String strUp){
        if( strUp.equalsIgnoreCase(strForCompare) ){
            return true;
        }
        return false;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchVariantMaker#getUpLowerCaseCombineKeyWord(java.util.TreeMap) }
     * </ul>
     * @param inputArrStr
     * @return 
     */
    private static String arrToStr(String[] inputArrStr){
        String toReturn = "";
        for(int i = 0; i < inputArrStr.length ; i++){
            toReturn = toReturn + inputArrStr[i];
        }
        return toReturn;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSrchVariantMaker#getUpLowerCaseCombineKeyWord(java.util.TreeMap) }
     * </ul>
     * @param inputHexStr
     * @return 
     */
    private static String[] strToArray(String inputHexStr){
        int idxStart = 0;
        int idxEnd = idxStart + 4;
        int idx = 0;
        String[] strToRet = new String[inputHexStr.length() / 4];
        do{
            strToRet [idx] = inputHexStr.substring(idxStart, idxEnd);
            idxStart = idxStart + 4;
            idxEnd = idxStart + 4;
            idx++;
        }
        while( idxEnd <= inputHexStr.length() ); 
        return strToRet;        
    }
}
