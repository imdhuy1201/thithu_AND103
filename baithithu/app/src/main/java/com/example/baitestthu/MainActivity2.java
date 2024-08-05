package com.example.baitestthu;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitestthu.adapter.SinhvienAdapter;
import com.example.baitestthu.handle.Item_Handle;
import com.example.baitestthu.model.Response;
import com.example.baitestthu.model.Student;
import com.example.baitestthu.services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity2 extends AppCompatActivity implements Item_Handle {
    private RecyclerView rcvstudent;
    private HttpRequest httpRequest;
    private SinhvienAdapter adapter;
    EditText txtsearch;
    private ArrayList<Student> list;
    FloatingActionButton fltadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //ánh xạ
        rcvstudent=findViewById(R.id.rcvstudent);
        txtsearch=findViewById(R.id.txtsearch);
        fltadd=findViewById(R.id.fltadd);

        list= new ArrayList<>();//khởi tạo mảng
        //khởi tạo services
        httpRequest = new HttpRequest();
        // thực thi call API
        httpRequest.CallApi()
                .getListStudent()//phương thi api cân thuc thi
                .enqueue(getListStudentAPI);//xử lý bất đồng bộ
        //btnthem
        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialogthem();

            }
        });
        //tìm kiếm
        txtsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    //lấy từ khóa từ ô tìm kiếm
                    String key=txtsearch.getText().toString();
                    httpRequest.CallApi()
                            .searchStudent(key) //phung thức API cần thực thi
                            .enqueue(getListStudentAPI);//xử lý bất đồng bộ
                    return true;
                }
                return false;
            }
        });

    }//đóng của oncreate
    //
    //getdata
    private void dodulieu(ArrayList<Student> ds) {
        adapter = new SinhvienAdapter(this, ds,this);
        rcvstudent.setLayoutManager(new LinearLayoutManager(this));
        rcvstudent.setAdapter(adapter);
    }
    //
    Callback<Response<ArrayList<Student>>> getListStudentAPI =new Callback<Response<ArrayList<Student>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Student>>> call, retrofit2.Response<Response<ArrayList<Student>>> response) {
            //khi call thành công
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){ //check status code
                    list= response.body().getData();// gán dữ liệu trả về từ phản hồi vào biến ds
                    dodulieu(list);
                    Toast.makeText(MainActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Student>>> call, Throwable t) {
            Log.d(">>>> Student", "onFailure: " + t.getMessage());
        }
    };

    //
    Callback<Response<Student>> responseStudentAPI=new Callback<Response<Student>>() {
        @Override
        public void onResponse(Call<Response<Student>> call, retrofit2.Response<Response<Student>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    //
                    // gọi lại API để load lại dữ liệu sau khi thêm thành công.
                    httpRequest.CallApi().getListStudent().enqueue(getListStudentAPI);//
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Student>> call, Throwable t) {

        }
    };

    //opendialogthem
    private void opendialogthem(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.item_add,null);
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();
        //ánh xa
        EditText txthoten = view.findViewById(R.id.txthotena);
        EditText txtquequan = view.findViewById(R.id.txtquequana);
        EditText txtdiem= view.findViewById(R.id.txtdiema);
        EditText txthinh= view.findViewById(R.id.txthinha);
        Button btnthem = view.findViewById(R.id.btnthem);
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoten=txthoten.getText().toString();
                String quequan=txtquequan.getText().toString();
                double diem=Double.parseDouble(txtdiem.getText().toString());
                String linkhinh=txthinh.getText().toString();
                               Student sv=new Student();
                sv.setHoten(hoten);
                sv.setQuequan(quequan);
                sv.setDiem(diem);
                sv.setHinhanh(linkhinh);
                httpRequest.CallApi().addStudent(sv).enqueue(responseStudentAPI);
//                    Toast.makeText(MainActivity.this, "Nội dung: " + content, Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Đóng dialog sau khi thêm thành công
                Toast.makeText(MainActivity2.this, "insert succ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void Delete(String id) {
        httpRequest.CallApi().deleteStudentById(id).enqueue(responseStudentAPI);
    }

    @Override
    public void Update(String id, Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);//khởi tạo alertdialog, AlertDialog.Builder được sử dụng để xây dựng một hộp thoại cảnh báo (alert dialog), giúp hiển thị thông báo cho người dùng trên màn hình.
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_update, null);
        builder.setView(view);//thiêt lâp view cho hộp thoại
        Dialog dialog = builder.create();//tạo ra dialog
        dialog.show();
        //ánh xa
        EditText txthotenud = view.findViewById(R.id.txthotenud);
        EditText txtquequanud = view.findViewById(R.id.txtquequanud);
        EditText txtdiemud= view.findViewById(R.id.txtdiemud);
        EditText txthinhud= view.findViewById(R.id.txthinhud);
        Button btnsua = view.findViewById(R.id.btnedit);
        //gán dữ liệu lên ô text
        txthotenud.setText(student.getHoten());
        txtquequanud.setText(student.getQuequan());
        txtdiemud.setText(String.valueOf(student.getDiem()));
        txthinhud.setText(student.getHinhanh());
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoten=txthotenud.getText().toString();
                String quequan=txtquequanud.getText().toString();
                double diem=Double.parseDouble(txtdiemud.getText().toString());
                String linkhinh=txthinhud.getText().toString();
                //cập nhật dữ liệu
                student.setHoten(hoten);
                student.setQuequan(quequan);
                student.setDiem(diem);
                student.setHinhanh(linkhinh);
                httpRequest.CallApi().updateStudentById(id,student).enqueue(responseStudentAPI);
                dialog.dismiss();
                Toast.makeText(MainActivity2.this, "update succ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //
}