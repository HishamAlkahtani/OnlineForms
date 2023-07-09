package online.hk10.OnlineForms.database;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import online.hk10.OnlineForms.database.entities.FormResponse;

import online.hk10.OnlineForms.database.entities.Form;

public class ExcelUtil {

	/* Takes a form and it's responses, converts them to
	* an excel file and returns the binary data for the file
	* in a ByteArrayOutputStream, so that the file can be sent
	* directly from memory, without writing to an external file 
	* (no extra I/O). */
	public static ByteArrayOutputStream convertToExcel(Form form, List<FormResponse> responses) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		String[] columns = form.getColumns(); 
		
		// Create and style header
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(new XSSFColor(new byte[] {(byte) 190, (byte) 190, (byte) 190}));
		
		XSSFRow header = sheet.createRow(0);
		int headerCellIndex = 0;
		for (String i : columns) {
			XSSFCell cell = header.createCell(headerCellIndex++);
			cell.setCellValue(i);
			cell.setCellStyle(headerStyle);
		}
		
		// fill response data
		int rowNumber = 1;
		for (FormResponse i : responses) {
			XSSFRow row = sheet.createRow(rowNumber++);
			
			int cellIndex = 0;
			for (String col : columns) {
				row.createCell(cellIndex++).setCellValue(i.getResponse().get(col));
			}
		}
		
		try {
			// write to memory and return
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
			return outputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
