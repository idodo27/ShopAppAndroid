package com.micdoz.ShopApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.micdoz.ShopApp.baseObjects.Cart;
import com.micdoz.ShopApp.baseObjects.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * This class will be used when the user has succesfully logged in to the application.
 * The class will include all the shopping list's properties and acts as a main page.
 */

public class ShoppingActivity extends AppCompatActivity {

    /**
     * Class variables
     */

    private String shoppingItems;
    private String[] itemsArr;
    TextView userName;
    Cart cart;
    ListView productListView;
    ListView cartListView;
    ArrayList<String> items;
    ArrayList<String> cartItems;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> cartAdapter;
    Random rand;
    ArrayList<Product> products;
    ArrayList<Product> cartProducts;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String userId;
    int total = 0;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    Button cartClearBtn;
    Button toProfBtn;
    TextView totalView;
    Intent showProf;
    Button hide, showCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        rand = new Random();
        userName = (TextView) findViewById(R.id.nameForUser);
        toProfBtn = (Button) findViewById(R.id.toProfileButton);
        hide = (Button) findViewById(R.id.hideCart);
        showCart = (Button) findViewById(R.id.showCart);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        totalView = (TextView) findViewById(R.id.sum);
        if(fUser == null){
            Toast.makeText(this, "no user", Toast.LENGTH_SHORT).show();
        }
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("shopapp-e8021-default-rtdb");
        if(getIntent().getStringExtra("user_id") == null){
            userId = fUser.getUid();
        }
        else{
            userId = getIntent().getStringExtra("user_id");
        }

        dbRef.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ShoppingActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap h = (HashMap)  Objects.requireNonNull(task.getResult()).getValue();
                    userName.setText("Hello " + h.get("fName") + " " + h.get("lName"));
                    userName.setTypeface(null, Typeface.BOLD);
                }
            }
        });

        if(cart == null) {
            cart = new Cart(fUser.getUid(), this, fAuth, fUser);
        }
        cartListView = (ListView)findViewById(R.id.cart_list);
        productListView = (ListView)findViewById(R.id.product_list);
        shoppingItems = "allspice basil bay-leaves black-pepper" +
                " cardamom cayennne-pepper chili-pepper cinnamon cloves " +
                "coriander cumin curry dill garam-masala ginger mint mustard " +
                "nutmeg oregano red-pepper flakes paprika rosemary sage artichokes " +
                "asparagus broccoli brussel-sprouts cabbage celery cucumber green-beans" +
                " leafy-greens green-onions green-peppers snow peas spinach zucchini" +
                " beets radishes red-pepper red-potatoes tomatoes carrots squash sweet-potatoes" +
                " yellow-pepper cauliflower garlic leeks ginger mushrooms onions parsnips turnips " +
                "eggplant red-cabbage avocados green-apples green-grapes melon kiwi limes green-pears " +
                "berries cherries pink-grapefruit red-grapes watermelon apricots cantaloupe lemons mangoes " +
                "nectarines oranges papayas peaches pineapple tangerines Chicken-Nuggets Pizza-Dough Tea Butter " +
                "Cheese Eggs Milk Tortillas Yogurt Bacon Chicken Ostrich Sausage Flounder Haddock Halibut Red-snapper Salmon";
        itemsArr = shoppingItems.split(" ");
        items = new ArrayList<>();
        products = new ArrayList<>();
        items.addAll(Arrays.asList(itemsArr));
        for(int i = 0 ; i < items.size() ; i++){
            products.add(new Product(items.get(i), "" + i, rand.nextInt(20)+1));
        }
        for(int i = 0 ; i < items.size() ; i++){
            items.set(i, products.get(i).shopString());
        }
        adapter = new ArrayAdapter<>(this.getApplicationContext(), android.R.layout.simple_list_item_1, items);
        productListView.setAdapter(adapter);
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                cart.addProduct(products.get(i));
                Toast.makeText(ShoppingActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
            }
        });
        cartListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                total -= cart.getProducts()[i].getProductPrice();
                cart.removeProduct(cart.getProducts()[i]);
                totalView.setText("total : " + total + " $");
                Toast.makeText(ShoppingActivity.this, "Product Removed", Toast.LENGTH_SHORT).show();
                cartItems.remove(i);
                cartAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    /**
     * This method is used to display the cart content on the screen.
     * @param view
     */

    @SuppressLint("SetTextI18n")
    public void showUserCart(View view) {
        if(getIntent().getParcelableArrayListExtra("products") != null){
            cartProducts = getIntent().getParcelableArrayListExtra("products");
            cart = getIntent().getParcelableExtra("cart");
        }
        else{
            cartProducts = new ArrayList<>();
            cartProducts.addAll(Arrays.asList(cart.getProducts()));
        }

        cartItems = new ArrayList<>();

        for(int i = 0 ; i < cartProducts.size() ; i++) {
            if (cartProducts.get(i) != null) {
                cartItems.add(cartProducts.get(i).shopString());
            }
        }
        cartAdapter = new ArrayAdapter<>(this.getApplicationContext(), android.R.layout.simple_list_item_1, cartItems);
        cartListView.setAdapter(cartAdapter);
        productListView.setVisibility(View.INVISIBLE);
        cartListView.setVisibility(View.VISIBLE);
        cartClearBtn = findViewById(R.id.clearCart);
        cartClearBtn.setVisibility(View.VISIBLE);
        total = cart.getSum();
        totalView.setText("total : " + total + " $");
        totalView.setTypeface(null, Typeface.BOLD);
        totalView.setVisibility(View.VISIBLE);
        showCart.setVisibility(View.INVISIBLE);
        hide.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Long press on an item to delete",Toast.LENGTH_SHORT).show();

    }

    /**
     * This method is used to disconnect to current user from the application.
     * @param view
     */

    public void logOut(View view){

        if(fUser != null){
            if (fAuth != null){
                fAuth.signOut();
                fUser.reload();
                finish();
            }
        }
    }

    /**
     * This method is used to move to the user's profile page.
     * @param view
     */

    public void showProfile(View view){

        showProf = new Intent(ShoppingActivity.this, ProfileActivity.class);
        if(cart.getProducts()[0] != null) {
            showProf.putParcelableArrayListExtra("products", new ArrayList<>(Arrays.asList(cart.getProducts())));
            showProf.putExtra("cart", cart);
        }
        startActivity(showProf);

    }

    /**
     * This method is used to clear all the cart's content.
     * @param view
     */

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("SuspiciousListRemoveInLoop")
    public void clearCart(View view){
        cart.clearProducts(view);
        cartListView.setAdapter(null);
        getIntent().putParcelableArrayListExtra("products", null);
        getIntent().putExtra("cart", (Bundle) null);
        total = 0;
        totalView.setText("total : " + total + " $");
    }

    /**
     * This method is used to hide the cart content after the user's has seen it.
     * @param view
     */

    public void hideMyCart(View view) {
        hide.setVisibility(View.INVISIBLE);
        showCart.setVisibility(View.VISIBLE);
        productListView.setVisibility(View.VISIBLE);
        cartListView.setVisibility(View.INVISIBLE);
        totalView.setVisibility(View.INVISIBLE);
    }
}