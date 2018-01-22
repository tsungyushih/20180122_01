package com.frandog.a20180122_01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




//    去網路上下載DBBrowser，新建一個資料庫(有id，name，score)，然後在studio的res建一個raw資料夾，把剛建好的資料庫複製貼進去(聽老師說這是android的bug，記得id欄位要打_id，)
//    檔案讀寫方式與相關語法可參閱 http://android-deve.blogspot.tw/2012/11/file-access.html
    public void click1(View v)      //此按鈕的作用是將Resource的資料庫students.db抓下來，就可在Device Monitor的data/data/專案名/files裡看到建好的student.db
    {
        File dbFile = new File(getFilesDir(),"student.db");      //getFilesDir():取得 App 內部儲存體存放檔案的目錄 (絕對路徑)，預設路徑為 /data/data/[package.name]/files/

        InputStream is = getResources().openRawResource(R.raw.student);
        try {
            OutputStream os = new FileOutputStream(dbFile);
            int r;
            while ((r = is.read()) != -1)
            {
                os.write(r);
            }
            is.close();
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void click2(View v)      //以下為sqlite專屬寫法:查詢內容
    {
        File dbFile = new File(getFilesDir(),"student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        Cursor c = db.rawQuery("Select * from students",null);      //students是當初建資料庫的資料表名稱
        c.moveToFirst();
        Log.d("DB==============",c.getString(1)+","+c.getInt(2));

//        c.moveToNext();       //為避免click6、7當掉，要用while
//        Log.d("DB", c.getString(1) + "," + c.getInt(2));
        while (c.moveToNext())      //為避免click6、7當掉，要用while
        {
            Log.d("DB==============",c.getString(1)+","+c.getInt(2));
        }
    }
    public void click3(View v)      //指定查詢id2
    {
        File dbFile = new File(getFilesDir(),"student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        String strSql = "Select * from students where _id=?";
        Cursor c = db.rawQuery(strSql, new String[]{"2"});
//        Cursor c = db.rawQuery("Select * from students where _id='?'", null);        // 上兩行的簡略寫法
        c.moveToFirst();
        Log.d("DB", c.getString(1) + "," + c.getInt(2));
    }
    public void click4(View v)      //查詢First
    {
        File dbFile = new File(getFilesDir(),"student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        Cursor c = db.query("students", new String[] {"_id", "name", "score"}, null,null,null,null,null);
        c.moveToFirst();
        Log.d("DB==============",c.getString(1)+","+c.getInt(2));
    }
    public void click5(View v)      //指定查詢id2的另一種寫法
    {
        File dbFile = new File(getFilesDir(),"student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        Cursor c = db.query("students",new String[]{"_id","name","score"},"_id=?",new String[]{"2"},null,null,null);
        c.moveToFirst();
        Log.d("DB==============",c.getString(1)+","+c.getInt(2));

    }

    public void click6(View v)      //此為SQL語法，因key重複，按第2次就會當掉，新增id3
    {
        File dbFile = new File(getFilesDir(), "student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("Insert into students (_id, name, score) values (3, 'Bob', 95)");
        db.close();
    }
    public void click7(View v)      //因key重複，按第2次就會當掉，新增id4
    {
        File dbFile = new File(getFilesDir(), "student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues cv = new ContentValues();
        cv.put("_id", 4);
        cv.put("name", "Mary");
        cv.put("score", 92);
        db.insert("students", null, cv);
        db.close();
    }

    public void click8(View v)      //修改
    {
        File dbFile = new File(getFilesDir(),"student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        ContentValues cv = new ContentValues();
        cv.put("score",85);
        db.update("students",cv,"_id=?",new String[]{"2"});
        db.close();
    }
    public void click9(View v)      //刪除
    {
        File dbFile = new File(getFilesDir(),"student.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        db.delete("students","_id=?",new String[]{"2"});
        db.close();
    }
}

