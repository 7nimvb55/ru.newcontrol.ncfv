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

/**
 *
 * @author wladimirowichbiaran
 */
public class NcLogColorizer {
    public static String getHtmlStr(String strInput){
        boolean isHTML = false;
        if( strInput.contains(NcStrLogMsgField.ERROR.getStr()) ){
            strInput = "<b color=red>" + strInput + "</b>" ;
            isHTML = true;
        }
        if( strInput.contains("[LA_GUI") ){
            strInput = "<i color=green>" + strInput + "</i>";
            isHTML = true;
        }
        if( strInput.contains("[LA_CFG") ){
            strInput = "<i color=blue>" + strInput + "</i>";
            isHTML = true;
        }
        if(isHTML){
            strInput = "<html>" + strInput + "</html>";
            isHTML = false;
        }
        return strInput;
    }
}
