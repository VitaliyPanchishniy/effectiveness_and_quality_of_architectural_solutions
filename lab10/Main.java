package lab10;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Інтерфейс Посередника
interface OrderFormMediator {
    void notify(FormComponent sender, String event);
}

// Базовий клас компонента форми
abstract class FormComponent {
    protected OrderFormMediator mediator;
    protected boolean isEnabled = true;
    protected boolean isVisible = true;
    protected boolean isRequired = false;

    public void setMediator(OrderFormMediator mediator) {
        this.mediator = mediator;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void setRequired(boolean required) {
        this.isRequired = required;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isRequired() {
        return isRequired;
    }
}


// Вибір дати доставки
class DatePicker extends FormComponent {
    private String selectedDate;

    public void selectDate(String date) {
        this.selectedDate = date;
        System.out.println("[UI] Обрано дату доставки: " + date);
        if (mediator != null) {
            mediator.notify(this, "dateChanged");
        }
    }

    public String getSelectedDate() {
        return selectedDate;
    }
}

// Список доступних проміжків часу
class TimeSlotSelector extends FormComponent {
    private List<String> availableSlots = Collections.emptyList();

    public void updateAvailableSlots(List<String> slots) {
        this.availableSlots = slots;
        System.out.println("[UI] Оновлено доступні часові слоти: " + slots);
    }
}

// Чекбокс "Отримувач інша особа"
class AnotherRecipientCheckbox extends FormComponent {
    private boolean isChecked = false;

    public void setChecked(boolean checked) {
        this.isChecked = checked;
        System.out.println("[UI] Чекбокс 'Отримувач інша особа': " + checked);
        if (mediator != null) {
            mediator.notify(this, "anotherRecipientToggled");
        }
    }

    public boolean isChecked() {
        return isChecked;
    }
}

// Текстове поле для Імені та Телефону отримувача
class TextField extends FormComponent {
    private String fieldName;
    private String value = "";

    public TextField(String fieldName) {
        this.fieldName = fieldName;
        this.isVisible = false;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void updateState(boolean visible, boolean required) {
        setVisible(visible);
        setRequired(required);
        System.out.println("[UI] Поле '" + fieldName + "' -> видиме: " + visible + ", обов'язкове: " + required);
    }
}

// Чекбокс "Самовивіз з магазину"
class SelfPickupCheckbox extends FormComponent {
    private boolean isChecked = false;

    public void setChecked(boolean checked) {
        this.isChecked = checked;
        System.out.println("[UI] Чекбокс 'Самовивіз з магазину': " + checked);
        if (mediator != null) {
            mediator.notify(this, "selfPickupToggled");
        }
    }

    public boolean isChecked() {
        return isChecked;
    }
}

// Конкретний Посередник
class FlowerOrderMediator implements OrderFormMediator {
    private DatePicker datePicker;
    private TimeSlotSelector timeSlotSelector;
    private AnotherRecipientCheckbox anotherRecipientCheckbox;
    private TextField recipientNameField;
    private TextField recipientPhoneField;
    private SelfPickupCheckbox selfPickupCheckbox;

    public FlowerOrderMediator(
            DatePicker datePicker,
            TimeSlotSelector timeSlotSelector,
            AnotherRecipientCheckbox anotherRecipientCheckbox,
            TextField recipientNameField,
            TextField recipientPhoneField,
            SelfPickupCheckbox selfPickupCheckbox) {

        this.datePicker = datePicker;
        this.timeSlotSelector = timeSlotSelector;
        this.anotherRecipientCheckbox = anotherRecipientCheckbox;
        this.recipientNameField = recipientNameField;
        this.recipientPhoneField = recipientPhoneField;
        this.selfPickupCheckbox = selfPickupCheckbox;

        // Реєстрація посередника в усіх компонентах
        this.datePicker.setMediator(this);
        this.timeSlotSelector.setMediator(this);
        this.anotherRecipientCheckbox.setMediator(this);
        this.recipientNameField.setMediator(this);
        this.recipientPhoneField.setMediator(this);
        this.selfPickupCheckbox.setMediator(this);
    }

    @Override
    public void notify(FormComponent sender, String event) {
        // Зміна дати доставки -> оновлення доступних інтервалів часу
        if (sender == datePicker && "dateChanged".equals(event)) {
            handleDateChange(datePicker.getSelectedDate());
        }

        //  Перемикання "Отримувач інша особа" -> показ та обов'язковість полів Ім'я та Телефон
        if (sender == anotherRecipientCheckbox && "anotherRecipientToggled".equals(event)) {
            boolean isOther = anotherRecipientCheckbox.isChecked();
            recipientNameField.updateState(isOther, isOther);
            recipientPhoneField.updateState(isOther, isOther);
        }

        // Перемикання "Самовивіз" -> деактивація всіх елементів доставки
        if (sender == selfPickupCheckbox && "selfPickupToggled".equals(event)) {
            boolean isDeliveryActive = !selfPickupCheckbox.isChecked();
            datePicker.setEnabled(isDeliveryActive);
            timeSlotSelector.setEnabled(isDeliveryActive);
            anotherRecipientCheckbox.setEnabled(isDeliveryActive);
            recipientNameField.setEnabled(isDeliveryActive);
            recipientPhoneField.setEnabled(isDeliveryActive);

            System.out.println("[Mediator] Елементи доставки активні: " + isDeliveryActive);
        }
    }

    private void handleDateChange(String date) {
        if ("2026-11-21".equals(date)) { // Наприклад, сьогодні доступні лише вечірні слоти
            timeSlotSelector.updateAvailableSlots(Arrays.asList("18:00 - 20:00", "20:00 - 22:00"));
        } else {
            timeSlotSelector.updateAvailableSlots(Arrays.asList("09:00 - 12:00", "12:00 - 15:00", "15:00 - 18:00"));
        }
    }
}

// Клієнтський код
public class Main {
    public static void main(String[] args) {
        // Ініціалізація елементів форми
        DatePicker datePicker = new DatePicker();
        TimeSlotSelector timeSlotSelector = new TimeSlotSelector();
        AnotherRecipientCheckbox anotherRecipientCheckbox = new AnotherRecipientCheckbox();
        TextField recipientName = new TextField("Ім'я отримувача");
        TextField recipientPhone = new TextField("Телефон отримувача");
        SelfPickupCheckbox selfPickupCheckbox = new SelfPickupCheckbox();

        // Створення посередника, який зв'язує всі елементи
        OrderFormMediator mediator = new FlowerOrderMediator(
                datePicker, timeSlotSelector, anotherRecipientCheckbox,
                recipientName, recipientPhone, selfPickupCheckbox
        );

        System.out.println("--- Сценарій 1: Вибір дати доставки ---");
        datePicker.selectDate("2026-11-21");
        datePicker.selectDate("2026-11-25");

        System.out.println("\n--- Сценарій 2: Отримувач інша особа (true -> false) ---");
        anotherRecipientCheckbox.setChecked(true);
        anotherRecipientCheckbox.setChecked(false);

        System.out.println("\n--- Сценарій 3: Вибір самовивозу з магазину ---");
        selfPickupCheckbox.setChecked(true);
        System.out.println("Статус поля дати (isEnabled): " + datePicker.isEnabled());
        System.out.println("Статус вибору часу (isEnabled): " + timeSlotSelector.isEnabled());
        System.out.println("Статус чекбоксу отримувача (isEnabled): " + anotherRecipientCheckbox.isEnabled());
    }
}