package it.delucia.model;

public class ResourceLoad {
    private Resource resource;
    private int quantity;
    private int step;

    public ResourceLoad(Resource resource, int quantity, int step) {
        this.resource = resource;
        this.quantity = quantity;
        this.step = step;
    }


    public Resource getResource() {
        return resource;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStep() {
        return step;
    }

    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLoad that = (ResourceLoad) o;
        return quantity == that.quantity &&
                step == that.step &&
                resource.equals(that.resource);
    }




}
