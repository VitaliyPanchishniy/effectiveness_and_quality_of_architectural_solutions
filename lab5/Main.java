package lab5;

//Product
class Product {
    private String id;
    private String title;
    private String description;
    private String imageUrl;

    public Product(String id, String title, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

// Інтерфейс Реалізації
interface Renderer {
    String renderTitle(String title);
    String renderTextBlock(String text);
    String renderImage(String url);
    String renderField(String key, String value);
}

// Конкретні реалізації Renderer
class HTMLRenderer implements Renderer {
    @Override
    public String renderTitle(String title) {
        return "<h1>" + title + "</h1>";
    }

    @Override
    public String renderTextBlock(String text) {
        return "<p>" + text + "</p>";
    }

    @Override
    public String renderImage(String url) {
        return "<img src=\"" + url + "\" />";
    }

    @Override
    public String renderField(String key, String value) {
        return "<div><strong>" + key + ":</strong> " + value + "</div>";
    }
}

class JsonRenderer implements Renderer {
    @Override
    public String renderTitle(String title) {
        return "\"title\": \"" + title + "\"";
    }

    @Override
    public String renderTextBlock(String text) {
        return "\"content\": \"" + text + "\"";
    }

    @Override
    public String renderImage(String url) {
        return "\"imageUrl\": \"" + url + "\"";
    }

    @Override
    public String renderField(String key, String value) {
        return "\"" + key + "\": \"" + value + "\"";
    }
}

class XmlRenderer implements Renderer {
    @Override
    public String renderTitle(String title) {
        return "<title>" + title + "</title>";
    }

    @Override
    public String renderTextBlock(String text) {
        return "<content>" + text + "</content>";
    }

    @Override
    public String renderImage(String url) {
        return "<image>" + url + "</image>";
    }

    @Override
    public String renderField(String key, String value) {
        return "<" + key + ">" + value + "</" + key + ">";
    }
}

// Абстракція (Abstraction)
abstract class Page {
    protected Renderer renderer;

    public Page(Renderer renderer) {
        this.renderer = renderer;
    }

    public abstract String view();
}

// Розширені абстракції Page
class SimplePage extends Page {
    private String title;
    private String content;

    public SimplePage(Renderer renderer, String title, String content) {
        super(renderer);
        this.title = title;
        this.content = content;
    }

    @Override
    public String view() {
        return renderer.renderTitle(title) + "\n" +
                renderer.renderTextBlock(content);
    }
}

class ProductPage extends Page {
    private Product product;

    public ProductPage(Renderer renderer, Product product) {
        super(renderer);
        this.product = product;
    }

    @Override
    public String view() {
        return renderer.renderField("id", product.getId()) + "\n" +
                renderer.renderTitle(product.getTitle()) + "\n" +
                renderer.renderTextBlock(product.getDescription()) + "\n" +
                renderer.renderImage(product.getImageUrl());
    }
}

public class Main {
    public static void main(String[] args) {
        Product phone = new Product("P-101", "Smartphone", "High performance phone", "phone.jpg");

        Renderer htmlRenderer = new HTMLRenderer();
        Renderer jsonRenderer = new JsonRenderer();
        Renderer xmlRenderer = new XmlRenderer();

        // Рендеринг SimplePage у різних форматах
        Page simplePageHtml = new SimplePage(htmlRenderer, "Про нас", "Ласкаво просимо на наш сайт!");
        Page simplePageJson = new SimplePage(jsonRenderer, "Про нас", "Ласкаво просимо на наш сайт!");

        System.out.println("=== SimplePage в HTML ===");
        System.out.println(simplePageHtml.view());

        System.out.println("\n=== SimplePage в JSON ===");
        System.out.println(simplePageJson.view());

        // Рендеринг ProductPage у різних форматах
        Page productPageHtml = new ProductPage(htmlRenderer, phone);
        Page productPageXml = new ProductPage(xmlRenderer, phone);

        System.out.println("\n=== ProductPage в HTML ===");
        System.out.println(productPageHtml.view());

        System.out.println("\n=== ProductPage в XML ===");
        System.out.println(productPageXml.view());
    }
}