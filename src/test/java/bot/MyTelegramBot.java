package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.annotations.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class MyTelegramBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        // Проверяем, что сообщение содержит текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));

            if (userMessage.contains("?")) {
                message.setText("Надо подумать, приходи за ответом на след. неделе");
            }else if (userMessage.contains("!")){
                message.setText("Очень интересная мысль! сам додумался?");
            }else {
                message.setText("ну чтож..");
            }

            try {
                execute(message); // Отправляем сообщение
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "sanja_test_bot"; // Укажите имя вашего бота
    }

    @Override
    public String getBotToken() {
        Properties properties = new Properties();
        FileInputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("bot"); // Укажите токен вашего бота
    }
}



