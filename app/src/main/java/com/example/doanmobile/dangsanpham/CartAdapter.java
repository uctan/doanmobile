package com.example.doanmobile.dangsanpham;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItems;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }


    @Override
    public int getCount() {
        if (cartItems == null) {
            return 0;
        }
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cart_item, null);
        }

        CartItem cartItem = cartItems.get(position);

        ImageView productImage = view.findViewById(R.id.productImage);
        TextView productName = view.findViewById(R.id.productName);
        TextView productPrice = view.findViewById(R.id.productPrice);
        ImageView removeButton = view.findViewById(R.id.removeButton);
        TextView quantity = view.findViewById(R.id.quantity);

        View cong = view.findViewById(R.id.congcart);
        View tru = view.findViewById(R.id.trucart);

        productName.setText(cartItem.getTitle());
        productPrice.setText(String.valueOf(cartItem.getPrice()));
        quantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context)
                .load(cartItem.getImageURL())
                .into(productImage);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartManager.getInstance().removeFromCart(cartItem);
                notifyDataSetChanged();
            }
        });

        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.getPrice();

                notifyDataSetChanged();

            }
        });

        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    cartItem.getPrice();
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
