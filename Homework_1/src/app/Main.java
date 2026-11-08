package app;

public class Main {
    public static void main(String[] args) {

        // Создаем HashMap пользователей
        CustomHashMap<Integer, String> usersMap = new CustomHashMap<>();

        // Создаем HashMap товаров
        CustomHashMap<String, Double> gpuMap = new CustomHashMap<>();

        // Добавляем пользователей
        usersMap.put(1201, "ivanov@ya.ru");
        usersMap.put(1302, "petrov@mail.ru");
        usersMap.put(1404, "sidorov@gmail.com");

        // Добавляем каталог товаров
        gpuMap.put("NVIDIA RTX 4060", 35000.00);
        gpuMap.put("NVIDIA RTX 4070 Ti", 85499.90);
        gpuMap.put("AMD Radeon RX 7800 XT", 62000.50);
        gpuMap.put("NVIDIA RTX 4090", 210000.00);

        // Выводим в консоль
        System.out.println("Пользователи: " + usersMap);
        System.out.println("Количество пользователей: " + usersMap.size());
        System.out.println();
        System.out.println("Каталог видеокарт: " + gpuMap);
        System.out.println("Всего видеокарт в каталоге: " + gpuMap.size());
        System.out.println();

        // Проверки на вхождение
        System.out.println("Есть ли в наличии 'NVIDIA RTX 4060'? " + (gpuMap.containsKey("NVIDIA RTX 4060") ? " Да " : " Нет "));
        System.out.println("Есть ли пользователь с email 'nondefined@ya.ru'? " + (usersMap.containsValue("nondefined@ya.ru") ? " Да" : " Нет "));
        System.out.println();

        // Очищаем каталог товаров
        gpuMap.clear();
        System.out.println("Очищен каталог товаров");
        System.out.println();

        // Удаляем пользователя с ID = 1302
        String removedUser = usersMap.remove(1302);
        System.out.println("Удален пользователь: " + removedUser);
        System.out.println();

        // Выводим в консоль
        System.out.println("Пользователи: " + usersMap);
        System.out.println("Количество пользователей: " + usersMap.size());
        System.out.println();
        System.out.println("Каталог видеокарт: " + gpuMap);
        System.out.println("Всего видеокарт в каталоге: " + gpuMap.size());
        System.out.println();

    }
}
