package org.example;

import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ExcelManager<T> {
    private final ExcelUtil excelUtil;
    private final String sheetName;
    private final String[] headers;
    private final Function<Row, T> rowMapper;
    private final BiConsumer<T, Row> rowWriter;
    private final Function<T, Integer> idExtractor;

    public ExcelManager(ExcelUtil excelUtil, String sheetName, String[] headers,
                               Function<Row, T> rowMapper, BiConsumer<T, Row> rowWriter,
                               Function<T, Integer> idExtractor) {
        this.excelUtil = excelUtil;
        this.sheetName = sheetName;
        this.headers = headers;
        this.rowMapper = rowMapper;
        this.rowWriter = rowWriter;
        this.idExtractor = idExtractor;
    }

    public void load(List<T> entities) {
        try (Workbook workbook = excelUtil.openWorkbookForRead()) {
            load(workbook, entities);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке данных из листа " + sheetName + ": " + e.getMessage());
        }
    }

    public void load(Workbook workbook, List<T> entities) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return;
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            T entity = rowMapper.apply(row);
            if (entity != null) {
                entities.add(entity);
            }
        }
    }

    public void save(List<T> entities) {
        try (Workbook workbook = excelUtil.openWorkbookForRead()) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }
            } else {
                // Clear existing rows (except header)
                for (int i = sheet.getLastRowNum(); i > 0; i--) {
                    sheet.removeRow(sheet.getRow(i));
                }
            }
            for (int i = 0; i < entities.size(); i++) {
                Row row = sheet.createRow(i + 1);
                rowWriter.accept(entities.get(i), row);
            }
            excelUtil.saveWorkbook(workbook);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в лист " + sheetName + ": " + e.getMessage());
        }
    }

    public void removeEntityById(int id) {
        try (Workbook workbook = excelUtil.openWorkbookForRead()) {
            int rowIndex = excelUtil.findRowIndexById(sheetName, 0, id, workbook);
            if (rowIndex != -1) {
                excelUtil.removeRow(sheetName, rowIndex, workbook);
                excelUtil.saveWorkbook(workbook);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при удалении записи из листа " + sheetName + ": " + e.getMessage());
        }
    }

    public void updateFieldById(int id, int columnIndex, String value) {
        try (Workbook workbook = excelUtil.openWorkbookForRead()) {
            int rowIndex = excelUtil.findRowIndexById(sheetName, 0, id, workbook);
            if (rowIndex != -1) {
                excelUtil.changeCellValue(sheetName, rowIndex, columnIndex, value, workbook);
                excelUtil.saveWorkbook(workbook);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении поля в листе " + sheetName + ": " + e.getMessage());
        }
    }

    public void updateFieldById(int id, int columnIndex, double value) {
        try (Workbook workbook = excelUtil.openWorkbookForRead()) {
            int rowIndex = excelUtil.findRowIndexById(sheetName, 0, id, workbook);
            if (rowIndex != -1) {
                excelUtil.changeCellValue(sheetName, rowIndex, columnIndex, value, workbook);
                excelUtil.saveWorkbook(workbook);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении поля в листе " + sheetName + ": " + e.getMessage());
        }
    }

    public void updateEntity(T entity) {
        try (Workbook workbook = excelUtil.openWorkbookForRead()) {
            int id = idExtractor.apply(entity);
            int rowIndex = excelUtil.findRowIndexById(sheetName, 0, id, workbook);
            if (rowIndex != -1) {
                Row row = workbook.getSheet(sheetName).getRow(rowIndex);
                rowWriter.accept(entity, row);
                excelUtil.saveWorkbook(workbook);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении записи в листе " + sheetName + ": " + e.getMessage());
        }
    }
}