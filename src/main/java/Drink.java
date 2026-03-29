import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Drink {
    private final String name;           // final = неизменяемое поле
    private final String[] ingredients;
    private List<String> measurements;

    @JsonCreator
    public Drink(
            @JsonProperty("name") String name,
            @JsonProperty("ingredients") String[] ingredients,
            @JsonProperty("measurements") List<String> measurements) {
        this.name = name;
        this.ingredients = ingredients;
        this.measurements = measurements;
    }

    public String getName() { return name; }
    public String[] getIngredients() { return ingredients; }
    public List<String> getMeasurements() { return measurements; }

    @Override
    public String toString() {
        return String.format("🍹 %s\n📝 Ингредиенты: %s\n👨‍🍳 %s",
                name, String.join(", ", ingredients), measurements);
    }
}