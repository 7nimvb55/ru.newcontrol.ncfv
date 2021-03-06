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
public class AppThWorkDirListPack implements Runnable {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public AppThWorkDirListPack(AppThWorkDirListRule ruleForDirListWorkers) {
        super();
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    
    @Override
    public void run() {
        Boolean needFinishStateDirListPacker = innerRuleForDirListWorkers.getNeedFinishStateDirListPacker();
        //this.innerRuleForDirListWorkers.startDirListWriter();
        
        ThreadLocal<ThLogicDirListPacker> logicPacker = new ThreadLocal<ThLogicDirListPacker>();
        try{
            logicPacker.set(new ThLogicDirListPacker(this.innerRuleForDirListWorkers));
            logicPacker.get().doPacker();
        } finally {
            logicPacker.remove();
        }
    }
    
}
