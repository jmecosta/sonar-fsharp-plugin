/*
 * Sonar F# Plugin :: Core
 * Copyright (C) 2009-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/*
 * Sonar FSharp Plugin, open source software quality management tool.
 *
 * Sonar FSharp Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar FSharp Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.sonar.plugins.fsharp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

// https://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/

public class UnZip
{
  public static final Logger LOG = Loggers.get(UnZip.class);
  List<String> fileList;

  /**
   * Unzip it
   * @param zipFile input zip file
   * @param output zip file output folder
   */
  public void unZipIt(String zipFile, String outputFolder) throws IOException{

    byte[] buffer = new byte[1024];

    try{

      //create output directory is not exists
      File folder = new File(outputFolder);
      if(!folder.exists()){
          folder.mkdir();
      }

      //get the zip file content
      ZipInputStream zis =
          new ZipInputStream(new FileInputStream(zipFile));
      //get the zipped file list entry
      ZipEntry ze = zis.getNextEntry();

      while(ze!=null){

         String fileName = ze.getName();
         File newFile = new File(outputFolder + File.separator + fileName);

         LOG.debug("Unzip {}", newFile.getAbsolutePath());

          //create all non exists folders
          //else you will hit FileNotFoundException for compressed folder
          new File(newFile.getParent()).mkdirs();

          FileOutputStream fos = new FileOutputStream(newFile);

          int len;
          while ((len = zis.read(buffer)) > 0) {
          fos.write(buffer, 0, len);
          }

          fos.close();
          ze = zis.getNextEntry();
      }

      zis.closeEntry();
      zis.close();

      LOG.debug("Unzip Done.");
    }catch(IOException ex){
      LOG.error("Unzip Failed {}", ex.getMessage());
      throw ex;
    }
  }
}