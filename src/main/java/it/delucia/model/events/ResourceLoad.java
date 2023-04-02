package it.delucia.model.events;

import it.delucia.model.Resource;

public class ResourceLoad extends Event{
    private Resource resource;
    private int quantity;

    public ResourceLoad(Resource resource, int quantity, int step) {
        super(step);
        this.resource = resource;
        this.quantity = quantity;
    }


    public Resource getResource() {
        return resource;
    }

    public int getQuantity() {
        return quantity;
    }


    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLoad that = (ResourceLoad) o;
        return quantity == that.quantity &&
                this.getStep() == that.getStep() &&
                resource.equals(that.resource);
    }




}
