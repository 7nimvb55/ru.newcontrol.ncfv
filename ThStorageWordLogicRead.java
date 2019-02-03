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
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicRead {
    protected void doReadFromIndexStorageWord(ThStorageWordRule outerRuleStorageWord){
        try {
        /*ConcurrentSkipListMap<UUID, TdataWord> readedFormData =
                                    new ConcurrentSkipListMap<UUID, TdataWord>();
                            if( Files.exists(nowWritedFile) ){

                                try(ObjectInputStream ois = 
                                    new ObjectInputStream(Files.newInputStream(nowWritedFile)))
                                {
                                    readedFormData.putAll((ConcurrentSkipListMap<UUID, TdataWord>) ois.readObject());

                                } catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                try{
                                    Path mvOldDir = fsForReadData.getPath(AppFileNamesConstants.DIR_INDEX_OLD_DATA);
                                    if( Files.notExists(mvOldDir) ){
                                        Files.createDirectories(mvOldDir);
                                    }
                                    Path forNewMove = fsForReadData.getPath(AppFileNamesConstants.DIR_INDEX_OLD_DATA
                                            ,writerPath + "-"
                                            + AppFileOperationsSimple.getNowTimeStringWithMS() 
                                            + "-" 
                                            + String.valueOf(counIterations));

                                    Files.move(nowWritedFile, forNewMove);
                                } catch(UnsupportedOperationException ex){
                                    System.err.println(ex.getMessage());
                                    ex.printStackTrace();
                                } catch(FileAlreadyExistsException ex){
                                    System.err.println(ex.getMessage());
                                    ex.printStackTrace();
                                } catch(DirectoryNotEmptyException ex){
                                    System.err.println(ex.getMessage());
                                    ex.printStackTrace();
                                } catch(AtomicMoveNotSupportedException ex){
                                    System.err.println(ex.getMessage());
                                    ex.printStackTrace();
                                } catch(IOException ex){
                                    System.err.println(ex.getMessage());
                                    ex.printStackTrace();
                                }
                            }
                            ConcurrentSkipListMap<UUID, TdataWord> readyForWriteData =
                                    new ConcurrentSkipListMap<UUID, TdataWord>();
                            readyForWriteData.putAll(readedFormData);
                            readyForWriteData.putAll(writerData);*/
        } finally {

        }
    }
}
