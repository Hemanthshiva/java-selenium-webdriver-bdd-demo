package util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class XlsxUtility {

    private static final Logger LOGGER = Logger.getLogger(XlsxUtility.class.getName());
    private static final String ERROR_CELL_NOT_EXIST = " does not exist in xls";
    private static final String ERROR_OR_COLUMN = " or column ";

    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;

    public XlsxUtility(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(String.format("XLSX utility initialized with file path %s", path));
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(String.format("Error while initializing XLSX utility with message %s",
                        e.getLocalizedMessage()));
            }
            e.printStackTrace();
        }
    }

    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return 0;
        }
        sheet = workbook.getSheetAt(index);
        return sheet.getLastRowNum() + 1;
    }

    public String getCellData(String sheetName, String colName, int rowNum) {
        if (rowNum <= 0) {
            return "";
        }

        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return "";
        }

        int colNum = findColumnIndex(sheetName, colName);
        if (colNum == -1) {
            return "";
        }

        return getCellValue(sheetName, colNum, rowNum);
    }

    private int findColumnIndex(String sheetName, String colName) {
        sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));
        row = sheet.getRow(0);

        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                return i;
            }
        }
        return -1;
    }

    public String getCellData(String sheetName, int colNum, int rowNum) {
        return getCellValue(sheetName, colNum, rowNum);
    }

    private String getCellValue(String sheetName, int colNum, int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            }

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                return "";
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                return "";
            }

            XSSFCell cell = row.getCell(colNum);
            if (cell == null) {
                return "";
            }

            return extractCellValue(cell);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = String.format("row %d%s%d%s",
                    rowNum, ERROR_OR_COLUMN, colNum, ERROR_CELL_NOT_EXIST);
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(errorMessage);
            }
            return errorMessage;
        }
    }

    private String extractCellValue(XSSFCell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
            return formatNumericCell(cell);
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else {
            return String.valueOf(cell.getBooleanCellValue());
        }
    }

    private String formatNumericCell(XSSFCell cell) {
        String cellText = String.valueOf(cell.getNumericCellValue());
        if (DateUtil.isCellDateFormatted(cell)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtil.getJavaDate(cell.getNumericCellValue()));
            return String.format("%d/%d/%s",
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH) + 1,
                    String.valueOf(cal.get(Calendar.YEAR)).substring(2));
        }
        return cellText;
    }
}
