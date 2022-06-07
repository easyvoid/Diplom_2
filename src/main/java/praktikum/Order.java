package praktikum;

public class Order {
    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {

    }

    public String[] getIngredients() {
        return ingredients;
    }

    public Order setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}
