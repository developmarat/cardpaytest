package develop.marat.order.parser;

import develop.marat.order.parser.converter.ConverterService;
import develop.marat.order.parser.parser.CsvParser;
import develop.marat.order.parser.parser.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final int REQUIRED_ARGS_COUNT = 2;

    private CsvParser csvParser;
    private JsonParser jsonParser;
    private ConverterService converterService;

    @Autowired
    public Application(CsvParser csvParser, JsonParser jsonParser, ConverterService converterService) {
        this.csvParser = csvParser;
        this.jsonParser = jsonParser;
        this.converterService = converterService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Override
    public void run(String... args) {
        if (args.length < REQUIRED_ARGS_COUNT) {
            System.out.println("Error! Number of required arguments - " + REQUIRED_ARGS_COUNT);
            return;
        }

        String csvFileName = args[0];
        String jsonFileName = args[1];

        //Запуск парсеров
        Future csvParserResult = csvParser.asyncExecute(csvFileName);
        Future jsonParserResult = jsonParser.asyncExecute(jsonFileName);

        //запуск конвертора
        Future convertServiceResult = converterService.asyncExecute();

        waitParsersIsDone(csvParserResult, jsonParserResult);//ожидание окончания работы парсера

        convertServiceResult.cancel(true);//окончание работы парсера
    }

    private void waitParsersIsDone(Future... parsersFuture) {
        for (Future future : parsersFuture) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(ExceptionHelper.getStackTrace(e));
            }
        }
    }
}

