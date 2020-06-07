package LoadData;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadFileXLSX {
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		String jdbcURL = "jdbc:mysql://localhost:3306/students";
		String username = "root";
		String password = "123456789";
		String excelFilePath = "DataSV.xlsx";

		int batchSize = 20;

		Connection connection = null;

		try {
			long start = System.currentTimeMillis();

			FileInputStream inputStream = new FileInputStream(excelFilePath);
			Workbook workbook = new XSSFWorkbook(inputStream);

			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = firstSheet.iterator();

			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection(jdbcURL, username, password);
			connection.setAutoCommit(false);

			String sql = "INSERT INTO Student (STT,ID,Name,Year,Address) VALUES  (?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 0;
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();

					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						int STT = (int) nextCell.getNumericCellValue();
						statement.setInt(1, STT);
						break;
					case 1:
						String ID = nextCell.getStringCellValue();
						statement.setString(2,ID);
						break;
					case 2:
						String Name = nextCell.getStringCellValue();
						statement.setString(3, Name);
						break;
					case 3:
						String Year = nextCell.getStringCellValue();
						statement.setString(4, Year);
						break;
					case 4:
						String Address = nextCell.getStringCellValue();
						statement.setString(5, Address);
						break;
					}

				}

				statement.addBatch();

				if (count % batchSize == 0) {
					statement.executeBatch();
				}
			}

			workbook.close();

			// execute the remaining queries
			statement.executeBatch();

			connection.commit();
			connection.close();

			long end = System.currentTimeMillis();
			System.out.printf("Import done in %d ms\n", (end - start));

		} catch (FileNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
