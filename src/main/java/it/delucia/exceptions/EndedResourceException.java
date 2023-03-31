package it.delucia.exceptions;

import it.delucia.model.Resource;

public class EndedResourceException extends Exception{
    public EndedResourceException(Resource resource) {
        super("The resource " + resource.getName() + " is ended");
    }
}
