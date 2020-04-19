package develop.marat.order.parser.parser;

public class ParseItem {
    private final String fileName;
    private final int fileLine;
    private final String parseErrorMessage;

    private final String id;
    private final String amount;
    private final String currency;
    private final String comment;

    private ParseItem(
            String fileName,
            int fileLine,
            String parseErrorMessage,
            String id,
            String amount,
            String currency,
            String comment
    ) {
        this.fileName = fileName;
        this.fileLine = fileLine;
        this.parseErrorMessage = parseErrorMessage;

        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }

    public static ParseItem createSuccess(
            String fileName,
            int fileLine,
            String id,
            String amount,
            String currency,
            String comment
    ) {
        return new ParseItem(fileName, fileLine, null, id, amount, currency, comment);
    }

    public static ParseItem createError(String fileName, int fileLine, String parseErrorMessage) {
        return new ParseItem(fileName, fileLine, parseErrorMessage, null, null, null, null);
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileLine() {
        return fileLine;
    }

    public String getParseErrorMessage() {
        return parseErrorMessage;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getComment() {
        return comment;
    }
}
