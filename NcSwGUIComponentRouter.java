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
    protected static String pathMainFramePanel(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineStart(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SLINESTART.getStr();
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
    protected static String pathMainFramePanelLineEndTabbedPane(){
        return pathMainFramePanelLineEnd()
            + NcStrGUIComponent.SJTABBEDPANE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneStack(){
        return pathMainFramePanelLineEndTabbedPane()
            + NcStrGUIComponent.SSTACK.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneWork(){
        return pathMainFramePanelLineEndTabbedPane()
            + NcStrGUIComponent.SWORK.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneOutput(){
        return pathMainFramePanelLineEndTabbedPane()
            + NcStrGUIComponent.SOUTPUT.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneStackScrollPane(){
        return pathMainFramePanelLineEndTabbedPaneStack()
                + NcStrGUIComponent.SJSCROLLPANE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneWorkScrollPane(){
        return pathMainFramePanelLineEndTabbedPaneWork()
                + NcStrGUIComponent.SJSCROLLPANE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneOutputScrollPane(){
        return pathMainFramePanelLineEndTabbedPaneOutput()
                + NcStrGUIComponent.SJSCROLLPANE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneStackScrollPaneTreeShowStack(){
        return pathMainFramePanelLineEndTabbedPaneStackScrollPane()
                + NcStrGUIComponent.SJTREE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneWorkScrollPaneTreeShowWork(){
        return pathMainFramePanelLineEndTabbedPaneWorkScrollPane()
                + NcStrGUIComponent.SJTREE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelLineEndTabbedPaneOutputScrollPaneTreeShowOutput(){
        return pathMainFramePanelLineEndTabbedPaneOutputScrollPane()
                + NcStrGUIComponent.SJTREE.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelPageStart(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SPAGESTART.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelPageStartTextFieldSearch(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SPAGESTART.getStr()
                + NcStrGUIComponent.STEXTFIELD.getStr()
                + NcStrGUIComponent.SSEARCH.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelPageStartButtonSearch(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SPAGESTART.getStr()
                + NcStrGUIComponent.SJBUTTON.getStr()
                + NcStrGUIComponent.SSEARCH.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelPageEnd(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SPAGEEND.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelPageEndButtonGetStack(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SPAGEEND.getStr()
                + NcStrGUIComponent.SJBUTTON.getStr()
                + NcStrGUIComponent.SSTACK.getStr();
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
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelCenter(){
        return NcStrGUIComponent.SMAIN.getStr()
                + NcStrGUIComponent.SJFRAME.getStr()
                + NcStrGUIComponent.SJPANEL.getStr()
                + NcStrGUIComponent.SCENTER.getStr();
    }
    /**
     * 
     * @return 
     */
    protected static String pathMainFramePanelCenterTable(){
        return NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SCENTER.getStr()
            + NcStrGUIComponent.SJTABLE.getStr();
    }
}
