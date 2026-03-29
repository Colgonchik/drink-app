import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Drink {
    private final String name;           // final = неизменяемое поле
    private final String[] ingredients;

    @JsonCreator
    public Drink(
            @JsonProperty("name") String name,
            @JsonProperty("ingredients") String[] ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() { return name; }
    public String[] getIngredients() { return ingredients; }

    @Override
    public String toString() {
        return String.format("🍹 %s\n📝 Ингредиенты: %s\n👨‍🍳 %s",
                name, String.join(", ", ingredients));
    }
}