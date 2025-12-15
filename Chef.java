import java.util.concurrent.BlockingQueue;

public class Chef implements Runnable {
    private final int id;
    private final BlockingQueue<Order> orderQueue;
    private final BlockingQueue<Order> completedOrders;
    private volatile boolean running = true;

    public Chef(int id, BlockingQueue<Order> orderQueue, BlockingQueue<Order> completedOrders) {
        this.id = id;
        this.orderQueue = orderQueue;
        this.completedOrders = completedOrders;
    }

    @Override
    public void run() {
        System.out.printf("Повар %d начал работу%n", id);
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    Order order = orderQueue.take();
                    
                    if (order.getDish().equals("ЗАВЕРШЕНИЕ")) {
                        break;
                    }
                    
                    order.setStatus("ГОТОВИТСЯ");
                    System.out.printf("Повар %d начал готовить: %s%n", id, order);
                    
                    Thread.sleep(order.getPreparationTime());
                    
                    order.setStatus("ПРИГОТОВЛЕН");
                    System.out.printf("Повар %d приготовил: %s%n", id, order);
                    
                    completedOrders.put(order);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.printf("Повар %d столкнулся с ошибкой: %s%n", id, e.getMessage());
        }
        System.out.printf("Повар %d завершил работу%n", id);
    }
    
    public void stop() {
        running = false;
    }
}