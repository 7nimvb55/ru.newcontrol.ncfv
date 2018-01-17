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
public enum NcStrGUIComponent {
    SMAIN("MAIN"),
    SMODAL("MODAL"),
    SENVIRONMENT("ENVIRONMENT"),
    SPROPERTIES("PROPERTIES"),
    SLOGVIEW("LOGVIEW"),
    
    SCENTER("CENTER"),
    SPAGESTART("PAGESTART"),
    SPAGEEND("PAGEEND"),
    SLINESTART("LINESTART"),
    SLINEEND("LINEEND"),
    
    SJCOMPONENT("JCOMPONENT"),
    SJSCROLLPANE("JSCROLLPANE"),
    SJFRAME("JFRAME"),
    SJDIALOG("JDIALOG"),
    
    SJMENUBAR("JMENUBAR"),
    SJMENU("JMENU"),
    SJMENUITEM("JMENUITEM"),
    
    SJPANEL("JPANEL"),
    SJTREE("JTREE"),
    SJTABLE("JTABLE"),
    
    STEXTFIELD("TEXTFIELD"),
    SJLABEL("JLABEL"),
    SJBUTTON("JBUTTON"),
    
    SSEARCH("SEARCH"),
    SMANAGE("MANAGE"),
    SDIRECTORY("DIRECTORY");
    
    private String strMsg;
    NcStrGUIComponent(String strMsg){
        this.strMsg = strMsg;
    }
    public String getStr(){
        return strMsg;
    }
}
