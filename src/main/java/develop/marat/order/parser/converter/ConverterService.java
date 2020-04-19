package develop.marat.order.parser.converter;

import develop.marat.order.parser.ExceptionHelper;
import develop.marat.order.parser.parser.ParseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class ConverterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterService.class);

    private static final int TIMEOUT_SECONDS = 1;

    private final BlockingQueue<ParseItem> queue;
    private final ItemConverter converter;

    @Autowired
    public ConverterService(BlockingQueue<ParseItem> queue, ItemConverter converter) {
        this.queue = queue;
        this.converter = converter;
    }

    private void execute() {
        try {
            while (!Thread.currentThread().isInterrupted() || !queue.isEmpty()) {
                ParseItem parseItem = queue.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                if (parseItem != null) {
                    ResultItem resultConvert = converter.convert(parseItem);
                    System.out.println(resultConvert);
                }
            }

        } catch (InterruptedException e) {
            LOGGER.error(ExceptionHelper.getStackTrace(e));
        }
    }

    @Async
    public Future<Void> asyncExecute() {
        execute();
        return new AsyncResult<>(null);
    }
}
