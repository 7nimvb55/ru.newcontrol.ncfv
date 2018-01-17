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

import java.lang.reflect.Proxy;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwThreadManager {
     
    public static void setToViewSearchedResult(NcSwGUIComponentStatus lComp, String strSrch){
        String componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SCENTER.getStr();
        JPanel centerPanel =
            (JPanel) lComp.getComponentByPath(componentPath);
        
        
        //NcThProcGUICallbackInterface proxyInstGuiCb = getProxyInstanceGUICallback();
        TableModel locNewTableModel = new NcSIMASearchResultTableModel(strSrch);
        
        componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SCENTER.getStr()
            + NcStrGUIComponent.SJTABLE.getStr();
        JTable guiTable = (JTable) lComp.getComponentByPath(componentPath);
        
        guiTable.setModel(locNewTableModel);
        centerPanel.repaint();
    }
    public static NcThProcGUICallbackInterface getProxyInstanceGUICallback(){
        
        NcThProcGUICallback cbLoc = new NcThProcGUICallback();
        NcThProcInvocationHandler ncInvHandler = 
            new NcThProcInvocationHandler(cbLoc);
        NcThProcGUICallbackInterface proxyInstGui = (NcThProcGUICallbackInterface)
        Proxy.newProxyInstance(
        NcThProcGUICallbackInterface.class.getClassLoader(),
        new Class[]{NcThProcGUICallbackInterface.class},
        ncInvHandler);
        return proxyInstGui;
    }
}
