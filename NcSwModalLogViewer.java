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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwModalLogViewer {
    private static String modalTitle = "View log file";
    
    public static JDialog getDialogLogViewer(JFrame mainFrame){
        JDialog modalWindow = new JDialog(mainFrame, modalTitle, true);
        JPanel modalPanelPageStart = getPanelPageStart(modalWindow);
        JPanel modalPanelCenter = getPanelCenter(modalWindow);
        JPanel modalPanelPageEnd = getPanelPageEnd(modalWindow);
        
        modalWindow.add(modalPanelPageStart, BorderLayout.PAGE_START);
        modalWindow.add(modalPanelCenter, BorderLayout.CENTER);
        modalWindow.add(modalPanelPageEnd, BorderLayout.PAGE_END);
        Dimension pSize = new Dimension(800, 600);
        modalWindow.setSize(pSize);
        modalWindow.setPreferredSize(pSize);
        modalWindow.setLocationRelativeTo(mainFrame);
        return modalWindow;
    }
    private static JPanel getPanelCenter(JDialog modalWindowInFunc){
        JPanel modalPanelInFunc = new JPanel();
        JButton buttonClose = getButtonClose(modalWindowInFunc);
        modalPanelInFunc.add(buttonClose);
        return modalPanelInFunc;
    }
    private static JPanel getPanelPageStart(JDialog modalWindowInFunc){
        JPanel modalPanelInFunc = new JPanel();
        JButton buttonClose = getButtonClose(modalWindowInFunc);
        modalPanelInFunc.add(buttonClose);
        return modalPanelInFunc;
    }
    private static JPanel getPanelPageEnd(JDialog modalWindowInFunc){
        JPanel modalPanelInFunc = new JPanel();
        JButton buttonClose = getButtonClose(modalWindowInFunc);
        modalPanelInFunc.add(buttonClose);
        return modalPanelInFunc;
    }
    private static JButton getButtonClose(JDialog modalWindowForButton){
        JButton buttonClose = new JButton("Close");
        buttonClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                modalWindowForButton.setVisible(false);
            }
        });
        return buttonClose;
    }
    
}
