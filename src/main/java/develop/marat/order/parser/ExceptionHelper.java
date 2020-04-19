package develop.marat.order.parser;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHelper {
    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
