package develop.marat.order.parser.parser;

import au.com.bytecode.opencsv.CSVReader;
import develop.marat.order.parser.ExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class CsvParser extends Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvParser.class);

    private static final int COLUMN_COUNT = 4;
    private static final String CSV_PARSE_ERROR_MSG = "Error CSV parsing";

    @Autowired
    public CsvParser(BlockingQueue<ParseItem> queue) {
        super(queue);
    }

    @Override
    protected void execute(String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName), ',', '"')) {
            int lineNumber = 1;
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                ParseItem item;
                if (nextLine.length >= COLUMN_COUNT) {
                    item = ParseItem.createSuccess(fileName, lineNumber, nextLine[0], nextLine[1], nextLine[2], nextLine[3]);
                } else {
                    item = ParseItem.createError(fileName, lineNumber, CSV_PARSE_ERROR_MSG);
                }
                queue.put(item);
                lineNumber++;
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(ExceptionHelper.getStackTrace(e));
        }
    }
}
