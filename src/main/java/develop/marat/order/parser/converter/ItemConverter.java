package develop.marat.order.parser.converter;

import develop.marat.order.parser.parser.ParseItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ItemConverter implements Converter<ParseItem, ResultItem> {
    private static final String RESULT_OK_MESSAGE = "OK";
    private static final String NO_REQUIRED_FIELDS_MESSAGE = "Required fields are not filled";

    private static final int ID_DEFAULT = 0;
    private static final int AMOUNT_DEFAULT = 0;


    @Override
    public ResultItem convert(ParseItem parseItem) {
        if (parseItem == null) {
            return null;
        }

        if (parseItem.getParseErrorMessage() == null) {
            if (hasRequiredFields(parseItem)) {
                return createSuccessResult(parseItem);
            } else {
                return createErrorResult(parseItem, NO_REQUIRED_FIELDS_MESSAGE);
            }
        } else {
            return createErrorResult(parseItem, parseItem.getParseErrorMessage());
        }
    }

    private boolean hasRequiredFields(ParseItem parseItem) {
        return parseItem.getId() != null
                && parseItem.getAmount() != null
                && parseItem.getCurrency() != null
                && parseItem.getFileName() != null;
    }


    private ResultItem createSuccessResult(ParseItem parseItem) {
        try {
            int id = Integer.parseInt(parseItem.getId());
            int amount = Integer.parseInt(parseItem.getAmount());

            return new ResultItem(
                    id,
                    amount,
                    parseItem.getCurrency(),
                    parseItem.getComment(),

                    parseItem.getFileName(),
                    parseItem.getFileLine(),
                    RESULT_OK_MESSAGE
            );
        } catch (NumberFormatException e) {
            return createErrorResult(parseItem, e.getMessage());
        }
    }


    private ResultItem createErrorResult(ParseItem parseItem, String errorMessage) {
        return new ResultItem(
                obtainId(parseItem),
                obtainAmount(parseItem),
                parseItem.getCurrency(),
                parseItem.getComment(),

                parseItem.getFileName(),
                parseItem.getFileLine(),
                errorMessage
        );
    }

    private int obtainId(ParseItem parseItem) {
        try {
            return Integer.parseInt(parseItem.getId());
        } catch (NumberFormatException e) {
            return ID_DEFAULT;
        }
    }

    private int obtainAmount(ParseItem parseItem) {
        try {
            return Integer.parseInt(parseItem.getAmount());
        } catch (NumberFormatException e) {
            return AMOUNT_DEFAULT;
        }
    }
}
