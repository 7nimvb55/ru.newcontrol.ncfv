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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwGUIComponentStatus {
    
    private TreeMap<Integer, Component> modalLogView;
    
    public NcSwGUIComponentStatus(){
        modalLogView = new TreeMap<Integer, Component>();
    }

    public TreeMap<Integer, Component> getComponentsList(){
        return modalLogView;
    }
    public Component getComponentByPath(String typeToGet){
        return modalLogView.get(typeToGet.hashCode());
    }
    public void putComponents(String typeToAdd, Component compToAdd){
        if( modalLogView == null ){
            modalLogView = new TreeMap<Integer, Component>();
        }
        Component toUnset = modalLogView.get(typeToAdd.hashCode());
        toUnset = null;
        modalLogView.put(typeToAdd.hashCode(), compToAdd);
    }
    
    
}
