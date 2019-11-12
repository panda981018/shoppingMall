/*
package com.example.shoppingmall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class ObjectList extends Activity {

    String name;
    String[] values = {"name", "price", "text", "url"};
    TextView[] lists = new TextView[5];
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toomuch);

        TextView one = findViewById(R.id.one);
        TextView two = findViewById(R.id.two);
        TextView three = findViewById(R.id.three);
        TextView four= findViewById(R.id.four);
        TextView five = findViewById(R.id.five);


        lists[0] = one;
        lists[1] = two;
        lists[2] = three;
        lists[3] = four;
        lists[4] = five;


        Intent intent = getIntent();
        name = (intent.getStringExtra("name"));
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("category");
        DatabaseReference thing = root.child(name);  // brace
        // 메인 액티비티에서 목걸이/팔찌/귀걸이중 하나 골라서 여기로 받아오는 것까지 함

        thing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) { // brace1
                    String text = snapshot.child("name").getValue(String.class);
                    lists[i].setText(text);
                    i++;
                };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}*/

package com.example.shoppingmall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class ObjectList extends Activity {
    ListView list;
    ArrayList<String> category = new ArrayList<>();
    String name;
    MyAdapter1 adapter;
    EditText editSearch;
    ArrayList<String> array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toomuch);
        editSearch = findViewById(R.id.editSearch);

        Intent intent = getIntent();
        name = (intent.getStringExtra("name")); // brace 받아오기

        list = findViewById(R.id.list2);
        adapter = new MyAdapter1(
                getApplicationContext(),
                R.layout.list, //textview
                category);
        list.setAdapter(adapter);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("category"); // category
        DatabaseReference thing = root.child(name);  // brace | neck | ear
        // 메인 액티비티에서 목걸이/팔찌/귀걸이중 하나 골라서 여기로 받아오는 것까지 함


        thing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String str = fileSnapshot.child("name").getValue(String.class);
                    Log.i("TAG: value is ", str);
                    category.add(str);
                }
                array.addAll(category);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                //input 창에 문자를 입력할 때마다 호출됨.
                // search 메소드 호출.
                String text = editSearch.getText().toString();
                search(text);
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        ObjectDetail.class);
                intent.putExtra("name1", name); // brace 전송
                intent.putExtra("name2", category.get(position).toLowerCase()); //brace1 전송
                startActivity(intent);
            }
        });


    }
    public void search(String charText) {
        category.clear();
        if (charText.length() == 0) {
            category.addAll(array);
        } else { //문자 입력을 할때
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).toLowerCase().contains(charText)) {
                    category.add(array.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}

class MyAdapter1 extends BaseAdapter {
    private int layout;
    private ArrayList<String> category;
    private LayoutInflater inf;

    public MyAdapter1(Context context, int layout, ArrayList<String> category) {
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
    public String getItem(int position) {
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
        TextView cat = (TextView) convertView.findViewById(R.id.list);

        String o = category.get(position);
        cat.setText(o);

        return convertView;
    }
}