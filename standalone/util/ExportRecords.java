package test.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Fernando on 27/10/2017.
 */
public class ExportRecords {

    private FileWriter fileWriter;
    private CSVPrinter printer;

    public ExportRecords(String path) throws IOException {

        fileWriter = new FileWriter(path);
        printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
    }

    public void writeToFile(List<String> record) throws IOException {

        printer.printRecord(record);
    }

    public void close() throws IOException {

        fileWriter.flush();
        fileWriter.close();
        printer.close();
    }

}
