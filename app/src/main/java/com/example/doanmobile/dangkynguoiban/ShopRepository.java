package com.example.doanmobile.dangkynguoiban;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.function.Consumer;

public interface ShopRepository {
    void addShop(Shop shop, Callback<Boolean> callback);
}
