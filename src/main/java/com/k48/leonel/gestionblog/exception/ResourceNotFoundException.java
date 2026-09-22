package com.k48.leonel.gestionblog.exception;

/**
 * Levée lorsqu'une ressource (article ou commentaire) n'existe pas.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
