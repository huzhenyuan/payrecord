package xyz.loadnl.payrecord.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import xyz.loadnl.payrecord.data.LogItem;

public class DBManager {
    private SQLiteDatabase db;
    private final String tableName = "loadnl";

    public DBManager(Context context) {
        DBHelper helper = DBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    public String getConfig(String name) {
        Cursor c = db.query(tableName, new String[]{"value_m"}, "key_m=?", new String[]{name}, null, null, null);
        String rs = "";
        while (c.moveToNext()) {
            rs = c.getString(c.getColumnIndex("value_m"));
            break;
        }
        c.close();
        return rs;
    }

    private boolean hasConfig(String name) {
        Cursor c = db.query(tableName, new String[]{"value_m"}, "key_m=?", new String[]{name}, null, null, null);
        if (c.moveToNext()) {
            c.close();
            return true;
        }
        return false;
    }

    public void setConfig(String name, String value) {
        ContentValues values = new ContentValues();
        values.put("key_m", name);
        values.put("value_m", value);
        if (hasConfig(name)) {
            db.update(tableName, values, "key_m=?", new String[]{name});
        } else {
            db.insert(tableName, null, values);
        }
    }

    public void addLog(String log, int type) {
        ContentValues values = new ContentValues();
        values.put("log_value", log);
        values.put("log_type", type);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        values.put("create_dt", sd.format(new Date(System.currentTimeMillis())));
        db.insert(tableName + "_log", null, values);
    }

    private Cursor getLog(int page, int type) {
        if (page < 1) {
            page = 1;
        }
        int from = (page - 1) * 25;
        String pageStr = from + "," + 25;
        Cursor c;
        if (type > 0) {
            c = db.query(tableName + "_log", new String[]{"*"}, "log_type=?", new String[]{type + ""}, null, null, "id desc", pageStr);
        } else {
            c = db.query(tableName + "_log", new String[]{"*"}, null, null, null, null, "id desc", pageStr);
        }
        return c;
    }

    public ArrayList<LogItem> getLogList(int page, int type) {
        ArrayList<LogItem> list = new ArrayList<>();
        Cursor c = getLog(page, type);
        int idIdx = c.getColumnIndex("id");
        int valueIdx = c.getColumnIndex("log_value");
        int typeIdx = c.getColumnIndex("log_type");
        int dateIdx = c.getColumnIndex("create_dt");
        while (c.moveToNext()) {
            LogItem item = new LogItem();
            item.id = c.getInt(idIdx);
            item.log_value = c.getString(valueIdx);
            item.log_type = c.getInt(typeIdx);
            item.create_dt = c.getString(dateIdx);
            list.add(item);
        }
        return list;
    }

}