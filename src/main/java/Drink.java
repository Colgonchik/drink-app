import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Drink {
    private final String name;           // final = неизменяемое поле
    private final String[] ingredients;
    private final String instruction;

    @JsonCreator
    public Drink(
            @JsonProperty("name") String name,
            @JsonProperty("ingredients") String[] ingredients,
            @JsonProperty("instruction") String instruction) {
        this.name = name;
        this.ingredients = ingredients;
        this.instruction = instruction;
    }

    public String getName() { return name; }
    public String[] getIngredients() { return ingredients; }
    public String getInstruction() { return instruction; }

    @Override
    public String toString() {
        return String.format("🍹 %s\n📝 Ингредиенты: %s\n👨‍🍳 %s",
                name, String.join(", ", ingredients), instruction);
    }
}