package edu.utdallas.whoosh.api;

/**
 * Data interface that represents contact information for a single entity.
 *
 * Created by sasha on 9/22/15.
 */
public interface IContact {

    /**
     * a natural key
     */
    String getId();
    String getName();
    String getPhoneNumber();
    String getEmailAddress();
    String getUrl();

}