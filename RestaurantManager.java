import java.util.concurrent.*;
import java.util.*;

public class RestaurantManager {
    private final BlockingQueue<Order> orderQueue;
    private final BlockingQueue<Order> completedOrders;
    private final ExecutorService chefExecutor;
    private final List<Waiter> waiters;
    private final List<Thread> waiterThreads;
    private volatile boolean isRunning = false;
    private final int waiterCount;
    private final int chefCount;
    
    public RestaurantManager(int waiterCount, int chefCount) {
        this.waiterCount = waiterCount;
        this.chefCount = chefCount;
        
        this.orderQueue = new LinkedBlockingQueue<>();
        this.completedOrders = new LinkedBlockingQueue<>();
        
        this.chefExecutor = Executors.newFixedThreadPool(chefCount);
        for (int i = 1; i <= chefCount; i++) {
            chefExecutor.execute(new Chef(i, orderQueue, completedOrders));
        }
        
        this.waiters = new ArrayList<>();
        this.waiterThreads = new ArrayList<>();
        for (int i = 1; i <= waiterCount; i++) {
            Waiter waiter = new Waiter(i, orderQueue, completedOrders);
            waiters.add(waiter);
            Thread waiterThread = new Thread(waiter);
            waiterThreads.add(waiterThread);
        }
    }
    
    public void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.println("======================================");
            System.out.printf("Ресторан открывается с %d официантами и %d поварами%n", 
                waiterCount, chefCount);
            System.out.println("======================================");
            
            for (Thread waiterThread : waiterThreads) {
                waiterThread.start();
            }
            
            System.out.println("Ресторан открыт!");
        }
    }
    
    public void shutdown() {
        if (isRunning) {
            isRunning = false;
            System.out.println("\n======================================");
            System.out.println("Ресторан закрывается...");
            System.out.println("======================================");
            
            for (Waiter waiter : waiters) {
                waiter.stop();
            }
            
            try {
                for (int i = 0; i < chefCount; i++) {
                    orderQueue.put(new Order("", "ЗАВЕРШЕНИЕ", 0));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            for (Thread waiterThread : waiterThreads) {
                try {
                    waiterThread.join(3000);
                    if (waiterThread.isAlive()) {
                        waiterThread.interrupt();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            chefExecutor.shutdown();
            try {
                if (!chefExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    chefExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                chefExecutor.shutdownNow();
            }
            
            printStatistics();
        }
    }
    
    private void printStatistics() {
        System.out.println("\n=== Статистика ресторана ===");
        System.out.printf("Заказов в очереди на кухне: %d%n", orderQueue.size());
        System.out.printf("Готовых заказов в ожидании: %d%n", completedOrders.size());
        System.out.printf("Всего официантов: %d%n", waiterCount);
        System.out.printf("Всего поваров: %d%n", chefCount);
        System.out.println("Ресторан успешно закрыт!");
    }
    
    public void addOrderDirectly(Order order) throws InterruptedException {
        orderQueue.put(order);
    }
    
    public BlockingQueue<Order> getOrderQueue() {
        return orderQueue;
    }
    
    public BlockingQueue<Order> getCompletedOrders() {
        return completedOrders;
    }
}