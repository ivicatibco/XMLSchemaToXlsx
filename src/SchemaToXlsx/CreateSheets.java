package SchemaToXlsx;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateSheets {

    private final String fileName;
    private boolean debug = false;
    private final String schemaFileName = "/Users/ivisuker/LHC/SVN/ESB-AMX/Services/trunk/_Model/ServiceRepository/ESBBusinessObjects/Sales_1_0_0/Quotation/QuotationTransferObjects_1_1_0/QuotationTransferObjects_1_1_0.xsd";
    private ArrayList<Operation> operationList;
    private XSSFSheet sheet;
    private XSSFWorkbook wb;

    private CreateSheets() {
        fileName = schemaFileName.substring(schemaFileName.lastIndexOf("/") + 1, schemaFileName.length() - 3) + "xlsx";

        wb = new XSSFWorkbook();

        GetAllElements elements = new GetAllElements(schemaFileName, debug);
        operationList = elements.getOperationList();

        for (Operation operation : operationList) {
            if (debug) {
                System.out.println(getLineNumber() + "\t" + operation.getName());
            }
            createSheet(operation);
        }

        closeExcel();
    }

    /*****************
     *  main Method  *
     *****************/

    public static void main(String[] args) {
        new CreateSheets();
    }

    /*******************
     *  static methods
     *******************/

    // returns current line number
    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    /*************
     *  Methods  *
     *************/

    private void createSheet(Operation operation) {
        String operationName;
        if (operation.getName().length() > 31) {
            //System.out.println(getLineNumber() + "\t" + operation.getName().substring(operation.getName().length()-30,operation.getName().length()));
            operationName = operation.getName().substring(operation.getName().length() - 31);
        } else {
            operationName = operation.getName();
        }
        try {
            sheet = wb.createSheet(operationName);
        } catch (java.lang.IllegalArgumentException e) {
            int count = 1;
            while (true) {
                try {
                    operationName = operationName.substring(0, 28) + count;
                    sheet = wb.createSheet(operationName);
                    break;
                } catch (java.lang.IllegalArgumentException f) {
                    if (++count > 9) {
                        throw e;
                    }
                }
            }
        }

        createHeader();

        int rowNumber = 1;
        ArrayList<Integer> reference = new ArrayList<>();

        for (SchemaElement element : operation.getElements()) {
            int slashes = getNumberOfSlashes(element.getName());
            int size = reference.size();

            if (slashes == 0) {
                reference.add(1);
            } else if (slashes + 1 == size) {
                reference.set(size - 1, reference.get(size - 1) + 1);
            } else if (slashes + 1 > size) {
                reference.add(1);
            } else if (slashes + 1 < size) {
                reference.set(slashes, reference.get(slashes) + 1);
                reference = new ArrayList<>(reference.subList(0, slashes + 1));
            } else {
                System.out.println(slashes + " " + reference.size());
            }

            element.setReference(formatReference(reference));

            addSchemaElement(element, rowNumber++);
        }

    }

    private void addSchemaElement(SchemaElement element, int rowNumber) {
        Row row = sheet.createRow(rowNumber);

        Cell cell0 = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        Cell cell3 = row.createCell(3);
        Cell cell4 = row.createCell(4);
        Cell cell5 = row.createCell(5);
        Cell cell6 = row.createCell(6);

        cell0.setCellValue(element.getReference());
        cell1.setCellValue(element.getElementName());
        cell2.setCellValue(element.getMinOccurs() + ".." + element.getMaxOccurs());
        cell3.setCellValue(element.getNecessity());
        cell4.setCellValue(element.getSimpleType());
        cell5.setCellValue(element.getRestrictions());
        cell6.setCellValue(element.getDescription());

    }

    private void createHeader() {
        Row row = sheet.createRow(0);

        Cell cell0 = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        Cell cell3 = row.createCell(3);
        Cell cell4 = row.createCell(4);
        Cell cell5 = row.createCell(5);
        Cell cell6 = row.createCell(6);

        cell0.setCellValue("Reference");
        cell1.setCellValue("Element");
        cell2.setCellValue("Occurrence");
        cell3.setCellValue("M/O/C");
        cell4.setCellValue("Data Type");
        cell5.setCellValue("valid Values");
        cell6.setCellValue("Description");
    }

    private void closeExcel() {
        try {
            FileOutputStream fileOut = new FileOutputStream(new File(fileName));
            wb.write(fileOut);
            fileOut.close();
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            System.exit(0);
        }

        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }

    private int getNumberOfSlashes(String string) {
        return string.length() - string.replace("/", "").length();
    }

    private String formatReference(ArrayList<Integer> reference) {
        StringBuilder returnString = new StringBuilder();
        for (int i : reference) {
            if (returnString.length() == 0) {
                returnString = new StringBuilder(Integer.toString(i));
            } else {
                returnString.append(".").append(Integer.toString(i));
            }
        }

        return returnString.toString();
    }

}
