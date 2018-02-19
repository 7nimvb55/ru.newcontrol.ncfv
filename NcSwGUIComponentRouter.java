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
public class NcSwGUIComponentRouter {
    /**
     * 
     * @return 
     */
    protected static String pathMainFrame(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEnd(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SLINEEND.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndScrollPane(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SLINEEND.getStr()
                + NcStrGUIComponent.SJSCROLLPANE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndScrollPaneTreeShowStack(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SLINEEND.getStr()
                + NcStrGUIComponent.SJSCROLLPANE.getStr()
                + NcStrGUIComponent.SJTREE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelPageEndProgressBar(){
        String componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SPAGEEND.getStr()
            + NcStrGUIComponent.SJPROGRESSBAR.getStr();
        return componentPath;
    }
}
