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

import javax.swing.JProgressBar;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwStatusPanel {
    /**
     * 
     * @param lComp
     * @return 
     */
    protected static JProgressBar getProgressBar(NcSwGUIComponentStatus lComp){
        String componentPath = getProgressBarPath();
        JProgressBar progressBar = NcSwGUIComponent.getProgressBar();
        lComp.putComponents(componentPath,
            progressBar);
        return progressBar;
    }
    /**
     * 
     * @param lComp 
     */
    protected static void visibleProgressBar(NcSwGUIComponentStatus lComp){
        String componentPath = getProgressBarPath();
        JProgressBar progressBar = (JProgressBar) lComp.getComponentByPath(componentPath);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
    }
    /**
     * 
     * @param lComp 
     */
    protected static void hideProgressBar(NcSwGUIComponentStatus lComp){
        String componentPath = getProgressBarPath();
        JProgressBar progressBar = (JProgressBar) lComp.getComponentByPath(componentPath);
        progressBar.setVisible(false);
    }
    /**
     * 
     * @return 
     */
    protected static String getProgressBarPath(){
        String componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SPAGEEND.getStr()
            + NcStrGUIComponent.SJPROGRESSBAR.getStr();
        return componentPath;
    }
}
