package com.example.consultorio.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Consultorio.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CITAS = "citas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_TELEFONO = "telefono"
        private const val COLUMN_FECHA = "fecha"
        private const val COLUMN_HORA = "hora"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_CITAS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NOMBRE TEXT, " +
                "$COLUMN_TELEFONO TEXT, " +
                "$COLUMN_FECHA TEXT, " +
                "$COLUMN_HORA TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CITAS")
        onCreate(db)
    }

    // CREATE
    fun insertarCita(cita: Cita): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, cita.nombre)
            put(COLUMN_TELEFONO, cita.telefono)
            put(COLUMN_FECHA, cita.fecha)
            put(COLUMN_HORA, cita.hora)
        }
        val id = db.insert(TABLE_CITAS, null, values)
        db.close()
        return id
    }

    // READ
    fun obtenerTodasLasCitas(): List<Cita> {
        val listaCitas = mutableListOf<Cita>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CITAS", null)

        if (cursor.moveToFirst()) {
            do {
                val cita = Cita(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA))
                )
                listaCitas.add(cita)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaCitas
    }

    // UPDATE
    fun actualizarCita(cita: Cita): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, cita.nombre)
            put(COLUMN_TELEFONO, cita.telefono)
            put(COLUMN_FECHA, cita.fecha)
            put(COLUMN_HORA, cita.hora)
        }
        val success = db.update(TABLE_CITAS, values, "$COLUMN_ID=?", arrayOf(cita.id.toString()))
        db.close()
        return success
    }

    // DELETE
    fun eliminarCita(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_CITAS, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }
}
