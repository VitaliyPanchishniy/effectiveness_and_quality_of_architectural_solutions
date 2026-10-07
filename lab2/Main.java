package lab2;

// Інтерфейс продукту (Спільний для всіх соціальних мереж)
interface SocialNetwork {
    void logIn();
    void publishMessage(String message);
    void logOut();
}

// Конкретний продукт для Facebook
class FacebookConnector implements SocialNetwork {
    private String login;
    private String password;

    public FacebookConnector(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public void logIn() {
        System.out.println("Facebook: Авторизація користувача з login '" + this.login + "'...");
    }

    @Override
    public void publishMessage(String message) {
        System.out.println("Facebook: Публікація повідомлення: \"" + message + "\"");
    }

    @Override
    public void logOut() {
        System.out.println("Facebook: Вихід з акаунту.\n");
    }
}

// Конкретний продукт для LinkedIn
class LinkedInConnector implements SocialNetwork {
    private String email;
    private String password;

    public LinkedInConnector(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void logIn() {
        System.out.println("LinkedIn: Авторизація користувача з email '" + this.email + "'...");
    }

    @Override
    public void publishMessage(String message) {
        System.out.println("LinkedIn: Публікація повідомлення: \"" + message + "\"");
    }

    @Override
    public void logOut() {
        System.out.println("LinkedIn: Вихід з акаунту.\n");
    }
}

// Базовий клас Креатора (Фабрика)
abstract class SocialNetworkPublisher {

    // Фабричний метод, який повинні реалізувати підкласи
    public abstract SocialNetwork createSocialNetwork();

    // Базова бізнес-логіка, яка використовує фабричний метод
    public void publish(String message) {
        SocialNetwork network = createSocialNetwork();
        network.logIn();
        network.publishMessage(message);
        network.logOut();
    }
}

// Конкретний креатор для Facebook
class FacebookPublisher extends SocialNetworkPublisher {
    private String login;
    private String password;

    public FacebookPublisher(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public SocialNetwork createSocialNetwork() {
        // Створюємо та повертаємо підключення до Facebook
        return new FacebookConnector(login, password);
    }
}

// Конкретний креатор для LinkedIn
class LinkedInPublisher extends SocialNetworkPublisher {
    private String email;
    private String password;

    public LinkedInPublisher(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public SocialNetwork createSocialNetwork() {
        // Створюємо та повертаємо підключення до LinkedIn
        return new LinkedInConnector(email, password);
    }
}

// Головний клас для демонстрації роботи (Клієнтський код)
public class Main {
    public static void main(String[] args) {

        // Створюємо фабрику для Facebook з параметрами login та password
        SocialNetworkPublisher facebookPublisher = new FacebookPublisher("user_login_123", "qwertyPass");
        // Публікуємо повідомлення
        facebookPublisher.publish("Привіт, світ! Це мій перший пост у Facebook.");

        // Створюємо фабрику для LinkedIn з параметрами email та password
        SocialNetworkPublisher linkedInPublisher = new LinkedInPublisher("user@email.com", "securePass456");
        // Публікуємо повідомлення
        linkedInPublisher.publish("Шукаю нові кар'єрні можливості. Це мій пост у LinkedIn.");
    }
}