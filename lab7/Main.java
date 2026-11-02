package lab7;

// Інтерфейс стратегії розрахунку доставки
interface DeliveryStrategy {
    double calculateCost(double orderAmount, double distanceKm);
}

// Самовивіз безкоштовно
class SelfPickupStrategy implements DeliveryStrategy {
    @Override
    public double calculateCost(double orderAmount, double distanceKm) {
        return 0.0;
    }
}

// Доставка зовнішньою службою (наприклад, кур'єрська служба-партнер)
class ExternalDeliveryStrategy implements DeliveryStrategy {
    @Override
    public double calculateCost(double orderAmount, double distanceKm) {
        // Базовий тариф зовнішньої служби + оплата за кожний км
        double baseFee = 50.0;
        double pricePerKm = 15.0;
        return baseFee + (distanceKm * pricePerKm);
    }
}

// Доставка власною службою
class InternalDeliveryStrategy implements DeliveryStrategy {
    @Override
    public double calculateCost(double orderAmount, double distanceKm) {
        // Безкоштовна доставка для замовлень від 500 грн, інакше фіксований тариф
        if (orderAmount >= 500.0) {
            return 0.0;
        }
        return 40.0 + (distanceKm * 10.0);
    }
}

// Замовлення їжі
class OrderContext {
    private DeliveryStrategy deliveryStrategy;
    private double orderAmount;
    private double distanceKm;

    public OrderContext(double orderAmount, double distanceKm) {
        this.orderAmount = orderAmount;
        this.distanceKm = distanceKm;
    }

    // Дозволяє змінювати стратегію доставки під час виконання програми
    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    // Метод для розрахунку підсумкової вартості доставки
    public double calculateDeliveryCost() {
        if (deliveryStrategy == null) {
            throw new IllegalStateException("Стратегію доставки не обрано!");
        }
        return deliveryStrategy.calculateCost(orderAmount, distanceKm);
    }
}

// Демонстрація роботи програми
public class Main {
    public static void main(String[] args) {
        double orderAmount = 600.0; // Сума замовлення у грн
        double distance = 5.0;       // Відстань у км

        OrderContext order = new OrderContext(orderAmount, distance);

        System.out.println("Сума замовлення: " + orderAmount + " грн, Відстань: " + distance + " км\n");

        // Самовивіз
        order.setDeliveryStrategy(new SelfPickupStrategy());
        System.out.println("Вартість (Самовивіз): " + order.calculateDeliveryCost() + " грн");

        // Зовнішня служба доставки
        order.setDeliveryStrategy(new ExternalDeliveryStrategy());
        System.out.println("Вартість (Зовнішня доставка): " + order.calculateDeliveryCost() + " грн");

        // Власна служба доставки
        order.setDeliveryStrategy(new InternalDeliveryStrategy());
        System.out.println("Вартість (Власна доставка): " + order.calculateDeliveryCost() + " грн");
    }
}