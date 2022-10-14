package com.utdreqeng.whoosh.whoosh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.utdallas.whoosh.appservices.Contact;
import edu.utdallas.whoosh.appservices.DirectoryService;

/**
 * Created by Dustin on 11/30/2015.
 */
public class DirectoryActivity extends AppCompatActivity
{
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_layout);
        contacts = new DirectoryService(this.getApplicationContext()).getContacts();

        String[] osArray = { "Campus Map", "Directory", "About" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        ListView mDrawerList = (ListView)findViewById(R.id.navList);

        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageResource(R.drawable.nav_title);
        mDrawerList.addHeaderView(iv);

        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (position == 1) {
                    onBackPressed();
                } else if (position == 2) {

                } else if (position == 3) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://utdwhoosh.github.io/")));
                }
            }
        });

        ListView contactList = (ListView)findViewById(R.id.directory_list);

        contactList.setAdapter(new DirectoryAdapter(getApplicationContext(),
                contacts, R.layout.contact_layout));
    }

    private class DirectoryAdapter extends ArrayAdapter<Contact> {

        private List<Contact> contacts;

        public DirectoryAdapter(Context ct, List<Contact> contacts, int resId){
           super(ct, resId);

            this.contacts = contacts;
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Contact getItem(int i) {
            return contacts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View temp = null;

            if (view == null) {
                temp = LayoutInflater.from(getContext()).inflate(R.layout.contact_layout, null);
            }

            if(temp != null) {
                ((TextView) temp.findViewById(R.id.contact_name)).setText(getItem(i).getName());
                ((TextView) temp.findViewById(R.id.contact_phone)).setText(getItem(i).getEmailAddress());
                ((TextView) temp.findViewById(R.id.contact_email)).setText(getItem(i).getPhoneNumber());
            }
            return temp;
        }
    }
}
