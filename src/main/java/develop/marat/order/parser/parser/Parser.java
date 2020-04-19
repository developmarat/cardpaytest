package develop.marat.order.parser.parser;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

public abstract class Parser {
    protected final BlockingQueue<ParseItem> queue;

    public Parser(BlockingQueue<ParseItem> queue) {
        this.queue = queue;
    }

    protected abstract void execute(String fileName);

    @Async
    public Future<Void> asyncExecute(String fileName) {
        execute(fileName);
        return new AsyncResult<>(null);
    }
}
