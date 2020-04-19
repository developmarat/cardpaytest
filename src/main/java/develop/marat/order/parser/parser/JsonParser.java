package develop.marat.order.parser.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.marat.order.parser.ExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class JsonParser extends Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);

    private static final String JSON_PARSE_ERROR_MSG = "Error JSON parsing";

    private static final String ORDER_ID_COLUMN = "orderId";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String CURRENCY_COLUMN = "currency";
    private static final String COMMENT_COLUMN = "comment";

    @Autowired
    public JsonParser(BlockingQueue<ParseItem> queue) {
        super(queue);
    }

    @Override
    protected void execute(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                ParseItem item = createItemFromJsonStr(fileName, lineNumber, line);
                queue.put(item);
                lineNumber++;
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.error(ExceptionHelper.getStackTrace(e));
        }

    }

    private ParseItem createItemFromJsonStr(String fileName, int lineNumber, String jsonStr) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(jsonStr);
            return ParseItem.createSuccess(
                    fileName,
                    lineNumber,
                    node.get(ORDER_ID_COLUMN) != null ? node.get(ORDER_ID_COLUMN).asText() : null,
                    node.get(AMOUNT_COLUMN) != null ? node.get(AMOUNT_COLUMN).asText() : null,
                    node.get(AMOUNT_COLUMN) != null ? node.get(CURRENCY_COLUMN).asText() : null,
                    node.get(COMMENT_COLUMN) != null ? node.get(COMMENT_COLUMN).asText() : null
            );
        } catch (IOException e) {
            LOGGER.error(ExceptionHelper.getStackTrace(e));
            return ParseItem.createError(fileName, lineNumber, JSON_PARSE_ERROR_MSG);
        }
    }
}

