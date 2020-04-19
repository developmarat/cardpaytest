package develop.marat.order.parser;

import develop.marat.order.parser.parser.ParseItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class ApplicationConfig {

    @Bean
    public BlockingQueue<ParseItem> getQueue() {
        return new LinkedBlockingDeque<>();
    }
}
