package edu.utdallas.whoosh.api;

import java.util.List;
import edu.utdallas.whoosh.appservices.Contact;

/**
 * Service interface that provides functionality to retrieve {@link IContact} information.
 *
 * Created by sasha on 9/22/15.
 */
public interface IDirectoryService {

    /**
     * returns all available {@link IContact}s
     */
    List<Contact> getContacts();

}