package com.example.ass3
import android.app.Application
import android.util.Log
import com.example.greendaogenerator.db.DaoMaster
import com.example.greendaogenerator.db.DaoSession
import org.greenrobot.greendao.database.Database

class MainApplication: Application(){
    //db part
    init {
        instance =this
    }
   companion object Farm{
        private var instance: MainApplication? = null
       lateinit var daoSession: DaoSession
        fun applicationContext() : MainApplication {
            return instance as MainApplication
        }
   }
    public override fun onCreate(){
        super.onCreate()

        //initialize regular SQL Database
        var helper: DaoMaster.DevOpenHelper = DaoMaster.DevOpenHelper(this, "contact_table", null)
        var db: Database =  helper.writableDb

        daoSession = DaoMaster(db).newSession()
    }
    @JvmName("getDaoSession1")
    public fun getDaoSession():DaoSession{
        return daoSession
    }

}