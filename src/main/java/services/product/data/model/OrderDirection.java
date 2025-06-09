package services.product.data.model;

public enum OrderDirection {
    ASC("ASC"),
    DESC("DESC");
    
    private final String name;

    private OrderDirection(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
