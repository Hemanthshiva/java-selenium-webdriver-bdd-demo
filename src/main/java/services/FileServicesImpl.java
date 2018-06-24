package services;

import com.google.common.io.Files;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class FileServicesImpl implements FileServices {

    private List<File> list = new ArrayList<>();


    @Override
    public List<File> findFiles(File filePath) {
        File folder = new File(String.valueOf(filePath));
        File[] files = folder.listFiles();
        if (files != null)
            list = Arrays.asList(files);
        return list;
    }

    @Override
    public String getFileName(File filePath) {
        return Files.getNameWithoutExtension(String.valueOf(filePath));
    }

    @Override
    public String getFilePath(File filePath) {
        return Files.simplifyPath(String.valueOf(filePath));
    }

    @Override
    public String getFileExtension(File filePath) {
        return Files.getFileExtension(String.valueOf(filePath));
    }

    @Override
    public long getFileSizeInKB(File filePath) {
        double bytes = filePath.length();
        double kilobytes = (bytes / 1024);
        return Math.round(kilobytes);
    }

    @Override
    public String getFileMimeType(File filePath) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        return mimeTypesMap.getContentType(filePath);
    }


    @Override
    public File[] getFilesByFileType(File filesDirectory, String fileType) {
        File[] files = filesDirectory.listFiles((d, name) -> name.endsWith(fileType));
        return files;
    }


}
