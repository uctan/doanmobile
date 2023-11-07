package com.example.doanmobile.hoadon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.KhachHang;
import com.example.doanmobile.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter (Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;

    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        Order order = orderList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(order.getOrderDate());
        holder.ngaynguoimuaorder.setText(formattedDate);
        holder.hinhthucnguoimuaorder.setText(order.getHtThanhToan());
        holder.diachinguoimuaorder.setText(order.getDiaChi());
        holder.tongtiennguoimuaorder.setText(String.valueOf(order.getTongTien()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int userID = order.getUserID();
        CollectionReference shopCollectionRef1 = db.collection("KhachHang");
        Query shopQuery1 = shopCollectionRef1.whereEqualTo("userID", userID);
        shopQuery1.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    KhachHang khachHang = document.toObject(KhachHang.class);
                    holder. tennguoimuaorder.setText(khachHang.getTenDayDu());
                }
            } else {
                // Xử lý trường hợp không thành công
            }
        });
        holder.ordercardnhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,xemchitiethoadon.class);
                intent.putExtra("orderID",order.getOrderID());
                intent.putExtra("userID",order.getUserID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tennguoimuaorder, ngaynguoimuaorder,hinhthucnguoimuaorder,diachinguoimuaorder,tongtiennguoimuaorder;
        CardView ordercardnhe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tennguoimuaorder = itemView.findViewById(R.id.tennguoimuaorder);
            ngaynguoimuaorder = itemView.findViewById(R.id.ngaynguoimuaorder);
            hinhthucnguoimuaorder = itemView.findViewById(R.id.hinhthucnguoimuaorder);
            diachinguoimuaorder = itemView.findViewById(R.id.diachinguoimuaorder);
            tongtiennguoimuaorder = itemView.findViewById(R.id.tongtiennguoimuaorder);
            ordercardnhe = itemView.findViewById(R.id.ordercardnhe);
        }
    }
}
