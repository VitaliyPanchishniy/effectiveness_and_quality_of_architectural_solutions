package lab3;

// Спільний інтерфейс Будівельника запитів
interface SQLQueryBuilder {
    SQLQueryBuilder select(String... fields);

    SQLQueryBuilder where(String condition);

    SQLQueryBuilder limit(int count);

    String getSQL();
}

//  Конкретний будівельник для MySQL
class MySQLQueryBuilder implements SQLQueryBuilder {

    @Override
    public SQLQueryBuilder select(String... fields) {
        return this;
    }

    @Override
    public SQLQueryBuilder where(String condition) {
        return this;
    }

    @Override
    public SQLQueryBuilder limit(int count) {
        return this;
    }

    @Override
    public String getSQL() {
        // Збирання всіх частин у єдиний рядок
        return "SELECT [fields] FROM [table] WHERE [condition] LIMIT [count]; (MySQL dialect)";
    }
}

// Конкретний будівельник для PostgreSQL
class PostgreSQLQueryBuilder implements SQLQueryBuilder {

    @Override
    public SQLQueryBuilder select(String... fields) {
        return this;
    }

    @Override
    public SQLQueryBuilder where(String condition) {
        return this;
    }

    @Override
    public SQLQueryBuilder limit(int count) {
        return this;
    }

    @Override
    public String getSQL() {
        // Збирання всіх частин у єдиний рядок
        return "SELECT [fields] FROM [table] WHERE [condition] LIMIT [count]; (PostgreSQL dialect)";
    }
}

// Клієнтський код
public class Main {
    public static void main(String[] args) {

        // Робота з MySQL
        SQLQueryBuilder mysqlBuilder = new MySQLQueryBuilder();

        String mysqlQuery = mysqlBuilder
                .select("id", "name", "email")
                .where("age > 18")
                .limit(10)
                .getSQL();

        System.out.println("Згенерований запит MySQL:");
        System.out.println(mysqlQuery);
        System.out.println();

        // Робота з PostgreSQL
        SQLQueryBuilder pgBuilder = new PostgreSQLQueryBuilder();

        String pgQuery = pgBuilder
                .select("id", "created_at")
                .where("status = 'ACTIVE'")
                .limit(50)
                .getSQL();

        System.out.println("Згенерований запит PostgreSQL:");
        System.out.println(pgQuery);
    }
}