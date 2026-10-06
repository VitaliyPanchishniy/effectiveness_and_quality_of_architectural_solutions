package lab1;

// Базовий інтерфейс для всіх сховищ
interface IStorage {
    void uploadFile(String userId, String fileName, String fileData);
    String downloadFile(String userId, String fileName);
}

// Реалізація локального сховища як Одинака (Singleton)
class LocalStorage implements IStorage {

    // Приватний конструктор: забороняє пряме створення через new
    private LocalStorage() {}

    // Внутрішній статичний клас для лінивої та потокобезпечної ініціалізації
    private static class Holder {
        private static final LocalStorage INSTANCE = new LocalStorage();
    }

    public static LocalStorage getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void uploadFile(String userId, String fileName, String fileData) {
        // Логіка збереження на локальний диск
    }

    @Override
    public String downloadFile(String userId, String fileName) {
        // Логіка читання з локального диска
        return "local_file_content_placeholder";
    }
}

// Реалізація сховища Amazon S3 як Одинака (Singleton)
class AmazonS3Storage implements IStorage {

    // Приватний конструктор
    private AmazonS3Storage() {}

    // Внутрішній статичний клас для створення екземпляра
    private static class Holder {
        private static final AmazonS3Storage INSTANCE = new AmazonS3Storage();
    }

    public static AmazonS3Storage getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void uploadFile(String userId, String fileName, String fileData) {
        // Логіка відправки даних на AWS S3
    }

    @Override
    public String downloadFile(String userId, String fileName) {
        // Логіка отримання даних з AWS S3
        return "s3_file_content_placeholder";
    }
}

// Клас користувача
class User {
    private final String id;
    private IStorage storage;

    public User(String userId, IStorage selectedStorage) {
        this.id = userId;
        this.storage = selectedStorage;
    }

    public void setStorage(IStorage newStorage) {
        this.storage = newStorage;
    }

    public void saveFile(String fileName, String data) {
        if (storage != null) {
            storage.uploadFile(id, fileName, data);
        }
    }

    public String getFile(String fileName) {
        if (storage != null) {
            return storage.downloadFile(id, fileName);
        }
        return "";
    }
}

public class Main {
    public static void main(String[] args) {
        // Отримання єдиних екземплярів сховищ
        IStorage local = LocalStorage.getInstance();
        IStorage s3 = AmazonS3Storage.getInstance();

        // Створення користувачів з окремо призначеними сховищами
        User user1 = new User("user_001", local);
        User user2 = new User("user_002", s3);

        user1.saveFile("document.txt", "Hello Local Storage!");
        user2.saveFile("avatar.png", "Image Data");
    }
}