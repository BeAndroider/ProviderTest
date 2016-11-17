package cn.beandroid.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //insert()方法会返回一个带新增数据ID的Uri对象，用newId存储它
    private String newId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加数据
        Button addDate = (Button)findViewById(R.id.add_date);
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先解析 内容URI
                Uri uri = Uri.parse("content://beandroider.cn.datebasetest.provider/Book");
                ContentValues values = new ContentValues();
                values.put("name","Java核心技术");
                values.put("author","Cay");
                values.put("price",65);
                values.put("pages",500);
                //insert()会返回一个uri对象
                Uri newUri = getContentResolver().insert(uri,values);
                //这个uri对象会携带新增数据的id
                newId = newUri.getPathSegments().get(1);
            }
        });

        //查询数据
        Button queryDate = (Button)findViewById(R.id.query_date);
        queryDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://beandroider.cn.datebasetest.provider/Book");
                Cursor cursor = getContentResolver().query(uri,null,null,null,null,null);
                if (cursor != null){
                    while (cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity","book name is " + name);
                        Log.d("MainActivity","author name is" + author);
                        Log.d("MainActivity","book pages are" + pages);
                        Log.d("MainActivity","book price is" + price);
                    }
                    //记得关闭游标
                    cursor.close();
                }

            }
        });
        //更新数据
        Button updateDate = (Button)findViewById(R.id.update_date);
        updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------------->newId是新增数据的id，这里是为了约束行数从而不影响其他行数
                Uri uri = Uri.parse("conent://beandroider.cn.datebasetest.provider/Book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name","Android编程权威指南");
                values.put("author","Bill");
                values.put("pages",400);
                values.put("price",25);
                getContentResolver().update(uri,values,null,null);
            }
        });
        //删除数据
        Button deleteDate = (Button)findViewById(R.id.delete_date);
        deleteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://beandroider.cn.datebasetest.provider/Book/" + newId);
                getContentResolver().delete(uri,null,null);
            }
        });
    }
}
