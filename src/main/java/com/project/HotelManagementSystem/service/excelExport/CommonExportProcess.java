package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.entity.MasterData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class CommonExportProcess<T extends MasterData> {
    public abstract String getSheetName();
    public abstract List<T> featchData();
}
