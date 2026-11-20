package lab9;

// Інтерфейс Відвідувача (Visitor)
interface Visitor {
    String visitCompany(Company company);
    String visitDepartment(Department department);
    String visitEmployee(Employee employee);
}

// Інтерфейс Елемента структури
interface OrganizationElement {
    String accept(Visitor visitor);
}

// Сутність: Співробітник
class Employee implements OrganizationElement {
    private String position;
    private double salary;

    public Employee(String position, double salary) {
        this.position = position;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitEmployee(this);
    }
}

// Сутність: Департамент
class Department implements OrganizationElement {
    private String name;
    private Employee[] employees;

    public Department(String name, Employee[] employees) {
        this.name = name;
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public Employee[] getEmployees() {
        return employees;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitDepartment(this);
    }
}

// Сутність: Компанія
class Company implements OrganizationElement {
    private String name;
    private Department[] departments;

    public Company(String name, Department[] departments) {
        this.name = name;
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public Department[] getDepartments() {
        return departments;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitCompany(this);
    }
}

// Конкретний Відвідувач: Зарплатна відомість
class SalaryReportVisitor implements Visitor {

    @Override
    public String visitCompany(Company company) {
        StringBuilder report = new StringBuilder();
        report.append("=== Зарплатна відомість компанії: ").append(company.getName()).append(" ===\n\n");

        for (Department department : company.getDepartments()) {
            report.append(department.accept(this)).append("\n");
        }
        return report.toString();
    }

    @Override
    public String visitDepartment(Department department) {
        StringBuilder report = new StringBuilder();
        double totalDepartmentSalary = 0;

        report.append("--- Департамент: ").append(department.getName()).append(" ---\n");
        for (Employee employee : department.getEmployees()) {
            report.append(employee.accept(this)).append("\n");
            totalDepartmentSalary += employee.getSalary();
        }
        report.append("Загальний фонд оплати департаменту: ").append(totalDepartmentSalary).append(" грн\n");
        return report.toString();
    }

    @Override
    public String visitEmployee(Employee employee) {
        return "  • Позиція: " + employee.getPosition() + " | Зарплата: " + employee.getSalary() + " грн";
    }
}

//  Клієнтський код
public class Main {
    public static void main(String[] args) {
        // Створення співробітників для відділу розробки
        Employee[] devEmployees = {
                new Employee("Senior Java Developer", 120000.0),
                new Employee("Middle QA Engineer", 65000.0)
        };
        Department devDepartment = new Department("Розробка та тестування", devEmployees);

        // Створення співробітників для відділу маркетингу
        Employee[] marketingEmployees = {
                new Employee("Marketing Lead", 75000.0),
                new Employee("SMM Manager", 35000.0)
        };
        Department marketingDepartment = new Department("Маркетинг", marketingEmployees);

        // Створення компанії, що приймає масив департаментів
        Department[] allDepartments = { devDepartment, marketingDepartment };
        Company company = new Company("TechNova Ukraine", allDepartments);

        // Ініціалізація відвідувача для формування репорту
        Visitor salaryReportVisitor = new SalaryReportVisitor();

        // Отримання репорту для всієї компанії
        String companyReport = company.accept(salaryReportVisitor);
        System.out.println(companyReport);

        // Отримання репорту окремо для одного з департаментів
        System.out.println("==========================================");
        System.out.println("ОКРЕМИЙ ЗВІТ ПО ДЕПАРТАМЕНТУ:");
        String singleDepartmentReport = devDepartment.accept(salaryReportVisitor);
        System.out.println(singleDepartmentReport);
    }
}