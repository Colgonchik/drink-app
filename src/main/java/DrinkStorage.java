import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DrinkStorage {
    private List<Drink> drinks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Загрузка рецептов из JSON файла
    public void loadFromJson(String filePath) {
        try {
            // Загружаем файл из resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                System.err.println("Файл не найден: " + filePath);
                return;
            }

            // Парсим JSON в список напитков
            drinks = objectMapper.readValue(inputStream, new TypeReference<List<Drink>>() {});
            System.out.println("Загружено рецептов: " + drinks.size());

        } catch (Exception e) {
            System.err.println("Ошибка загрузки JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    public Drink getRandomDrink() {
        if (drinks.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * drinks.size());
        return drinks.get(randomIndex);
    }

    public List<Drink> getDrinksByIngredient(String ingredient) {
        List<Drink> result = new ArrayList<>();
        for (Drink drink : drinks) {
            for (String ing : drink.getIngredients()) {
                if (ing.toLowerCase().contains(ingredient.toLowerCase())) {
                    result.add(drink);
                    break;
                }
            }
        }
        return result;
    }

    public List<Drink> getAllDrinks() {
        return drinks;
    }

    public int getCount() {
        return drinks.size();
    }
}
