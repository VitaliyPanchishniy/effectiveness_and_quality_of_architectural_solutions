package lab6;

import java.util.HashMap;
import java.util.Map;

// Спільний інтерфейс
interface Downloader {
    String download(String url);
}

class SimpleDownloader implements Downloader {
    @Override
    public String download(String url) {
        // Умовна симуляція завантаження з мережі
        System.out.println("[SimpleDownloader] Завантаження даних з URL: " + url);
        return "Вміст файлу з " + url;
    }
}

// Замісник з кешуванням
class CachingDownloaderProxy implements Downloader {
    private SimpleDownloader downloader;
    private Map<String, String> cache;

    public CachingDownloaderProxy() {
        this.downloader = new SimpleDownloader();
        this.cache = new HashMap<>();
    }

    @Override
    public String download(String url) {
        if (cache.containsKey(url)) {
            System.out.println("[CachingDownloaderProxy] Повернення даних з кешу для URL: " + url);
            return cache.get(url);
        }

        System.out.println("[CachingDownloaderProxy] Дані відсутні в кеші. Виклик реального завантажувача...");
        String data = downloader.download(url);
        cache.put(url, data);
        return data;
    }
}

public class Main {
    public static void main(String[] args) {
        // Використовуємо Proxy через спільний інтерфейс Downloader
        Downloader downloader = new CachingDownloaderProxy();

        String fileUrl1 = "https://example.com/file1.zip";
        String fileUrl2 = "https://example.com/file2.png";

        System.out.println("--- Перший запит file1.zip ---");
        String result1 = downloader.download(fileUrl1);
        System.out.println("Результат: " + result1 + "\n");

        System.out.println("--- Повторний запит file1.zip (має взяти з кешу) ---");
        String result2 = downloader.download(fileUrl1);
        System.out.println("Результат: " + result2 + "\n");

        System.out.println("--- Перший запит file2.png ---");
        String result3 = downloader.download(fileUrl2);
        System.out.println("Результат: " + result3 + "\n");

        System.out.println("--- Повторний запит file2.png (має взяти з кешу) ---");
        String result4 = downloader.download(fileUrl2);
        System.out.println("Результат: " + result4);
    }
}