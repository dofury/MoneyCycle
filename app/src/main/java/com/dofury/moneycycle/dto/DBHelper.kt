package com.dofury.moneycycle.dto

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import android.widget.Toast
import java.io.File

class DBHelper(
    val context: Context?,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MONEY_LOG"

        // Table
        const val TABLE_NAME = "LOGS"
        const val UID = "UID"
        const val COL_CHARGE = "CHARGE"
        const val COL_SIGN = "SIGN"
        const val COL_CATEGORY = "CATEGORY"
        const val COL_DATE = "DATE"
        const val COL_MEMO = "MEMO"
        const val COL_IS_BUDGET = "IS_BUDGET"
        const val COL_IS_SERVER = "IS_SERVER"
    }
    override fun onCreate(db: SQLiteDatabase) {
        var sql: String = "CREATE TABLE if not exists " +
                "$TABLE_NAME ($UID integer primary key autoincrement, " +
                "$COL_CHARGE integer, $COL_SIGN integer, $COL_CATEGORY text, $COL_DATE text, $COL_MEMO text, $COL_IS_BUDGET integer, $COL_IS_SERVER integer);"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists $TABLE_NAME"
        db.execSQL(sql)
        onCreate(db)
    }

    val allLogs:MutableList<MoneyLog>
        @SuppressLint("Range")
        get() {
            val logs = ArrayList<MoneyLog>()
            val selectQueryHandler = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQueryHandler,null)
            if(cursor.moveToFirst()){
                do{
                    val log = MoneyLog(
                        cursor.getInt(cursor.getColumnIndex(UID)),
                        cursor.getLong(cursor.getColumnIndex(COL_CHARGE)),
                        intToBoolean(cursor.getInt(cursor.getColumnIndex(COL_SIGN))),
                        cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COL_DATE)),
                        cursor.getString(cursor.getColumnIndex(COL_MEMO)),
                        intToBoolean(cursor.getInt(cursor.getColumnIndex(COL_IS_BUDGET))),
                        intToBoolean(cursor.getInt(cursor.getColumnIndex(COL_IS_SERVER))),
                    )
                    logs.add(log)
                }while(cursor.moveToNext())
            }
            db.close()
            return logs
        }
    fun addLog(log: MoneyLog){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_CHARGE, log.charge)
        values.put(COL_SIGN, log.sign)
        values.put(COL_CATEGORY, log.category)
        values.put(COL_DATE, log.date)
        values.put(COL_MEMO, log.memo)
        values.put(COL_IS_BUDGET, log.is_budget)
        values.put(COL_IS_SERVER, log.is_server)

        db.insert(TABLE_NAME, null, values)
        db.close()
        Toast.makeText(this.context,"가계부 추가", Toast.LENGTH_SHORT).show()
    }

    private fun intToBoolean(int: Int): Boolean {
        return int>0
    }
    // 유저 정보 업데이트 메소드
    fun updateLog(log: MoneyLog): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_CHARGE, log.charge)
        values.put(COL_SIGN, log.sign)
        values.put(COL_CATEGORY, log.category)
        values.put(COL_DATE, log.date)
        values.put(COL_MEMO, log.memo)
        values.put(COL_IS_BUDGET, log.is_budget)
        values.put(COL_IS_SERVER, log.is_server)

        return db.update(TABLE_NAME, values, "$UID=?", arrayOf(log.uid.toString()))
    }

    // 유저 삭제 메소드
    fun deleteLog(log: MoneyLog){
        val db = this.writableDatabase

        db.delete(TABLE_NAME,"$UID=?", arrayOf(log.uid.toString()))
        db.close()
    }


    fun drop() {
        val DB_PATH = "/data/data/" + (context?.packageName ?: String)
        val DB_NAME = "$DATABASE_NAME.db"
        val DB_FULLPATH = "$DB_PATH/databases/$DB_NAME"
        val dbFile = File(DB_FULLPATH)
        if (dbFile.delete()) {
            println(" 삭제 성공")
        } else {
            println(" 삭제 실패")
        }
    }

}