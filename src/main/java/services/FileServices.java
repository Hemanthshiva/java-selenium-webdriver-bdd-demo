package services;

import java.io.File;
import java.util.List;


public interface FileServices {

    List<File> findFiles(File filePath);

    String getFileName(File filePath);

    String getFilePath(File filePath);

    String getFileExtension(File filePath);

    long getFileSizeInKB(File filePath);

    String getFileMimeType(File filePath);

    File[] getFilesByFileType(File filesDirectory, String fileType);
}
