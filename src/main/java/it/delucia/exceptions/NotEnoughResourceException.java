package it.delucia.exceptions;

import it.delucia.model.Resource;

public class NotEnoughResourceException extends Exception{
    public NotEnoughResourceException(Resource resource) {
        super("The resource " + resource.getName() + " is not enough");
    }
}
