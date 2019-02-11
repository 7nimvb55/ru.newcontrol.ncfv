/*
 * Copyright 2019 wladimirowichbiaran.
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
 * From ThStorageWordBusOutput get data, convert int typeWord to String for 
 * directory, create or add to path, get from bus data, read heximal value,  
 * first four bytes, create or add to sub path, calculate length for subString 
 * value, generate name for list file in format wl-(UUID)-(Size)-(Volume Number)
 * 
 * ThStorageWordStatistic - search directories and list file names
 * ThStorageWordCache - temp storages for data
 * ThStorageWordHelperFileSystem - static functions for create, move, scan 
 * directories and list files
 * 
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicRouter {
    protected void doRouterForIndexStorageWord(ThStorageWordRule outerRuleStorageWord){
        ThIndexRule indexRule = outerRuleStorageWord.getIndexRule();
        ThIndexStatistic indexStatistic = indexRule.getIndexStatistic();
        
        ThStorageWordStatistic storageWordStatistic = outerRuleStorageWord.getStorageWordStatistic();
        
        
    }
}
