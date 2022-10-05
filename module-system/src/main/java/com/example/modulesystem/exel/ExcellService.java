package com.example.modulesystem.exel;

import com.example.modulecore.exception.CustomRuntimeException;
import com.example.modulecore.response.code.CommonResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class ExcellService {

  public Workbook workbook;
  public String fileName;
  public abstract void initWorkbook();
  public abstract void initFileName();
  public abstract void buildeSheet();

  public void download(HttpServletResponse res) {
    try {
      res.setContentType("application/octet-stream");
      res.setHeader("Content-Disposition", "attachment;filename=" + this.buildFile());
      this.workbook.write(res.getOutputStream());
      this.workbook.close();
    } catch (IOException e) {
      throw new CustomRuntimeException("Excell Download IO Exception", CommonResponseCode.F_DOWNLOAD_FAIL);
    }
  }

  public CellStyle setHeaderStyle() {
    CellStyle style = this.workbook.createCellStyle();
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setAlignment(HorizontalAlignment.CENTER);
    return style;
  }

  private String buildFile() {
    if(this.workbook == null) throw new CustomRuntimeException("Not Found Workbook, FileName",CommonResponseCode.F_NOT_FOUND_FILE);
    String name = this.fileName != null ? this.fileName : "ISolution";
    if (this.workbook instanceof XSSFWorkbook) {
      name += ".xlsx";
    }
    if (this.workbook instanceof SXSSFWorkbook) {
      name += ".xlsx";
    }
    if (this.workbook instanceof HSSFWorkbook) {
      name += ".xls";
    }
    return name;
  }

}
