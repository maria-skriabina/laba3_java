public class RestaurantDemo {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация работы ресторана ===");
        
        RestaurantManager restaurant = new RestaurantManager(3, 2);
        
        restaurant.start();
        
        try {
            Thread.sleep(15000); // 15 секунд работы
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Демонстрация прервана!");
        }
        
        restaurant.shutdown();
        
        System.out.println("\n=== Демонстрация завершена ===");
    }
}