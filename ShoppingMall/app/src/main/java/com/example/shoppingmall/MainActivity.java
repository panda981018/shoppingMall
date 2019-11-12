package com.example.shoppingmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // main의 리스트뷰에 넣을 때 필요한 것들 초기화
    ListView list;
    ArrayList<ObjectThing> category = new ArrayList<ObjectThing>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //listview에 삽입
        category.add(new ObjectThing("NECK"));
        category.add(new ObjectThing("BRACE"));
        category.add(new ObjectThing("EAR"));

        //adapter 적용
        MyAdapter adapter = new MyAdapter(
            getApplicationContext(),
            R.layout.category, //textview
            category);

        list = findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        ObjectList.class);
                intent.putExtra("name", category.get(position).name.toLowerCase());
                startActivity(intent);
            }
        });

    }
}

class ObjectThing{
    String name = "";
    public ObjectThing(String name){
        this.name = name;
    }
}
class MyAdapter extends BaseAdapter {
    private int layout;
    private ArrayList<ObjectThing> category;
    private LayoutInflater inf;

    public MyAdapter(Context context, int layout, ArrayList<ObjectThing> category) {
        this.layout = layout;
        this.category = category;
        inf = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return category.size();
    }

    @Override
    public ObjectThing getItem(int position) {
        return category.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);
        TextView cat = (TextView) convertView.findViewById(R.id.category);

        ObjectThing o = category.get(position);
        cat.setText(o.name);

        return convertView;
    }
}
