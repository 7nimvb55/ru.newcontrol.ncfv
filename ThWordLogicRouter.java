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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicRouter {
    /**
     * with events logic version
     * @param outerRuleWord 
     */
    protected void doRouterForIndexWord(final ThWordRule outerRuleWord){
        ThWordRule wordRuleFunc;
        ThIndexRule indexRuleFunc;
        ThIndexState indexState;
        ThStorageWordRule storageWordRuleFunc;
        ThStorageWordState storageWordState;
        ThStorageWordBusOutput busOutputForWordRouter;
        try {
            wordRuleFunc = (ThWordRule) outerRuleWord;
            indexRuleFunc = wordRuleFunc.getIndexRule();
            indexState = indexRuleFunc.getIndexState();
            storageWordRuleFunc = indexRuleFunc.getIndexState().getRuleStorageWord();
            storageWordState = storageWordRuleFunc.getStorageWordState();
            busOutputForWordRouter = storageWordState.getBusJobForWordWrite();
            do{
                outerBusIterator(wordRuleFunc, busOutputForWordRouter);
            } while( storageWordRuleFunc.isRunnedStorageWordWorkFilter() );
        } finally {
            wordRuleFunc = null;
            indexRuleFunc = null;
            indexState = null;
            storageWordRuleFunc = null;
            storageWordState = null;
            busOutputForWordRouter = null;
        }
    }
    protected void outerBusIterator(
            ThWordRule outerRuleWord, 
            ThStorageWordBusOutput busOutputForWordRouterInputed){
        ThStorageWordBusOutput busOutputForWordRouterFunc;
        LinkedTransferQueue<TdataWord> busOutputByTypeWord;
        Integer typeWordOfBusOutput;
        try {
            busOutputForWordRouterFunc = (ThStorageWordBusOutput) busOutputForWordRouterInputed;
            for( Map.Entry<Integer, LinkedTransferQueue<TdataWord>> itemsBusByTypeWord : busOutputForWordRouterFunc.getExistBusEntrySetForTypeWord() ){
                //make event indexes, main flow, set stop flags and insert data into cache
                typeWordOfBusOutput = itemsBusByTypeWord.getKey();
                busOutputByTypeWord = itemsBusByTypeWord.getValue();
                if( busOutputByTypeWord != null ){
                    if( !busOutputByTypeWord.isEmpty() ){
                        generateMainFlowForDataFromBusOutput(outerRuleWord, typeWordOfBusOutput, busOutputByTypeWord);
                    }
                }
            }
        } finally {
            busOutputForWordRouterFunc = null;
            busOutputByTypeWord = null;
            typeWordOfBusOutput = null;
        }
    }
    protected void generateMainFlowForDataFromBusOutput(
            ThWordRule outerRuleWord, 
            Integer typeWordOfBusOutputInputed, 
            LinkedTransferQueue<TdataWord> busOutputByTypeWordInputed){
        ThWordEventLogic eventLogic;
        Boolean labelTypeData;
        Integer typeWordOfBusOutputFunc;
        String hexTagName;
        String subString;
        LinkedTransferQueue<TdataWord> busOutputByTypeWordFunc;
        ConcurrentSkipListMap<UUID, TdataWord> pollFromBusOutputDataPacket;
        TdataWord pollDataItem;
        UUID itemKey;
        try {
            typeWordOfBusOutputFunc = (Integer) typeWordOfBusOutputInputed;
            busOutputByTypeWordFunc = (LinkedTransferQueue<TdataWord>) busOutputByTypeWordInputed;
            hexTagName = new String();
            eventLogic = (ThWordEventLogic) outerRuleWord.getWordState().getEventLogic();
            do {
                pollDataItem = busOutputByTypeWordFunc.poll();
                if( pollDataItem != null ){
                    hexTagName = pollDataItem.hexSubString;
                    subString = pollDataItem.strSubString;
                    itemKey = pollDataItem.randomUUID;
                    eventLogic.insertIntoCacheData(typeWordOfBusOutputFunc, hexTagName, subString, pollDataItem);
                }
            } while( !busOutputByTypeWordFunc.isEmpty() );
        } finally {
            eventLogic = null;
            labelTypeData = null;
            typeWordOfBusOutputFunc = null;
            busOutputByTypeWordFunc = null;
            pollFromBusOutputDataPacket = null;
            pollDataItem = null;
            itemKey = null;
            hexTagName = null;
            subString = null;
        }
    }
    
}
