package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.MasterData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public abstract class CommonExportProcess<T extends MasterData> {
    public abstract String getSheetName();
    public abstract List<T> fetchData();
    public abstract List<T> fetchData(CountrySearchQuery query);
    public abstract List<ColumnSpec<T>> columns();

    public String fileName(){
        return getSheetName()+".xlsx";
    }

    public ByteArrayInputStream export(CountrySearchQuery query){
        try{
            Workbook wb = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeSheet(wb, query);
            wb.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Excel export failed for sheet " + getSheetName(), e);
        }
    }

    public void writeSheet(Workbook wb, CountrySearchQuery query){
        Sheet sheet = wb.createSheet(getSheetName());

        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setWrapText(true);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row header = sheet.createRow(0);
        List<ColumnSpec<T>> cols = columns();
        for(int i = 0; i < cols.size(); i++){
            Cell c = header.createCell(i);
            c.setCellValue(cols.get(i).getHeader());
            c.setCellStyle(headerStyle);
        }

        int r = 1;
        for(T rowObject: fetchData(query)){
            Row row = sheet.createRow(r++);
            for(int c = 0; c < cols.size(); c++){
                String text = safe(cols.get(c).getExtractor().apply(rowObject));
                row.createCell(c).setCellValue(text);
            }
        }

        for(int i = 0; i < cols.size(); i++){
            Integer w = cols.get(i).getWidth();
            if(w != null){
                sheet.setColumnWidth(i, w);
            }else{
                sheet.autoSizeColumn(i);
            }
        }

    }

    private String safe(Object v){
        return v == null ? "-" : String.valueOf(v);
    }

}
