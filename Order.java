import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final int id;
    private final String customerName;
    private final String dish;
    private final long preparationTime;
    private String status;
    private final Object lock = new Object();

    public Order(String customerName, String dish, long preparationTime) {
        this.id = counter.getAndIncrement();
        this.customerName = customerName;
        this.dish = dish;
        this.preparationTime = preparationTime;
        this.status = "СОЗДАН";
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getDish() { return dish; }
    public long getPreparationTime() { return preparationTime; }

    public String getStatus() {
        synchronized (lock) {
            return status;
        }
    }

    public void setStatus(String status) {
        synchronized (lock) {
            this.status = status;
        }
    }

    @Override
    public String toString() {
        return String.format("Заказ #%d: %s (для %s) [%s]", id, dish, customerName, status);
    }
}