package lab8;

import java.util.HashMap;
import java.util.Map;

// Абстрактний клас, що задає Шаблонний метод
abstract class DataUpdater {

    // Шаблонний метод final, щоб дочірні класи не могли змінити порядок виконання
    public final Map<String, Object> updateEntity(String entityId, Map<String, Object> incomingData) {
        // Отримання об'єкта
        Object entity = getEntity(entityId);

        // Хук: підготовка або дообробка даних перед валідацією
        incomingData = beforeValidationHook(incomingData);

        // Валідація вихідних даних
        boolean isValid = validateData(incomingData);
        if (!isValid) {
            // Хук при помилці валідації
            onValidationFailedHook(entityId, incomingData);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("status", "Validation Failed");
            return errorResponse;
        }

        // Формування запиту та збереження інформації
        saveEntity(entity, incomingData);

        // Формування відповіді
        Map<String, Object> response = formatResponse(entity);

        // Хук після успішного оновлення
        afterUpdateHook(entityId);

        return response;
    }


    protected Object getEntity(String entityId) {
        System.out.println("[Базовий крок] Отримання об'єкта з БД за ID: " + entityId);
        return new Object();
    }

    protected boolean validateData(Map<String, Object> data) {
        System.out.println("[Базовий крок] Валідація даних за загальною таблицею правил...");
        return true;
    }

    protected void saveEntity(Object entity, Map<String, Object> data) {
        System.out.println("[Базовий крок] Збереження оновлених даних у БД...");
    }

    protected Map<String, Object> formatResponse(Object entity) {
        System.out.println("[Базовий крок] Формування базової відповіді (код 200, status OK)");
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("status", "Success");
        return response;
    }

    // Хуки

    // Хук для модифікації даних перед валідацією (за замовчуванням повертає дані без змін)
    protected Map<String, Object> beforeValidationHook(Map<String, Object> data) {
        return data;
    }

    // Хук, який викликається при непровалиданні (по дефолту нічого не робить)
    protected void onValidationFailedHook(String entityId, Map<String, Object> data) {
        // За замовчуванням порожньо
    }

    // Хук після успішного завершення всього процесу
    protected void afterUpdateHook(String entityId) {
        // За замовчуванням порожньо
    }
}

// Товар
class ProductDataUpdater extends DataUpdater {

    @Override
    protected boolean validateData(Map<String, Object> data) {
        boolean baseValid = super.validateData(data);
        // якщо ціна від'ємна або відсутня — валідація не пройдена
        if (data.containsKey("price") && (double) data.get("price") < 0) {
            return false;
        }
        return baseValid;
    }

    // при помилці валідації надсилати сповіщення адміністратору в месенджер
    @Override
    protected void onValidationFailedHook(String entityId, Map<String, Object> data) {
        System.out.println("⚠️ [Product Hook] Помилка валідації! Надсилаємо сповіщення адміністратору в месенджер...");
    }
}

// Користувач
class UserDataUpdater extends DataUpdater {

    // заборонено змінювати email
    @Override
    protected Map<String, Object> beforeValidationHook(Map<String, Object> incomingData) {
        if (incomingData.containsKey("email")) {
            System.out.println("🔒 [User Hook] Виявлено спробу зміни email. Видаляємо поле email з запиту!");
            Map<String, Object> filteredData = new HashMap<>(incomingData);
            filteredData.remove("email");
            return filteredData;
        }
        return incomingData;
    }
}

// Замовлення
class OrderDataUpdater extends DataUpdater {

    // повертати у відповіді не лише код і статус, а й JSON-подання сутності
    @Override
    protected Map<String, Object> formatResponse(Object entity) {
        Map<String, Object> response = super.formatResponse(entity);
        response.put("jsonBody", "{\"orderId\": \"ORD-123\", \"total\": 1250.00, \"items\": [\"Item A\", \"Item B\"]}");
        System.out.println("📦 [Order] Додано JSON-подання сутності Замовлення до відповіді.");
        return response;
    }
}

// Демонстрація виконання
public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Оновлення Товару (Тест помилки валідації) ===");
        DataUpdater productUpdater = new ProductDataUpdater();
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", "Ноутбук");
        productData.put("price", -500.0); // Некоректна ціна
        productUpdater.updateEntity("PROD-001", productData);

        System.out.println("\n=== 2. Оновлення Користувача (Спроба змінити email) ===");
        DataUpdater userUpdater = new UserDataUpdater();
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Іван");
        userData.put("email", "new_email@example.com"); // Заборонене поле
        userUpdater.updateEntity("USER-042", userData);

        System.out.println("\n=== 3. Оновлення Замовлення (Отримання JSON-відповіді) ===");
        DataUpdater orderUpdater = new OrderDataUpdater();
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("status", "SHIPPED");
        Map<String, Object> response = orderUpdater.updateEntity("ORD-123", orderData);
        System.out.println("Результат відповіді: " + response);
    }
}