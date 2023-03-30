package it.delucia.exceptions;

import it.delucia.algo.Resource;

public class EndedResourceException extends Exception{
    public EndedResourceException(Resource resource) {
        super("The resource " + resource.getName() + " is ended");
    }
}
