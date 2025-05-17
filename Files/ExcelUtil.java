package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtil {
    private static final String FILEPATH = "C:\\Users\\user\\Documents\\Homework\\Journal\\Journal.xlsx";

    public Workbook openWorkbookForRead() throws IOException {
        try (FileInputStream fis = new FileInputStream(FILEPATH)) {
            return new XSSFWorkbook(fis);
        }
    }

    public void saveWorkbook(Workbook workbook) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FILEPATH)) {
            workbook.write(fos);
        }
    }

    public int findRowIndexById(String sheetName, int idColumn, int id, Workbook workbook) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return -1;
        for (Row row : sheet) {
            Cell cell = row.getCell(idColumn);
            if (cell != null && cell.getCellType() == CellType.NUMERIC && (int) cell.getNumericCellValue() == id) {
                return row.getRowNum();
            }
        }
        return -1;
    }

    public void changeCellValue(String sheetName, int rowIndex, int columnIndex, String value, Workbook workbook) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return;
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
        if (cell == null) cell = row.createCell(columnIndex);
        cell.setCellValue(value);
    }

    public void changeCellValue(String sheetName, int rowIndex, int columnIndex, double value, Workbook workbook) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return;
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
        if (cell == null) cell = row.createCell(columnIndex);
        cell.setCellValue(value);
    }

    public void removeRow(String sheetName, int rowIndex, Workbook workbook) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null || rowIndex < 0) return;
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }
}