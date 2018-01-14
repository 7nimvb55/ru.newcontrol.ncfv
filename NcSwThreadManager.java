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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwThreadManager {
     
    public static void setToViewSearchedResult(String strSrch){
        NcSwGUIComponentStatus guiComponents = NcSwingIndexManagerApp.getComponentStatus();
        //NcThProcGUICallbackInterface proxyInstGuiCb = getProxyInstanceGUICallback();
        guiComponents.ncTableModel = new NcSIMASearchResultTableModel(strSrch);
        guiComponents.ncTable.setModel(guiComponents.ncTableModel);
        guiComponents.centerPanel.repaint();
    }
    public static NcThProcGUICallbackInterface getProxyInstanceGUICallback(){
        NcSwGUIComponentStatus guiComponents = NcSwingIndexManagerApp.getComponentStatus();
        NcThProcGUICallback cbLoc = new NcThProcGUICallback(guiComponents);
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
