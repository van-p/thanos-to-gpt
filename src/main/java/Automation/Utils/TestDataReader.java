package Automation.Utils;

import Automation.Utils.Enums.DateRequired;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class TestDataReader
{

    private ArrayList<List<String>> excelData;
    private Config testConfig;

    public TestDataReader(Config testConfig, String sheetName, String excelFilePath)
    {
        readFile(testConfig, sheetName, excelFilePath);
    }

    public TestDataReader(Config testConfig, String sheetName, InputStream inputStream)
    {
        readFile(testConfig, sheetName, inputStream);
    }

    public TestDataReader(Config testConfig)
    {
        this.testConfig = testConfig;
    }

    private static String convertCellValueToString(Cell cell, FormulaEvaluator evaluator)
    {
        if (cell == null)
        {
            return "";
        }
        String value = "";
        switch (cell.getCellType())
        {
            case NUMERIC:
                value = Double.toString(cell.getNumericCellValue());
                break;
            case STRING:
                value = cell.getRichStringCellValue().toString();
                break;
            case FORMULA:
                HSSFDataFormatter formatter = new HSSFDataFormatter();
                value = formatter.formatCellValue(cell, evaluator);
                break;
            case BOOLEAN:
                value = Boolean.toString(cell.getBooleanCellValue());
                break;
            default:
                value = "";
                break;
        }
        return value;
    }

    /**
     * This function will create a new Sheet inside an existing excel file. If excel file is not present then it will create it and add the required sheet
     *
     * @param testConfig    - object of config
     * @param filePath      - full path of excel file
     * @param sheetName     - name of sheet to be added
     * @param dataToBeAdded - data to be added inside that sheet, takes it as List of Objects.
     */
    public static void createNewSheetInsideExcel(Config testConfig, String filePath, String sheetName, List<Object[]> dataToBeAdded)
    {
        XSSFWorkbook workbook = null;
        File file = new File(filePath);
        FileOutputStream outputStream = null;

        if (file.exists())
        {
            testConfig.logComment("Creating new sheet inside existing excel file");
            try
            {
                workbook = (XSSFWorkbook) WorkbookFactory.create(file);
            } catch (Exception e)
            {
                testConfig.logExceptionAndFail(e);
            }
        } else
        {
            testConfig.logComment("Existing excel file not found so creating fresh excel at the given path");
            workbook = new XSSFWorkbook();
        }

        XSSFSheet sheet = workbook.createSheet(sheetName);
        int rowCount = 0;
        for (Object[] singleRowData : dataToBeAdded)
        {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Object field : singleRowData)
            {
                Cell cell = row.createCell(columnCount++);
                if (field instanceof String)
                {
                    if (((String) field).contains("Formula="))
                    {
                        cell.setCellFormula(field.toString().replace("Formula=", ""));
                    } else
                    {
                        cell.setCellValue(field.toString());
                    }
                } else if (field instanceof Integer)
                {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try
        {
            outputStream = new FileOutputStream(filePath, true);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            testConfig.logComment("Excel sheet created succefully at - " + filePath);
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        }
    }

    /**
     * This function will help to update the data of any particular cell inside an existing excel file
     *
     * @param testConfig      - object of config
     * @param filePath        - full path of excel file
     * @param sheetName       - name of sheet present inside that excel file
     * @param dataToBeUpdated - value to be added inside the cell
     * @param rowNumber       - row number of the cell
     * @param columnNumber    - column number of the cell
     */
    public static void updateDataInExcelSheet(Config testConfig, String filePath, String sheetName, String dataToBeUpdated, int rowNumber, int columnNumber)
    {
        testConfig.logComment("Updating data inside sheet name : " + sheetName);
        try
        {
            XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(new File(filePath));
            Sheet sheet = workbook.getSheet(sheetName);
            Cell cell = sheet.getRow(rowNumber).getCell(columnNumber);
            cell.setCellValue(dataToBeUpdated);
            FileOutputStream outputStream = new FileOutputStream(sheetName, true);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is to read a csv file and return it as List of String Array
     *
     * @param filePath - file path of csv
     * @return - List<String[]> of data
     */
    public static List<String[]> getCompleteCsvData(String filePath)
    {
        CSVReader csvReader = null;
        List<String[]> csvBody = null;
        try
        {
            csvReader = new CSVReader(new FileReader(filePath));
            csvBody = csvReader.readAll();
            csvReader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return csvBody;
    }

    /**
     * This function is used to read the excel sheets of type .xls, .xlsx and .csv
     *
     * @param sheetName
     * @param filePath
     */
    @SuppressWarnings("resource")
    private void readFile(Config testConfig, String sheetName, String filePath)
    {
        String filename = filePath.trim();
        BufferedReader csvFile = null;
        FileInputStream fileInputStream = null;
        excelData = new ArrayList<List<String>>();
        testConfig.logCommentForDebugging("Read:-'" + filePath + "', Sheet:- '" + sheetName + "'");
        try
        {
            fileInputStream = new FileInputStream(filename);
            if (filename.endsWith(".xls"))
            {
                HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
                HSSFSheet sheet = workbook.getSheet(sheetName);
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                if (sheet == null)
                {
                    testConfig.logFailToEndExecution("No sheet found with name '" + sheetName + "' in " + filename);
                }

                Iterator<Row> rows = sheet.rowIterator();
                while (rows.hasNext())
                {
                    HSSFRow row = (HSSFRow) rows.next();
                    List<String> data = new ArrayList<String>();
                    for (int z = 0; z < row.getLastCellNum(); z++)
                    {
                        String str = convertCellValueToString(row.getCell(z), evaluator);
                        data.add(str);
                    }
                    excelData.add(data);
                }
            } else if (filename.endsWith(".xlsx"))
            {
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet sheet = workbook.getSheet(sheetName);
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                if (sheet == null)
                {
                    testConfig.logFailToEndExecution("No sheet found with name '" + sheetName + "' in " + filename);
                }

                Iterator<Row> rows = sheet.rowIterator();
                while (rows.hasNext())
                {
                    XSSFRow row = (XSSFRow) rows.next();
                    List<String> data = new ArrayList<String>();
                    for (int z = 0; z < row.getLastCellNum(); z++)
                    {
                        String str = convertCellValueToString(row.getCell(z), evaluator);
                        data.add(str);
                    }
                    excelData.add(data);
                }
            } else if (filename.endsWith(".csv"))
            {
                csvFile = new BufferedReader(new FileReader(filePath));
                String dataRow = csvFile.readLine();

                while (dataRow != null)
                {
                    String[] dataArray = dataRow.split(",");
                    List<String> data = new ArrayList<String>();
                    int counter = 0;
                    String tempStr = "";
                    for (int z = 0; z < dataArray.length; z++)
                    {
                        String str = dataArray[z];
                        boolean sameLoop = false;
                        if (str.startsWith("\""))
                        {
                            str = str.replace("\"", "");
                            counter++;
                            tempStr = str;
                            sameLoop = true;
                        }
                        if (str.endsWith("\""))
                        {
                            str = str.replace("\"", "");
                            counter = 0;
                            if (sameLoop)
                            {
                                tempStr = str;
                            } else
                            {
                                tempStr = tempStr + "," + str;
                            }
                        }

                        if (counter > 0)
                        {
                            if (!tempStr.equals(str))
                            {
                                tempStr = tempStr + "," + str;
                            }
                        } else
                        {
                            if (StringUtils.isEmpty(tempStr))
                            {
                                tempStr = str;
                            }
                            data.add(tempStr);
                            tempStr = "";
                        }
                    }
                    excelData.add(data);
                    dataRow = csvFile.readLine();
                }
            } else if (filename.endsWith(".mdt"))
            {
                csvFile = new BufferedReader(new FileReader(filePath));
                String dataRow = csvFile.readLine();

                while (dataRow != null)
                {
                    String[] dataArray = dataRow.split("\\|");
                    List<String> data = new ArrayList<String>();
                    for (int z = 0; z < dataArray.length; z++)
                    {
                        String str = dataArray[z];
                        if (str.startsWith("\""))
                        {
                            str = str.replace("\"", "");
                        }
                        data.add(str);
                    }
                    excelData.add(data);
                    dataRow = csvFile.readLine();
                }
            }
        } catch (FileNotFoundException e)
        {
            testConfig.logExceptionAndFail(e);
        } catch (IOException e)
        {
            testConfig.logExceptionAndFail(e);
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        } finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                } catch (IOException e)
                {
                    testConfig.logExceptionAndFail(e);
                }
            }

            if (csvFile != null)
            {
                try
                {
                    csvFile.close();
                } catch (IOException e)
                {
                    testConfig.logExceptionAndFail(e);
                }
            }
        }
    }

    private void readFile(Config testConfig, String sheetName, InputStream input)
    {
        FileInputStream fileInputStream = null;
        excelData = new ArrayList<List<String>>();
        try
        {

            HSSFWorkbook workbook = new HSSFWorkbook(input);
            HSSFSheet sheet = workbook.getSheet(sheetName);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            if (sheet == null)
            {
                testConfig.logFailToEndExecution("No sheet found with name '" + sheetName);
            }

            Iterator<Row> rows = sheet.rowIterator();
            while (rows.hasNext())
            {
                HSSFRow row = (HSSFRow) rows.next();
                List<String> data = new ArrayList<String>();
                for (int z = 0; z < row.getLastCellNum(); z++)
                {
                    String str = convertCellValueToString(row.getCell(z), evaluator);
                    data.add(str);
                }
                excelData.add(data);
            }

        } catch (IOException e)
        {
            testConfig.logExceptionAndFail(e);
        } finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                } catch (IOException e)
                {
                    testConfig.logExceptionAndFail(e);
                }
            }
        }
    }

    /**
     * This function is used to fetch the data of a particular 'cell' of excel sheet
     *
     * @param testConfig - Pass object of config TODO
     * @param row        - row number of sheet
     * @param column     - coumn name of sheet
     * @return - string value
     */
    public String getData(Config testConfig, int row, String column)
    {
        String data = "";
        List<String> headerRow = excelData.get(0);
        List<String> dataRow = excelData.get(row);

        for (int i = 0; i < headerRow.size(); i++)
        {
            if (headerRow.get(i).equalsIgnoreCase(column))
            {
                try
                {
                    data = dataRow.get(i);
                } catch (IndexOutOfBoundsException e)
                {
                    data = "";
                }
                break;
            }
        }

        if (data.equals(""))
        {
            data = "{skip}";
        } else
        {
            if (data.contains("{empty}"))
            {
                data = data.replace("{empty}", "");
            }
            if (data.contains("{space}"))
            {
                data = data.replace("{space}", " ");
            }
            if (data.contains("{currentDateTime}"))
            {
                data = data.replace("{currentDateTime}", DataGenerator.getCurrentDateTime("YYYY-MM-dd hh:mm:ss ZZZZ"));
            }
            if (data.contains("{currentDateTimeUtc}"))
            {
                data = data.replace("{currentDateTimeUtc}", DataGenerator.getCurrentDateTimeInUTC("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            }
            if (data.startsWith("Encrypted:"))
            {
                data = data.replaceAll("Encrypted:", "");
                data = CommonUtilities.decryptMessage(data.getBytes());
            }
            if (data.contains("randomUUID"))
            {
                UUID uuid = UUID.randomUUID();
                data = data.replace("{randomUUID}", uuid.toString());
            }

            if (data.contains("{timeStamp}"))
            {
                data = data.replace("{timeStamp}", DataGenerator.getTimeinMillSeconds());
            }

            if (data.contains("{randomDecimalNum:"))
            {
                int start = data.indexOf("Num:") + 4;
                int end = data.indexOf("}", start);
                String substr = data.substring(start, end);
                String[] values = substr.split(",");
                int lowerBound = Integer.parseInt(values[0]);
                int upperBound = Integer.parseInt(values[1]);
                int precision = Integer.parseInt(values[2]);
                // {randomDecimalNum:0,10,2}
                data = data.replace("{randomDecimalNum:" + lowerBound + "," + upperBound + "," + precision + "}", DataGenerator.generateRandomDecimalValue(lowerBound, upperBound, precision));
            }

            while (data.contains("{random"))
            {
                if (data.contains("{randomString:"))
                {
                    int start = data.indexOf("String:") + 7;
                    int end = data.indexOf("}", start);
                    int length = Integer.parseInt(data.substring(start, end));
                    data = data.replace("{randomString:" + length + "}", DataGenerator.generateRandomString(length));
                } else
                {
                    int start = data.indexOf("Num:") + 4;
                    int end = data.indexOf("}", start);
                    int length = Integer.parseInt(data.substring(start, end));
                    if (data.contains("{randomAlphaNum:" + length + "}"))
                    {
                        data = data.replace("{randomAlphaNum:" + length + "}", DataGenerator.generateRandomAlphaNumericString(length));
                    }
                    if (data.contains("{randomNum:" + length + "}"))
                    {
                        data = data.replace("{randomNum:" + length + "}", Long.toString(DataGenerator.generateRandomNumber(length)));
                    }
                }
            }
        }
        if (data.contains("{date"))
        {
            int start = data.indexOf("{date") + 5;
            int end = data.indexOf("}", start);
            String subStr = data.substring(start, end);
            String[] dateFormat = subStr.split("_");
            if (dateFormat[0].contains("Current"))
            {
                data = data.replace("{dateCurrent_" + dateFormat[1] + "}", DataGenerator.getCurrentDate(dateFormat[1]));
            }
            if (dateFormat[0].contains("Tomorrow"))
            {
                data = data.replace("{dateTomorrow_" + dateFormat[1] + "}", DataGenerator.getDate(dateFormat[1], DateRequired.FutureDate, 1));
            }
            if (dateFormat[0].contains("Yesterday"))
            {
                data = data.replace("{dateYesterday_" + dateFormat[1] + "}", DataGenerator.getDate(dateFormat[1], DateRequired.FutureDate, -1));
            }
            if (dateFormat[0].contains("DaysAfterToday"))
            {
                int newend = data.indexOf("DaysAfterToday");
                int somedays = Integer.parseInt(data.substring(start, newend));
                data = data.replace("{date" + somedays + "DaysAfterToday_" + dateFormat[1] + "}", DataGenerator.getDate(dateFormat[1], DateRequired.FutureDate, somedays));
            }
            if (dateFormat[0].contains("DaysBeforeToday"))
            {
                int newend = data.indexOf("DaysBeforeToday");
                int somedays = Integer.parseInt(data.substring(start, newend));
                data = data.replace("{date" + somedays + "DaysBeforeToday_" + dateFormat[1] + "}", DataGenerator.getDate(dateFormat[1], DateRequired.FutureDate, -somedays));
            }
        }

        testConfig.logCommentForDebugging("Value of '" + column + "' column at row " + row + " is:- '" + data + "'");
        return data;
    }

    /**
     * This method returns the number of records present in the datasheet
     *
     * @return number of records
     */
    public int getRecordsNum()
    {
        return excelData.size();
    }

    /**
     * This method returns the number of columns of the datasheet (It counts the header and returns the number)
     *
     * @return number of columns
     */
    public int getColumnNum()
    {
        List<String> headerRow = excelData.get(0);
        return headerRow.size();
    }

    /**
     * Returns the Excel header value
     *
     * @param rowNumber - Excel Row number to read
     * @return The value read
     */
    public String getHeaderData(int rowNumber)
    {
        String data = "";
        List<String> dataRow = excelData.get(0);
        try
        {
            data = dataRow.get(rowNumber);
        } catch (IndexOutOfBoundsException e)
        {
            data = "";
        }
        data = data.trim();
        return data;
    }

    /**
     * @param testConfig - object of confing
     * @param rowNum     - row number of sheet
     * @param apihelper  - object of apiHelper
     * @return Hashmap - testdata in hashmap
     */
    public HashMap<String, String> getTestData(Config testConfig, int rowNum)
    {
        HashMap<String, String> testDataMap = new HashMap<>();
        for (int i = 0; i < excelData.get(0).size(); i++)
        {
            String key = excelData.get(0).get(i);
            String value = getData(testConfig, rowNum, excelData.get(0).get(i));
            if (value.equals("{skip}"))
            {
                if (testConfig.testData.get(key) != null)
                {
                    testConfig.testData.remove(key);
                }
                continue;
            }

            value = testConfig.replaceArgumentsWithRunTimeProperties(value);
            testDataMap.put(key, value);
        }
        testDataMap.remove("RowNo");
        return testDataMap;
    }

    /**
     * This method will read data for row index rowNum and having header at row `headerRowNum`
     *
     * @param testConfig   - object of confing
     * @param rowNum       - row number of sheet
     * @param headerRowNum - row number of headers in sheet
     * @return Hashmap - testdata in hashmap
     */
    public HashMap<String, String> getTestData(Config testConfig, int rowNum, int headerRowNum)
    {
        HashMap<String, String> testDataMap = new HashMap<>();
        for (int i = 0; i < excelData.get(headerRowNum).size(); i++)
        {
            String key = excelData.get(headerRowNum).get(i);
            String value = null;
            if (i < excelData.get(rowNum).size())
            {
                value = excelData.get(rowNum).get(i);
            }
            if (value == null || value.equals("{skip}"))
            {
                if (testConfig.testData.get(key) != null)
                {
                    testConfig.testData.remove(key);
                }
                continue;
            }

            value = testConfig.replaceArgumentsWithRunTimeProperties(value);
            testDataMap.put(key, value);
        }
        return testDataMap;
    }

    /**
     * Method to update the csv file on the specified row and column
     *
     * @param testConfig          - object of testConfig
     * @param filePath            - path of csv file
     * @param testDataToBeUpdated - testdata in string
     * @param testDataRowNo       - sheet row number
     * @param testDataColumnNo    - sheet column number
     */
    public void updateTestDataInCsvFile(Config testConfig, String filePath, String testDataToBeUpdated, int testDataRowNo, int testDataColumnNo)
    {
        CSVReader reader = null;
        List<String[]> csvBody = null;
        try
        {
            reader = new CSVReader(new FileReader(filePath));
            csvBody = reader.readAll();
            csvBody.get(testDataRowNo)[testDataColumnNo] = testDataToBeUpdated;
            reader.close();
        } catch (Exception e)
        {
            testConfig.logException("Exception while reading csv file", e);
        }

        CSVWriter writer;
        try
        {
            writer = new CSVWriter(new FileWriter(filePath));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
        } catch (IOException e)
        {
            testConfig.logException("File could not be closed, the exception is ", e);
        }
    }

    /**
     * This method is to read a csv file and return it as HashMap in form of key values
     *
     * @param filePath - file path of csv
     * @return - Hashmap of data
     */
    public HashMap<String, String> getDataFromCsv(String filePath)
    {
        String key;
        String value;
        CSVReader csvReader = null;
        List<String[]> csvBody = null;
        HashMap<String, String> csvData = new HashMap<>();
        try
        {
            csvReader = new CSVReader(new FileReader(filePath));
            csvBody = csvReader.readAll();
            if (csvBody.size() >= 2)
            {
                for (int i = 0; i < csvBody.get(0).length; i++)
                {
                    key = csvBody.get(0)[i].replace(" ", "");
                    value = csvBody.get(1)[i];
                    if (value.isEmpty())
                    {
                        value = "{skip}";
                    }
                    csvData.put(key, value);
                }
            }
            csvReader.close();
        } catch (Exception e)
        {
            testConfig.logException("Exception while reading csv file", e);
        }
        return csvData;
    }
}