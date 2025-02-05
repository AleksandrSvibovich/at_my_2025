package bot;

import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.xml.transform.Source;

public class MyBotSession implements BotSession {
    private BotOptions botOptions;
    private String token;
    private LongPollingBot longPollingBot;
    private boolean isActive = false;

    @Override
    public void setOptions(BotOptions botOptions) {
        this.botOptions = botOptions;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setCallback(LongPollingBot longPollingBot) {
        this.longPollingBot = longPollingBot;
    }

    @Override
    public void start() {
        isActive = true;
    }

    @Override
    public void stop() {
        isActive = false;
        System.out.println("Bot stopped");
    }

    @Override
    public boolean isRunning() {
        return isActive;
    }
}
