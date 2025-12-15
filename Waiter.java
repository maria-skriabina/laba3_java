import java.util.concurrent.BlockingQueue;
import java.util.Random;

public class Waiter implements Runnable {
    private final int id;
    private final BlockingQueue<Order> orderQueue;
    private final BlockingQueue<Order> completedOrders;
    private volatile boolean running = true;
    private final Random random = new Random();

    public Waiter(int id, BlockingQueue<Order> orderQueue, BlockingQueue<Order> completedOrders) {
        this.id = id;
        this.orderQueue = orderQueue;
        this.completedOrders = completedOrders;
    }

    @Override
    public void run() {
        System.out.printf("Официант %d начал работу%n", id);
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(random.nextInt(2000) + 1000);
                
                String[] dishes = {"Паста", "Пицца", "Салат", "Стейк", "Суп", "Бургер"};
                String dish = dishes[random.nextInt(dishes.length)];
                String customer = "Клиент-" + (random.nextInt(100) + 1);
                long prepTime = (random.nextInt(4) + 2) * 1000L;
                
                Order order = new Order(customer, dish, prepTime);
                order.setStatus("ПРИНЯТ ОФИЦИАНТОМ");
                System.out.printf("Официант %d принял заказ: %s%n", id, order);
                
                orderQueue.put(order);
                order.setStatus("В ОЧЕРЕДИ НА КУХНЕ");
                System.out.printf("Официант %d отправил заказ на кухню: %s%n", id, order);
                
                Order completedOrder = completedOrders.take();
                completedOrder.setStatus("ГОТОВ К ДОСТАВКЕ");
                System.out.printf("Официант %d получил готовый заказ: %s%n", id, completedOrder);
                
                deliverOrder(completedOrder);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("Официант %d завершил работу%n", id);
    }
    
    private void deliverOrder(Order order) throws InterruptedException {
        order.setStatus("ДОСТАВЛЯЕТСЯ");
        System.out.printf("Официант %d доставляет: %s%n", id, order);
        Thread.sleep(random.nextInt(1000) + 500);
        order.setStatus("ДОСТАВЛЕН");
        System.out.printf("Официант %d доставил: %s%n", id, order);
    }
    
    public void stop() {
        running = false;
    }
}