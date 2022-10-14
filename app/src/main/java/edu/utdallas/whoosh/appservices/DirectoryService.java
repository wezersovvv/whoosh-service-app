package edu.utdallas.whoosh.appservices;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.utdreqeng.whoosh.whoosh.R;

import edu.utdallas.whoosh.api.IDirectoryService;
import edu.utdallas.whoosh.appservices.Contact;

import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Taber on 11/29/2015.
 */
public class DirectoryService implements IDirectoryService{

    /**
     * Used for storing direct path to contacts file
     */
    private static final String contactsFileDir = "src/main/txt/";
    private static final String contactsFileName = "contacts.txt";

    /**
     * Used for easy change of attribute indexing within contacts file
     */
    private static final int idIndex = 0;
    private static final int nameIndex = 1;
    private static final int phoneNumberIndex = 2;
    private static final int emailAddressIndex = 3;
    private static final int urlIndex = 4;
    private Context ct;

    public DirectoryService(Context ct){
        this.ct = ct;
    }

    /**
     * Reads contacts file and returns list of all contacts
     *
     * @return - List<Contact> - all contacts read from file contacts.txt
     */
    @Override
    public List<Contact> getContacts()
    {
        String fileInput;
        String[] contactComponents;
        List<Contact> contacts = new ArrayList<>();

        try {
            Scanner contactsFileReader = new Scanner(ct.getResources().openRawResource(R.raw.contacts));
            while(contactsFileReader.hasNextLine())
            {
                fileInput = contactsFileReader.nextLine();
                Contact currContact = new Contact();

                if(fileInput.contains(";"))
                {
                    contactComponents = fileInput.split(";");

                    //Add attributes to object
                    currContact.setId(contactComponents[idIndex]);
                    currContact.setName(contactComponents[nameIndex]);
                    currContact.setPhoneNumber(contactComponents[phoneNumberIndex]);
                    currContact.setEmailAddress(contactComponents[emailAddressIndex]);
                    currContact.setUrl(contactComponents[urlIndex]);

                    //Add object to list
                    contacts.add(currContact);
                }
            }
        }
        /*catch(FileNotFoundException ex) {
            System.out.println("WARNING: contacts.txt file not found in expected location (" + contactsFileDir + "). Contact tech support or replace file.");
        }*/
        catch(Exception ex) {
            System.out.println("FATAL: Unexpected exception occurred. Contact tech support.");
        }

        return contacts;
    }
}
