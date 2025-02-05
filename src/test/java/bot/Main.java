package bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

public class Main {
    public static void main(String[] args) {


        // Создаем объект TelegramBotsApi с использованием стандартного конструктора
        TelegramBotsApi botsApi = null;
        try {

            botsApi = new TelegramBotsApi(MyBotSession.class);
            BotSession botSession = botsApi.registerBot(new MyTelegramBot());
            System.out.println(botSession.isRunning());


        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


    }
}

