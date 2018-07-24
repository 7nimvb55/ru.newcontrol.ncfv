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

import java.awt.Component;
import java.util.TreeMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwGUIComponentStatus {
    
    private TreeMap<Integer, Component> modalLogView;
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getDialogLogViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#createGui() }
     * </ul>
     */
    protected NcSwGUIComponentStatus(){
        modalLogView = new TreeMap<Integer, Component>();
    }
    /**
     * Not used
     * @return 
     */
    protected TreeMap<Integer, Component> getComponentsList(){
        return modalLogView;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMenuItems#getEnvironmentViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwMenuItems#getPropertiesViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getDialogLogViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getButtonUpdate(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwThreadManager#setToViewSearchedResult(ru.newcontrol.ncfv.NcSwGUIComponentStatus, java.lang.String) }
     * </ul>
     * @param typeToGet
     * @return 
     */
    protected Component getComponentByPath(String typeToGet){
        return modalLogView.get(typeToGet.hashCode());
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getDialogLogViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getPanelCenter(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getScrolledTree(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelCenter#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineStart#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageStart#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#createGui() }
     * </ul>
     * @param typeToAdd
     * @param compToAdd 
     */
    protected void putComponents(String typeToAdd, Component compToAdd){
        if( modalLogView == null ){
            modalLogView = new TreeMap<Integer, Component>();
        }
        Component toUnset = modalLogView.get(typeToAdd.hashCode());
        toUnset = null;
        modalLogView.put(typeToAdd.hashCode(), compToAdd);
    }
    
    
}
