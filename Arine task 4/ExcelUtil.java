package com.arine.automation.util;

import com.arine.automation.constants.Constants;
import com.arine.automation.models.ExcelMapper;
import com.arine.automation.models.SigTranslation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;


public class ExcelUtil {
    private static final Logger LOGGER = Logger.getLogger(ExcelUtil.class.getName());
    private static DataFormatter dataFormatter = new DataFormatter();
    private static Workbook workbook = null;

    /**
     * This method creates and return test data excel workbook..
     *
     * @return Workbook
     */
    public static Workbook getWorkBook(String filePath, String alternateFilePath) {
        try {
            if (workbook == null) {
                workbook = WorkbookFactory.create(new File(filePath));
            }
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            LOGGER.info(e.getMessage());
            try {
                copyFileUsingStream(filePath, alternateFilePath);
                workbook = WorkbookFactory.create(new File(alternateFilePath));
            } catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
                LOGGER.info(e1.getMessage());
            } finally {
                deleteFile(alternateFilePath);
            }
        } finally {
            deleteFile(alternateFilePath);
        }
        return workbook;
    }

    /**
     * This method return a sheet from workbook if workbook contains sheet with
     * the given sheet name.
     *
     * @param sheetName
     * @return Sheet
     */
    public static Sheet getDataSheet(String filePath, String alternateFilePath, String sheetName) {
        Workbook workbook = getWorkBook(filePath, alternateFilePath);
        if(workbook!=null) {
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                if (sheet.getSheetName().equalsIgnoreCase(sheetName)) {
                    return sheet;
                }
            }
        }
        return null;
    }

    /**
     * Method returns Map (column index with header text mapping).
     * @return Map<Integer, String>
     */
    private static HashMap<Integer, String> getHeaderMap(Row headerRow) {
        if (headerRow == null)
            return null;
        HashMap<Integer, String> headerMap = new HashMap<>();
        Iterator<Cell> cellIterator = headerRow.cellIterator();
        int columnNumber = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String headerValue = dataFormatter.formatCellValue(cell);
            headerMap.put(columnNumber, headerValue);
            columnNumber++;
        }
        return headerMap;
    }

    private static List<ExcelMapper> getMappings(String filePath, String alternateFilePath, String testMasterSheet, ExcelMapper excelMapper) {
        List<ExcelMapper> dataList = new ArrayList<>();
        Sheet sheet = getDataSheet(filePath, alternateFilePath,testMasterSheet);
        if(sheet==null)
            return dataList;
        Iterator<Row> rowIterator = sheet.rowIterator();
        int header = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (header == 0) {
                header++;
            } else {
                List<String> recordData = new ArrayList<>();
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    String cellData = dataFormatter.formatCellValue(row.getCell(i));
                    recordData.add(cellData);
                }
                dataList.add(excelMapper.setData(recordData));
            }
        }
        return dataList;
    }

    /**
     * Method is created to copy of a file.
     *
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFileUsingStream(String source, String destination) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null)
                is.close();
            if (os != null)
                os.close();
        }
    }

    /**
     * Method is created to delete file.
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File alternateFile = new File(filePath);
        if (alternateFile.exists()) {
            alternateFile.delete();
        }
    }

    /**
     * Method is created to clean a directory.
     */
    public static void cleanDirectory(String directoryName) {
        File dir = new File(Constants.USER_DIR + "/" + directoryName);
        deleteRecursive(dir);
        dir.delete();
    }

    /**
     * Method is created to delete a directory recursively .
     */
    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static List<Map<String, String>> getLoginData(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IOException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> dataMap = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = (cell != null) ? cell.getStringCellValue() : "";
                    dataMap.put(headers.get(j), cellValue);
                }

                dataList.add(dataMap);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }



}
