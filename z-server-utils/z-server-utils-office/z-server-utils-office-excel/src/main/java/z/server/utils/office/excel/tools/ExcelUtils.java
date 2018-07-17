package z.server.utils.office.excel.tools;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import z.server.utils.office.excel.tools.enums.ExcelEnum;
import z.server.utils.office.excel.tools.exception.ExcelException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

/**
 * @description: excel工具类
 * @author: zbn
 * @create: 2018-04-03 03:04
 **/
public class ExcelUtils {

    private static final String GET_METHOD_PREFIX = "get";
    private static final String SET_METHOD_PREFIX = "set";

    private static final int COLUMN_WIDTH = 8000;

    private static final String DOUBLE_TYPE = "double";
    private static final String FLOAT_TYPE = "float";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String BYTE_TYPE = "byte";
    private static final String SHORT_TYPE = "short";
    private static final String INT_TYPE = "int";
    private static final String LONG_TYPE = "long";


    /**
    * @author: zbn
    * @create: 3:09 2018/4/3
    **/
    public static Workbook createWorkBook(ExcelEnum excelEnum, InputStream inputStream){
        try {
        if(ExcelEnum.EXCEL_03_VERSION.getCode().equals(excelEnum.getCode())){
            return null != inputStream ? new HSSFWorkbook(inputStream) : new HSSFWorkbook();
        }
        if(ExcelEnum.EXCEL_07_VERSION.getCode().equals(excelEnum.getCode())){
            return null != inputStream ? new XSSFWorkbook(inputStream) : new XSSFWorkbook();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @author: zbn
    * @create: 9:07 2018/4/3
    **/
    public static Sheet createSheet(Workbook workbook, String sheetName){

        Objects.requireNonNull(workbook);
        if(StringUtils.isNotBlank(sheetName)){
            return workbook.createSheet(sheetName);
        }else{
            return workbook.createSheet();
        }
    }

    /**
    * @author: zbn
    * @create: 9:41 2018/4/3
    **/
    public static void writeAndClose(Workbook workbook, OutputStream outputStream){
        try {
            workbook.write(outputStream);
        }catch (Exception e){
            throw new ExcelException();
        }finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * @author: zbn
     * @create: 9:41 2018/4/3
     **/
    public static Row createRow(Sheet sheet, int index){
        return sheet.createRow(index);
    }

    /**
     * @author: zbn
     * @create: 9:41 2018/4/3
     **/
    public static Cell createCell(Row row, int index){
        return row.createCell(index);
    }

    /**
    * @description: 生成行数据
    * @author: zbn
    * @create: 14:03 2018/4/3
    * @param: 行号、行长度、sheet
    * @return row
    **/
    public static Row generateRowData(Sheet sheet, int rowNum, String[] fields, Object rowDataObject, Class<?> clazz, CellStyle cellStyle){

        Objects.requireNonNull(fields);
        Row row = createRow(sheet, rowNum);
        for (int start = 0; start < fields.length; start++){
            Cell cell = createCell(row, start);
            cellValueHandle(cell, fields[start], rowDataObject, clazz);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(start, COLUMN_WIDTH);
        }
        return row;
    }

    /**
    * @description: 处理EXCEL内容
    * @author: zbn
    * @create: 17:28 2018/4/3
    * @param: Sheet sheet, List<Object> contents, String[] fields, Class<?> clazz
    **/
    public static void contentHandle(Sheet sheet, int rowNum, List<Object> contents, String[] fields, Class<?> clazz, CellStyle cellStyle){
        Optional.ofNullable(contents).filter(cs -> !cs.isEmpty()).ifPresent(cs -> {
            for (int start = rowNum,cstart = 0;start < cs.size()+rowNum;start++,cstart++ ){
                generateRowData(sheet, start, fields, cs.get(cstart), clazz, cellStyle);
            }
        });
    }

    /**
    * @description: cell填充值
    * @author: zbn
    * @create: 17:13 2018/4/3
    * @param: Cell cell, String field, Object rowDataObject, Class<?> clazz
    **/
    public static void cellValueHandle(Cell cell, String field, Object rowDataObject, Class<?> clazz){

        String methodName = GET_METHOD_PREFIX.concat(field.substring(0, 1).toUpperCase()).concat(field.substring(1));
        try {
            Method method = clazz.getMethod(methodName, null);
            if (null != method) { 
                Object invoke = method.invoke(rowDataObject, null);
                if(null != invoke){
                    cell.setCellValue(invoke.toString());
                }
            }
        } catch (Exception e) {
            throw new ExcelException("cellValueHandle exception");
        }
    }

    /**
    * @author: zbn
    * @create: 21:10 2018/4/3
    **/
    public static void excelWirte(List contents, String[] heads, String[] fields, Class<?> clazz, ExcelEnum excelEnum, OutputStream outputStream){
        Workbook workBook = createWorkBook(excelEnum, null);
        if(null != workBook){
            Sheet sheet = workBook.createSheet();
            Row headRow = headRowHandle(sheet, 0, heads, defaultCellStyle(workBook));
            int rowNum = headRow.getRowNum();
            contentHandle(sheet, ++rowNum, contents, fields, clazz, defaultCellStyle(workBook));
            try {
                workBook.write(outputStream);
            } catch (IOException e) {
                throw new ExcelException("excelWirte exception");
            }
        }
    }

    /**
    * @description: 默认样式
    * @author: zbn
    * @create: 22:41 2018/4/3
    **/
    public static CellStyle defaultCellStyle(Workbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

//        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    /**
    * @description: 行头
    * @author: zbn
    * @create: 0:19 2018/4/4
    **/
    public static Row headRowHandle(Sheet sheet, int rowNum, String[] heads, CellStyle cellStyle){
        Row row = sheet.createRow(rowNum);
        for(int start = 0;start < heads.length;start++){
            Cell cell = row.createCell(start);
            cell.setCellValue(heads[start]);
            cellStyle.setFillForegroundColor((short) 13);// 设置背景色
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(start, COLUMN_WIDTH);
        }
        return row;
    }

    public static List<Object> excelRead(InputStream inputStream, Class<?> clazz, ExcelEnum excelEnum, String[] fields){
        Workbook workBook = createWorkBook(excelEnum, inputStream);
        if(null != workBook && null != workBook.getSheetAt(BigDecimal.ZERO.intValue())){
            return excelSheetRead(workBook.getSheetAt(BigDecimal.ZERO.intValue()), 1, clazz, fields, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return new ArrayList<>();
    }

    public static List<Object> excelSheetRead(Sheet sheet, Integer rowNum ,Class<?> clazz, String[] fields,  DateTimeFormatter dateTimeFormatter){
        List<Object> objects = new ArrayList<>();
        for(int start = rowNum; start <= sheet.getLastRowNum(); start++){
            Row row = sheet.getRow(start);
            if(null != row){
                Object o = excelRowRead(row, clazz, fields, dateTimeFormatter);
                if(null != o){
                    objects.add(o);
                }
            }
        }
        return objects;
    }

    /**
    * @description: 读取行数据
    * @author: zbn
    * @create: 18:58 2018/4/6
    **/
    public static Object excelRowRead(Row row, Class<?> clazz, String[] fields,  DateTimeFormatter dateTimeFormatter){
        try {
            Object o = clazz.newInstance();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for(int index = 0; index < fields.length; index++){
                Cell cell = row.getCell(index);
                if(null != cell){
                    excelCellReadHandle(cell, clazz, fields[index], declaredMethods, o, dateTimeFormatter);
                }
            }
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void excelCellReadHandle(Cell cell, Class<?> clazz, String fieldName, Method[] methods, Object o,  DateTimeFormatter dateTimeFormatter) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        String methodName = SET_METHOD_PREFIX.concat(fieldName.substring(0, 1).toUpperCase()).concat(fieldName.substring(1));
        Method method = Arrays.stream(methods).filter(m -> methodName.equals(m.getName())).findAny().orElse(null);
        Objects.requireNonNull(method);

        Object value = valueConvert(cell, clazz, fieldName, dateTimeFormatter);
        if(null != value){
            method.invoke(o, value);
        }
    }

    /**
    * @description: 值转换
    * @author: zbn
    * @create: 21:17 2018/4/6
    **/
    private static Object valueConvert(Cell cell, Class<?> clazz, String fieldName, DateTimeFormatter dateTimeFormatter) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        Objects.requireNonNull(field);
        Class<?> type = field.getType();
        String typeName = type.getTypeName();
        String cellValue = cell.toString();
        if(StringUtils.isBlank(typeName)){
            throw new ExcelException("excelCellReadHandle exception");
        }
        Object value = null;

        if (StringUtils.isBlank(cellValue)){
            return value;
        }
        if (typeName.contains(DOUBLE_TYPE) || type == Double.class){
            value = Double.parseDouble(cellValue);
        } else if (typeName.equals(FLOAT_TYPE) || type == Float.class){
            value = Float.parseFloat(cellValue);
        } else if (typeName.equals(BYTE_TYPE) || type == Byte.class){
            value = Byte.parseByte(cellValue);
        } else if (typeName.equals(SHORT_TYPE) || type == Short.class){
            value = Short.parseShort(cellValue);
        } else if (typeName.equals(INT_TYPE) || type == Integer.class){
            value = Integer.parseInt(cellValue);
        } else if (typeName.equals(LONG_TYPE) || type == Long.class){
            value = Long.parseLong(cellValue);
        } else if (typeName.equals(BOOLEAN_TYPE) || type == Boolean.class){
            value = Boolean.valueOf(cellValue);
        } else if (type == BigDecimal.class){
            value = new BigDecimal(cellValue);
        } else if (type == String.class){
            value = cell.toString();
        } else if (type == Date.class){
            if (null != dateTimeFormatter && String.class.getSimpleName().equalsIgnoreCase(cell.getCellTypeEnum().name())){
                TemporalAccessor ta = dateTimeFormatter.parse(cellValue);
                Instant instant = LocalDateTime.from(ta).atZone(ZoneId.systemDefault()).toInstant();
                value =  Date.from(instant);
            } else {
                value = cell.getDateCellValue();
            }
        }
        return value;
    }


}
