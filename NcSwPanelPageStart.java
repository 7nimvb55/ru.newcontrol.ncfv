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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;


/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwPanelPageStart {
        /**
     *
     * @return
     */
    public static JPanel getNorthPanel(){
        JPanel northPanel = new JPanel();
        Border northBorder = BorderFactory.createTitledBorder("NORTH panel");
        northPanel.setBorder(northBorder);
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JTextField addNorthWordSearch = new JTextField();
        addNorthWordSearch.setPreferredSize(new Dimension(300, 20));
        
        northPanel.add(addNorthWordSearch);
        JButton btnSearch = NcSwGUIComponent.createButton("Search",null,"");
        
        btnSearch.addActionListener(new ActionListener(){
            public void  actionPerformed(ActionEvent e){
                String strSearch = addNorthWordSearch.getText();
                int reply = JOptionPane.showConfirmDialog(null, strSearch, "Title", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION){
                  NcSwThreadManager.setToViewSearchedResult(strSearch);
                }
            }
        });
        northPanel.add(btnSearch);
        return northPanel;
    }
}
