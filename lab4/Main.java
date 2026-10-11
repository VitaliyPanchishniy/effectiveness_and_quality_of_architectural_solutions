package lab4;

// Цільовий інтерфейс системи, який ми не можемо змінювати (Target)
interface Notification {
    void send(String title, String message);
}

// Існуючий клас для Email-сповіщень
class EmailNotification implements Notification {
    private String adminEmail;

    public EmailNotification(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    @Override
    public void send(String title, String message) {
        // У PHP використовувалась функція mail(), тут просто виводимо в консоль
        System.out.println("Email: Відправлено лист з темою '" + title +
                "' на адресу '" + this.adminEmail + "'. Повідомлення: '" + message + "'.");
    }
}

class SlackApi {
    private String login;
    private String apiKey;

    public SlackApi(String login, String apiKey) {
        this.login = login;
        this.apiKey = apiKey;
    }

    public void logIn() {
        System.out.println("Slack API: Успішна авторизація з login '" + login + "' та apiKey '***'.");
    }

    public void sendMessage(String chatId, String formattedMessage) {
        System.out.println("Slack API: Відправка повідомлення у чат [" + chatId + "]: " + formattedMessage);
    }
}

// Адаптер для Slack
class SlackNotificationAdapter implements Notification {
    private SlackApi slackApi;
    private String chatId;

    // Конструктор адаптера отримує всі необхідні специфічні дані
    public SlackNotificationAdapter(String login, String apiKey, String chatId) {
        this.slackApi = new SlackApi(login, apiKey);
        this.chatId = chatId;
    }

    @Override
    public void send(String title, String message) {
        // Спочатку необхідно авторизуватись у Slack
        slackApi.logIn();

        // Адаптуємо параметри title і message в один формат, який вимагає Slack
        String slackMessage = "Тема: *" + title + "*\n" + message;

        // Відправляємо повідомлення за допомогою стороннього сервісу
        slackApi.sendMessage(this.chatId, slackMessage);
    }
}

class SmsApi {
    public void dispatchSms(String phone, String senderName, String text) {
        System.out.println("SMS API: Відправка від [" + senderName + "] на номер " + phone + ". Текст: '" + text + "'");
    }
}

// Адаптер для SMS
class SmsNotificationAdapter implements Notification {
    private SmsApi smsApi;
    private String phone;
    private String sender;

    public SmsNotificationAdapter(String phone, String sender) {
        this.smsApi = new SmsApi();
        this.phone = phone;
        this.sender = sender;
    }

    @Override
    public void send(String title, String message) {
        // Оскільки в СМС зазвичай немає окремого поля "title", адаптуємо дані, зливаючи їх в один текст
        String text = title + ". " + message;
        smsApi.dispatchSms(this.phone, this.sender, text);
    }
}

// 5. Клієнтський код
public class Main {

    // Клієнтський метод, який працює тільки з інтерфейсом Notification
    public static void clientCode(Notification notification) {
        // Клієнтський код не знає, яка саме система відправить повідомлення (Email, Slack чи SMS)
        notification.send("Тривога системи", "Сервер перевантажений на 99%!");
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {

        // Стандартна відправка Email
        Notification emailNotification = new EmailNotification("admin@example.com");
        clientCode(emailNotification);

        // Відправка через Slack за допомогою Адаптера
        Notification slackNotification = new SlackNotificationAdapter("admin_login", "api_key_12345", "dev_chat_id");
        clientCode(slackNotification);

        // Відправка через SMS за допомогою Адаптера
        Notification smsNotification = new SmsNotificationAdapter("+3801234567899", "InfoSystem");
        clientCode(smsNotification);
    }
}