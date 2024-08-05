package com.example.baitestthu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baitestthu.R;
import com.example.baitestthu.handle.Item_Handle;
import com.example.baitestthu.model.Student;

import java.util.ArrayList;

public class SinhvienAdapter extends RecyclerView.Adapter<SinhvienAdapter.viewholder> {
    private Context context;
    private final ArrayList<Student> list;
    private Item_Handle handle;

    public SinhvienAdapter(Context context, ArrayList<Student> list, Item_Handle handle) {
        this.context = context;
        this.list = list;
        this.handle = handle;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_sv, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Student sv = list.get(position);//truy cập đến đối tượng đang chọn tai vi tri đang chọn position
        //gán dữ liệu lên view
        holder.txthoten.setText("Họ tên:" + sv.getHoten());
        holder.txtquequan.setText("Quê quán:" + sv.getQuequan());
        holder.txtdiem.setText("Điểm:" + sv.getDiem());
        Glide.with(context)
                .load(sv.getHinhanh())
                .into(holder.imghinh);
        //nút xóa
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handle.Delete(sv.getId());
            }
        });
        //nút update
        holder.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handle.Update(sv.getId(), sv);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView txthoten, txtquequan, txtdiem;
        ImageView imghinh;
        ImageButton btnupdate, btndelete;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            //ánh xạ
            txthoten = itemView.findViewById(R.id.txthoten);
            txtdiem = itemView.findViewById(R.id.txtdiem);
            txtquequan = itemView.findViewById(R.id.txtquequan);
            imghinh = itemView.findViewById(R.id.imghinh);
            btndelete = itemView.findViewById(R.id.btndelete);
            btnupdate = itemView.findViewById(R.id.btnupdate);
        }
    }

}
