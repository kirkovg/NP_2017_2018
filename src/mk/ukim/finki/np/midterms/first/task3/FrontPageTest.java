package mk.ukim.finki.np.midterms.first.task3;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


import static java.util.stream.Collectors.toList;

class CategoryNotFoundException extends Exception {
    private String message;

    @Override
    public String getMessage() {
        return String.format("Category %s was not found", message);
    }

    public CategoryNotFoundException(String message) {
        this.message = message;
    }
}

class Category {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return name != null ? name.equals(category.name) : category.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }
}

abstract class NewsItem {
    protected String title;
    protected Date date;
    protected Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        long duration = new Date().getTime() - date.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        sb.append(diffInMinutes).append("\n");
        for(int i = 0; i < text.length(); i++){
            if (i == 80) {
                break;
            }
            sb.append(text.charAt(i));
        }

        sb.append("\n");
        return sb.toString();
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        long duration = new Date().getTime() - date.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        sb.append(diffInMinutes).append("\n");
        sb.append(url).append("\n");
        sb.append(views).append("\n");
        return sb.toString();
    }
}

class FrontPage {
    private List<NewsItem> news;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        this.news = new ArrayList<>();
    }

    public void addNewsItem(NewsItem newsItem) {
        news.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        return news.stream()
                .filter(item -> item.getCategory().equals(category))
                .collect(toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Optional<Category> cat = Arrays.stream(categories)
                .filter(c -> c.getName().equals(category))
                .findFirst();

        if (!cat.isPresent()) {
            throw new CategoryNotFoundException(category);
        }

        return listByCategory(cat.get());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        news.forEach(item -> sb.append(item.getTeaser()));
        return sb.toString();
    }

}


public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}