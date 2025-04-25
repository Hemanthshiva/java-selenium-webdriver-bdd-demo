package unittest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import services.FileServices;
import services.FileServicesImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static steps.TestSetUp.FILE_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = FileServicesImpl.class)
public class FileServiceTest {
    File[] files;
    @Autowired
    FileServices fileServices;

    @Test
    public void testFileCountWithFileExtension() {
        files = fileServices.getFilesByFileType(new File(FILE_PATH + "config"), "xlsx");
        assertEquals("File count in the folder with xlsx extension do not match the expected count", 3, files.length);
    }

    @Test
    public void testTotalFileCount() {
        List<File> foundFiles = fileServices.findFiles(new File(FILE_PATH + "config"));
        assertEquals("File count in the folder do not match the expected count", 10, foundFiles.size());
    }

    @Test
    public void testFileName() {
        files = fileServices.getFilesByFileType(new File(FILE_PATH + "config"), "xlsx");

        List<String> expectedFileNames = Arrays.asList("Book1", "Employee", "VehicleData");
        List<String> actualFileNames = new ArrayList<>();
        for (File file : files) {
            actualFileNames.add(fileServices.getFileName(file));
        }
        assertEquals("File name do not match the expected", expectedFileNames, actualFileNames);
    }

    @Test
    public void testFileExtension() {
        String extension = fileServices.getFileExtension(new File(FILE_PATH + "config/Data.xltx"));
        assertEquals("File extension do not match the expected", "xltx", extension);
    }

    @Test
    public void testFileSizeInKB() {
        int fileSize = Math.toIntExact(fileServices.getFileSizeInKB(new File(FILE_PATH + "config/VehicleData.xlsx")));
        assertEquals("File size do not match the expected size", 9, fileSize);

    }

}
