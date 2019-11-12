package com.example.shoppingmall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ObjectDetail extends Activity {
    ListView list;
    ArrayList<ObjectThing2> category = new ArrayList<>();
    MyAdapter2 adapter;
    String name;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaillist);

        intent = getIntent();
        name = (intent.getStringExtra("name1")); // brace 받음
        list = findViewById(R.id.detailList);
        adapter = new MyAdapter2(
                getApplicationContext(),
                R.layout.detail, //textview
                category);
        list.setAdapter(adapter);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("category");
        DatabaseReference thing = root.child(name);  // brace

        thing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //datasnapshot : brace1, brace2...

                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String name2 = intent.getStringExtra("name2");
                    if(fileSnapshot.child("name").getValue(String.class).equals(name2)) {
                        // 만약 전 액티비티에서 누른 항목과 같다면( brace 1을 눌렀으면 brace1만의 세부사항 출력)
                        String name = fileSnapshot.child("name").getValue(String.class);
                        Log.i("TAG: value is ", name);

                        String price = fileSnapshot.child("price").getValue(String.class);
                        Log.i("TAG: value is ", price);

                        String url = fileSnapshot.child("url").getValue(String.class);
                        Log.i("TAG: value is ", url);

                        category.add(new ObjectThing2(name, price, url));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}

class ObjectThing2{
    String name = "";
    String price = "";
    String url = "";
    public ObjectThing2(String name, String price, String url){
        this.price = price;
        this.url = url;
        this.name = name;
    }
}
class MyAdapter2 extends BaseAdapter {
    private int layout;
    private ArrayList<ObjectThing2> category;
    private LayoutInflater inf;

    public MyAdapter2(Context context, int layout, ArrayList<ObjectThing2> category) {
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
    public ObjectThing2 getItem(int position) {
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
        TextView cat = (TextView) convertView.findViewById(R.id.name);
        TextView cat2 = (TextView) convertView.findViewById(R.id.price);
        TextView cat3 = (TextView) convertView.findViewById(R.id.url);
        ImageView cat4 = (ImageView) convertView.findViewById(R.id.image);
        ObjectThing2 o = category.get(position);

        cat.setText(o.name);
        cat2.setText(o.price);
        cat3.setText(o.url);


        return convertView;
    }
}

