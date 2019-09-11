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

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicWrite {
    protected void doWriteToIndexWord(final ThWordRule outerRuleWord){
        ThIndexRule indexRule;
        ThIndexStatistic indexStatistic;
        ThWordRule funcRuleWord;
        AppFileStorageIndex currentIndexStorages;
        UUID pollNextUuid;
        URI byPrefixGetUri;
        Map<String, String> byPrefixGetMap;
        ThWordEventLogic eventLogic;
        try {
            AdilRule adilRule = outerRuleWord.getIndexRule().getAdilRule();
            AdilState adilState = adilRule.getAdilState();
            Integer numberProcessIndexSystem = 12;
            String msgToLog = AdilConstants.INFO_LOGIC_POSITION
                    + AdilConstants.CANONICALNAME
                    + ThWordLogicWrite.class.getCanonicalName()
                    + AdilConstants.METHOD
                    + "doWriteToIndexWord()";
            adilState.putLogLineByProcessNumberMsg(numberProcessIndexSystem, 
                    msgToLog
                    + AdilConstants.START);
            funcRuleWord = (ThWordRule) outerRuleWord;
            
            indexRule = funcRuleWord.getIndexRule();
            //indexStatistic = indexRule.getIndexStatistic();
            //indexStatistic.updateDataStorages();
            currentIndexStorages = funcRuleWord.getIndexRule().getIndexState().currentIndexStorages();
            byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
            byPrefixGetMap = currentIndexStorages.byPrefixGetMap( 
                    AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
            try( FileSystem fsForWriteData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
                System.out.println("   ---   ---   ---   ---   ---   ---   ---   ---   ---   " 
                    + ThWordLogicWrite.class.getCanonicalName() + " open storage " + fsForWriteData.getPath("/").toUri().toString());
                do {
                    pollNextUuid = outerRuleWord.getWordState().getBusEventShort().pollNextUuid(2, 3);
                    if( checkStateForUuidOnDoWrite(outerRuleWord, pollNextUuid) ){
                        //move uuid in bus event shot
                        //move uuid in statebuseventlocal
                        //do write data
                        //move uuid into wait read
                        eventLogic = (ThWordEventLogic) outerRuleWord.getWordState().getEventLogic();
                        try {
                            eventLogic.writeDataToStorage(fsForWriteData, pollNextUuid);
                        } catch(IllegalStateException exIllState) {
                            System.err.println(exIllState.getMessage());
                            exIllState.printStackTrace();
                        }
                    } else {
                        //not founded in nextStep ready insertToCache uuids go into...
                    }
                } while( funcRuleWord.isRunnedWordWorkRouter() );
                //need write all cached data after end for all read jobs
            } catch(FileSystemNotFoundException ex){
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } catch(ProviderNotFoundException ex){
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } catch(IllegalArgumentException ex){
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } catch(SecurityException ex){
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            adilState.putLogLineByProcessNumberMsg(numberProcessIndexSystem, 
                msgToLog
                + AdilConstants.FINISH);
        } finally {
            pollNextUuid = null;
            indexRule = null;
            indexStatistic = null;
            funcRuleWord = null;
            currentIndexStorages = null;
            byPrefixGetUri = null;
            byPrefixGetMap = null;
            eventLogic = null;
        }
    }
    protected Boolean checkStateForUuidOnDoWrite(ThWordRule outerRuleWord, UUID checkedReturnedUuid){
        LinkedTransferQueue<Integer[]> foundedNodes;
        try {
            if( checkedReturnedUuid != null ){
                foundedNodes = outerRuleWord.getWordState().getBusEventShortNextStep().foundUuidInList(checkedReturnedUuid);
                while( !foundedNodes.isEmpty() ) {
                    Integer[] foundUuidInList = foundedNodes.poll();
                    if( foundUuidInList[0] == -1 || foundUuidInList[1] == -1 ){
                        continue;
                    }
                    if( foundUuidInList[0] == 0 || foundUuidInList[1] == 1 ){
                        continue;
                    }
                    if( foundUuidInList[0] == 0 && foundUuidInList[1] == 2 ){
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        }
        finally {
            foundedNodes = null;
        }
    }
    protected void readNextUuidFromEventShot(){
        
    }
    protected void readExtendedInfoForUUID(){
        
    }
    protected void readDataFromCache(){
        
    }
    protected void writeDataToStorage(){
        
    }
    protected void deleteOldDataFromStorage(){
        
    }

}
