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
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import static ru.newcontrol.ncfv.AppFileOperationsSimple.getLogSubDir;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilStorage {
    protected static Path getIterationLogSubDir(String currentDateTimeStamp){
        Path toReturn;
        try{
            toReturn = Paths.get(getLogSubDir().toString(),
                    currentDateTimeStamp);
            if( Files.notExists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
                try {
                    Files.createDirectories(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                    System.exit(0);
                }
            }
            try {
                pathIsNotReadWriteLink(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                System.exit(0);
            }

            return toReturn;
        } finally {

            
        }    
    }
}
