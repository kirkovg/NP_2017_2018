package mk.ukim.finki.np.midterms.first.task2;

import java.util.*;


class DateUtil {
    public static String parseDateToUtcString(Date date) {
        String[] parts = date.toString().split(" ");
        parts[4] = "UTC";
        return String.format("%s %s %s %s %s %s", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}

class NonExistingItemException extends Exception {
    private String message;

    public NonExistingItemException(int id) {
        this.message = String.format("Item with id %s doesn't exist", String.valueOf(id));
    }

    @Override
    public String getMessage() {
        return message;
    }
}

abstract class Archive {
    protected Integer id;
    protected Date dateArchived;

    public Archive(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public abstract boolean openArchive(int id, Date date, StringBuilder logger);

}

class LockedArchive extends Archive {

    private Date dateToOpen;

    public LockedArchive(Integer id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public boolean openArchive(int id, Date date, StringBuilder logger) {
        if (date.before(this.dateToOpen)) {
            logger.append(String.format("Item %d cannot be opened before %s\n",
                    id,
                    DateUtil.parseDateToUtcString(this.dateToOpen))
            );
            return false;
        }
        return true;
    }
}

class SpecialArchive extends Archive {

    private Integer maxOpen;
    private Integer countTimesOpened;

    public SpecialArchive(Integer id, Integer maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.countTimesOpened = 0;
    }

    @Override
    public boolean openArchive(int id, Date date, StringBuilder logger) {
        if (this.countTimesOpened >= this.maxOpen) {
            logger.append(String.format("Item %d cannot be opened more than %d times\n", id, this.maxOpen));
            return false;
        }
        this.countTimesOpened++;
        return true;
    }
}

class ArchiveStore {
    private List<Archive> archives;
    private StringBuilder logger;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        logger = new StringBuilder();
    }

    public void archiveItem(Archive item, Date date) {
        item.setDateArchived(date);
        archives.add(item);
        logger.append(String.format("Item %d archived at %s\n", item.id, DateUtil.parseDateToUtcString(date)));
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        Optional<Archive> item = archives.stream()
                .filter(archive -> archive.getId().equals(id))
                .findFirst();

        if (!item.isPresent()) {
            throw new NonExistingItemException(id);
        }

        if (item.get().openArchive(id, date, logger)) {
            logger.append(String.format("Item %s opened at %s\n", id, DateUtil.parseDateToUtcString(date)));
        }
    }

    public String getLog() {
        return logger.toString();
    }

}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

