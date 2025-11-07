package com.project.HotelManagementSystem.service.excelImport;

import com.project.HotelManagementSystem.entity.MasterData;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class CommonImportProcess<T extends MasterData> {

    public abstract T mapRow(Row row);
    public abstract void saveEntity(T entity);

    public void importExcel(MultipartFile file) {
        try{
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i = 1; i <= rowCount; i++){
                Row row = sheet.getRow(i);
                if(row == null) continue;

                T entity = mapRow(row);
                if(entity != null){
                    saveEntity(entity);
                }
            }
        }catch (IOException e){
            throw new RuntimeException("Excel import failed: "+e.getMessage());
        }
    }
}
