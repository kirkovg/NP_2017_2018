package mk.ukim.finki.np.midterms.first.task14;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewTest {
    public static void main(String[] args) {
        View view = new View();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; ++i) {
            view.addButton(i + 1, String.format("BUTTON %d", i + 1));
        }
        int nn = scanner.nextInt();
        for (int i = 0; i < nn; ++i) {
            int x = scanner.nextInt();
            view.setOnButtonClickListener(x, new LongButtonClickImpl());
        }
        nn = scanner.nextInt();
        for (int i = 0; i < nn; ++i) {
            int x = scanner.nextInt();
            view.setOnButtonClickListener(x, new ShortButtonClickImpl());
        }
        for (int i = 0; i < n; ++i) {
            try {
                view.clickButton(i + 1);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }

}

interface OnButtonClick {
    void onClick(Button button);
}

class ShortButtonClickImpl implements OnButtonClick {
    @Override
    public void onClick(Button button) {
        System.out.println(String.format("Button with id '%d' was clicked SHORT", button.getId()));
    }
}

class LongButtonClickImpl implements OnButtonClick {
    @Override
    public void onClick(Button button) {
        System.out.println(String.format("Button with id '%d' was clicked LONG", button.getId()));
    }
}

// vashiot kod ovde

class Button {
    private int id;
    private String name;
    private OnButtonClick onButtonClick;

    public Button(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void click() {
        if (onButtonClick == null)
            throw new UnsupportedOperationException("Button click is not supported");
        onButtonClick.onClick(this);
    }

    public int getId() {
        return id;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }
}

class View {
    private List<Button> buttons;

    public View() {
        buttons = new ArrayList<>();
    }

    public void addButton(int id, String name) {
        buttons.add(new Button(id, name));
    }

    public void setOnButtonClickListener(int id, OnButtonClick onButtonClick) {
        Button button = buttons.stream()
                .filter(btn -> btn.getId() == id)
                .findFirst()
                .get();

        button.setOnButtonClick(onButtonClick);
    }

    public void clickButton(int id) {
        Button button = buttons.stream()
                .filter(btn -> btn.getId() == id)
                .findFirst()
                .get();

        button.click();
    }
}