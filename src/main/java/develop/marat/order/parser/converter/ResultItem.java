package develop.marat.order.parser.converter;

public class ResultItem {
    private int id;
    private int amount;
    private String currency;
    private String comment;

    private String fileName;
    private int fileLine;
    private String result;

    public ResultItem(int id, int amount, String currency, String comment, String fileName, int fileLine, String result) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;

        this.fileName = fileName;
        this.fileLine = fileLine;
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"amount\":" + amount +
                ", \"currency\":\"" + currency + "\"" +
                ", \"comment\":\"" + comment + "\"" +
                ", \"filename\":\"" + fileName + "\"" +
                ", \"line\":" + fileLine +
                ", \"result\":\"" + result + "\"" +
                "}";
    }
}
