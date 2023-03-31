package it.delucia.exceptions;

import it.delucia.model.Resource;

public class DuplicateResourceException extends Exception{
    public DuplicateResourceException(Resource resource) {
        super("The resource " + resource.getName() + " is already existing!");
    }
}
