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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

// https://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/
// https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/
// https://www.baeldung.com/java-compress-and-uncompress

public class UnZip {
  public static final Logger LOG = Loggers.get(UnZip.class);
  List<String> fileList;

  public static void main(String[] args) throws IOException {
    String workingDirectory = System.getProperty("user.dir");
    String zipFile = workingDirectory + "/FsSonarRunner/target/FsSonarRunner-0.0.0.1.zip";
    String outputFolder = workingDirectory + "/FsSonarRunner/target/extracted";

    if (Files.notExists(Paths.get(zipFile)))
    {
      System.err.println("Input zip files does not exist: " + zipFile);
      return;
    }

    new UnZip().unZipIt(zipFile, outputFolder);
    System.out.println("File extracted to directory: " + outputFolder);
  }

  /**
   * Unzip an archive with sub-directories and extract its contents
   *
   * @param zipFile input zip file
   * @param output  zip file output folder
   */
  public void unZipIt(String zipFile, String outputFolder) throws IOException {

    byte[] buffer = new byte[1024];

    try {
      // create output directory is not exists
      File folder = newFolder(null, outputFolder);

      // get the zip file content
      ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));

      // get the first zipped file list entry
      ZipEntry ze = zis.getNextEntry();
      while (ze != null) {

        if (ze.isDirectory()) {
          // If directory then create a new directory in uncompressed folder
          String folderName = ze.getName();
          File newFolder = newFolder(folder, folderName);
          LOG.debug("folder created: {}", newFolder.getAbsolutePath());
        } else {
          // else create the file
          String fileName = ze.getName();
          File newFile = newFile(folder, fileName);

          LOG.debug("file unzip: {}", newFile.getAbsolutePath());

          FileOutputStream fos = new FileOutputStream(newFile);

          int len;
          while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
          }

          fos.close();
        }

        ze = zis.getNextEntry();
      }

      zis.closeEntry();
      zis.close();

      LOG.debug("Unzip Done.");
    } catch (IOException ex) {
      LOG.error("Unzip Failed {}", ex.getMessage());
      throw ex;
    }
  }

  // create output directory is not exists
  public File newFolder(File parent, String folderName) throws IOException {
    File folder = newFile(parent, folderName);

    if (!folder.exists()) {
      folder.mkdir();
    }

    return folder;
  }

  public File newFile(File parent, String fileName) throws IOException {
    File destFile = new File(parent, fileName);

    if (parent != null) {
      // test on Zip Slip is only possible if `parent` is not `null`
      String destDirPath = parent.getCanonicalPath();
      String destFilePath = destFile.getCanonicalPath();

      if (!destFilePath.startsWith(destDirPath + File.separator)) {
        String msg = "Zip Slip: Entry is outside of the target dir: " + fileName;
        LOG.error(msg);
        throw new IOException(msg);
      }
    }

    return destFile;
  }
}