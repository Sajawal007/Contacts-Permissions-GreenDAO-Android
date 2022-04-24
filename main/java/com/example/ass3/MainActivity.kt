package com.example.ass3

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greendaogenerator.db.DaoMaster
import com.example.greendaogenerator.db.DaoSession
import com.example.greendaogenerator.db.User
import com.example.greendaogenerator.db.UserDao
import org.greenrobot.greendao.database.Database
import java.lang.StringBuilder
import java.util.jar.Manifest



class MainActivity : AppCompatActivity() {
    //variables
    lateinit var recyclerView: RecyclerView
    var contacts_list = arrayOf<Contact>()
    var contacts_loaded_from_db = arrayOf<Contact>()
    lateinit var adapter:MainAdapter
    //db things
    lateinit var users: UserDao
    var usersList = arrayOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //assign recycler view
        recyclerView = findViewById(R.id.recycler_view)

        //check for permissions
        checkPermission()

        //accessing the initialized session of greenDAO
        var daoSession:DaoSession = MainApplication.applicationContext().getDaoSession()
        users  = daoSession.getUserDao()

        //saving contacts into the Database
        if(users.queryBuilder().count().toInt() == 0){
            for(item in contacts_list){
                var tempusr: User = User()
                tempusr.setName(item.getName())
                tempusr.setPhone(item.getNumber())
                users.save(tempusr)
            }
        }


        //loading contacts from the database
        usersList = users.queryBuilder().list().toTypedArray()
        for (item in usersList){
            contacts_loaded_from_db += Contact(item.getName(),item.getPhone())
        }

        adapter = MainAdapter(this,contacts_loaded_from_db)
        recyclerView.adapter  = adapter

    }


    private fun checkPermission() {
        //check condition
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            //when permission is not granted
            //request permission
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_CONTACTS),100)
        }
        else
        {
            //permission granted
            getContactList()

        }
    }
    @SuppressLint("Range")
    private fun getContactList():StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
        null,
            null,
            null,
            null
        )

        if (cursor != null) {
            if(cursor.count > 0){
                while(cursor.moveToNext()){
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                    if(phoneNumber > 0)
                    {
                        var cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null)
                        //attaining phone number here
                        if(cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext())
                                {
                                    val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    contacts_list += Contact(name,phoneNumValue)
                                }
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "No Contacts Available!", Toast.LENGTH_SHORT).show()
        }
        cursor?.close()
        return builder
    }


}
