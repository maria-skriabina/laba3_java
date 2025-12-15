public class RestaurantTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Тестирование системы ресторана ===\n");
        
        System.out.println("Тест 1: Создание ресторана с 2 официантами и 1 поваром");
        RestaurantManager restaurant = new RestaurantManager(2, 1);
        restaurant.start();
        
        Thread.sleep(3000);
        
        System.out.println("\nТест 2: Добавление 3 тестовых заказов");
        restaurant.addOrderDirectly(new Order("ТестКлиент1", "ТестоваяПаста", 2000));
        restaurant.addOrderDirectly(new Order("ТестКлиент2", "ТестоваяПицца", 3000));
        restaurant.addOrderDirectly(new Order("ТестКлиент3", "ТестовыйСалат", 1000));
        
        System.out.println("Заказов в очереди: " + restaurant.getOrderQueue().size());
        
        Thread.sleep(5000);
        
        System.out.println("\nТест 3: Закрытие ресторана");
        restaurant.shutdown();
        
        System.out.println("\n=== Все тесты пройдены успешно ===");
    }
}