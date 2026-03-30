import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Drink {
    private final String name;           // final = неизменяемое поле
    private final String[] ingredients;
    private final String method;
    private final String glass;
    private final String garnish;
    private final List<String> measurements;

    @JsonCreator
    public Drink(
            @JsonProperty("name") String name,
            @JsonProperty("ingredients") String[] ingredients,
            @JsonProperty("method") String method,
            @JsonProperty("glass") String glass,
            @JsonProperty("garnish") String garnish,
            @JsonProperty("measurements") List<String> measurements) {
        this.name = name;
        this.ingredients = ingredients != null ? ingredients : new String[0];
        this.method = method != null ? method : "—";
        this.glass = glass != null ? glass : "—";
        this.garnish = garnish != null ? garnish : "—";
        this.measurements = measurements != null ? measurements : List.of();
    }

    public String getName() { return name; }
    public String[] getIngredients() { return ingredients; }
    public String getMethod() { return  method; }
    public String getGlass() { return  glass; }
    public String getGarnish() { return  garnish; }
    public List<String> getMeasurements() { return measurements; }

    @Override
    public String toString() {
        return String.format("🍹 %s\n📝 Ингредиенты: %s\n🥄 %s\n🍷 %s\n🌿 %s",
                name,
                String.join(", ", ingredients),
                method,
                glass,
                garnish);
    }
}