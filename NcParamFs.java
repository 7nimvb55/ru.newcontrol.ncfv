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

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcParamFs {
    private FileSystem idxFs;
    private Path dirDirList;
    private Path dirFileExist;
    private Path dirFileHash;
    private Path dirFileList;
    private Path dirFileType;
    private Path dirJournal;
    private Path dirLongWordList;
    private Path dirLongWordData;
    private Path dirStorageWord;
    private Path dirStorageWordAbc;
    private Path dirStorageWordNum;
    private Path dirStorageWordRabc;
    private Path dirStorageWordSpace;
    private Path dirStorageWordSym;
    private Path dirTmp;
    private Path dirWord;
    private long dataTime;
    private int dataHash;

    public NcParamFs(
            FileSystem idxFs, 
            Path dirDirList, 
            Path dirFileExist, 
            Path dirFileHash, 
            Path dirFileList, 
            Path dirFileType, 
            Path dirJournal, 
            Path dirLongWordList, 
            Path dirLongWordData, 
            Path dirStorageWord, 
            Path dirStorageWordAbc, 
            Path dirStorageWordNum, 
            Path dirStorageWordRabc, 
            Path dirStorageWordSpace, 
            Path dirStorageWordSym, 
            Path dirTmp, 
            Path dirWord) {
        this.idxFs = idxFs;
        this.dirDirList = dirDirList;
        this.dirFileExist = dirFileExist;
        this.dirFileHash = dirFileHash;
        this.dirFileList = dirFileList;
        this.dirFileType = dirFileType;
        this.dirJournal = dirJournal;
        this.dirLongWordList = dirLongWordList;
        this.dirLongWordData = dirLongWordData;
        this.dirStorageWord = dirStorageWord;
        this.dirStorageWordAbc = dirStorageWordAbc;
        this.dirStorageWordNum = dirStorageWordNum;
        this.dirStorageWordRabc = dirStorageWordRabc;
        this.dirStorageWordSpace = dirStorageWordSpace;
        this.dirStorageWordSym = dirStorageWordSym;
        this.dirTmp = dirTmp;
        this.dirWord = dirWord;
        
        long nowTime = System.nanoTime();
        
        this.dataTime = nowTime;
        this.dataHash = (idxFs.toString()
            + dirDirList.toString()
            + dirFileExist.toString()
            + dirFileHash.toString()
            + dirFileList.toString()
            + dirFileType.toString()
            + dirJournal.toString()
            + dirLongWordList.toString()
            + dirLongWordData.toString()
            + dirStorageWord.toString()
            + dirStorageWordAbc.toString()
            + dirStorageWordNum.toString()
            + dirStorageWordRabc.toString()
            + dirStorageWordSpace.toString()
            + dirStorageWordSym.toString()
            + dirTmp.toString()
            + dirWord.toString()
            + Long.toString(nowTime)).hashCode();
    }
    
    protected FileSystem getIdxFs(){
        return this.idxFs;
    } 
    protected Path getDirDirList(){
        
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirDirList) ){
            return this.dirDirList;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirDirList.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirFileExist(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirFileExist) ){
            return this.dirFileExist;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirFileExist.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirFileHash(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirFileHash) ){
            return this.dirFileHash;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirFileHash.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirFileList(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirFileList) ){
            return this.dirFileList;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirFileList.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirFileType(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirFileType) ){
            return this.dirFileType;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirFileType.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirJournal(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirJournal) ){
            return this.dirJournal;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirJournal.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirLongWordList(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirLongWordList) ){
            return this.dirLongWordList;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirLongWordList.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirLongWordData(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirLongWordData) ){
            return this.dirLongWordData;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirLongWordData.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirStorageWord(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirStorageWord) ){
            return this.dirStorageWord;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirStorageWord.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirStorageWordAbc(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirStorageWordAbc) ){
            return this.dirStorageWordAbc;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirStorageWordAbc.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirStorageWordNum(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirStorageWordNum) ){
            return this.dirStorageWordNum;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirStorageWordNum.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirStorageWordRabc(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirStorageWordRabc) ){
            return this.dirStorageWordRabc;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirStorageWordRabc.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirStorageWordSpace(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirStorageWordSpace) ){
            return this.dirStorageWordSpace;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirStorageWordSpace.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirStorageWordSym(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirStorageWordSym) ){
            return this.dirStorageWordSym;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirStorageWordSym.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirTmp(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirTmp) ){
            return this.dirTmp;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirTmp.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    } 
    protected Path getDirWord(){
        if( NcFsIdxOperationDirs.existAndHasAccessRW(this.dirWord) ){
            return this.dirWord;
        }
        String strMsg = "Imposible to return for Dir, see log, path "
            + this.dirWord.toString()
            + " not have read and write access or not exist";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    }
    
}
