package com.micdoz.ShopApp.baseObjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.micdoz.ShopApp.ShoppingActivity;

/**
 * In this class we have used the "Parcelable" class in order to transfer data between activities,
 * Specially the "Cart" Object.
 */

public class Cart implements Parcelable {

    /**
     * Class variables
     */

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private String cartID;
    private Product[] products;
    private int productCount;
    private ShoppingActivity activity;

    /**
     * Class constructors
     * @param id
     * @param activity
     * @param fAuth
     * @param fUser
     */

    public Cart(String id, ShoppingActivity activity, FirebaseAuth fAuth, FirebaseUser fUser){
        this.setCartID(id);
        this.setProducts();
        this.setfAuth(fAuth);
        this.setfUser(fUser);
        this.setActivity(activity);
    }

    protected Cart(Parcel in) {
        fUser = in.readParcelable(FirebaseUser.class.getClassLoader());
        cartID = in.readString();
        products = in.createTypedArray(Product.CREATOR);
        productCount = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    /**
     * Getters
     * @return
     */

    public String getCartID() {
        return cartID;
    }
    public Product[] getProducts() {
        return products;
    }
    public FirebaseAuth getfAuth() {
        return fAuth;
    }
    public FirebaseUser getfUser() {
        return fUser;
    }
    public ShoppingActivity getActivity() {
        return activity;
    }
    // This method is used to get the current product count inside the cart.
    public int getProductCount() {
        int count = 0;
        for(int i = 0; i < this.getProducts().length; i++){
            if(this.getProducts()[i] == null){
                break;
            }else{
                count++;
            }
        }

        return count;
    }
    // This method is used to calculate the total sum of the item's value inside the cart.
    public int getSum(){
        int sum = 0;
        for(int i = 0; i < this.getProductCount() ;i++){
            sum += this.getProducts()[i].getProductPrice();
        }
        return sum;
    }

    /**
     * Setters
     * @param cartID
     */

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }
    public void setProducts() {
        this.products = new Product[100];
    }
    public void setActivity(ShoppingActivity activity) {
        this.activity = activity;
    }
    public void setfAuth(FirebaseAuth fAuth) {
        this.fAuth = fAuth;
    }
    public void setfUser(FirebaseUser fUser) {
        this.fUser = fUser;
    }

    public boolean isAvailable() {

        if(this.getfUser() == null) {

            return true;
        }

        return false;
    }

    /**
     * This method is used to add a product to the user's cart.
     * @param product
     */

    public void addProduct(Product product) {

        Product[] products = this.getProducts();

        for(int i = 0 ; i < products.length ; i++ ) {

            if(products[i] != null) {

                if(products[i].getProductName().equals(product.getProductName())) {
                    String s = "You have already taken this product.";
                    Toast.makeText(getActivity().getBaseContext(), s, Toast.LENGTH_LONG).show();
                }
            }

            if(products[i] == null) {

                products[i] = product;
                break;
            }

            if(i==products.length-1 && products[i] != null) {
                String s = "The cart is in full capacity.";
                Toast.makeText(getActivity().getBaseContext(), s, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void removeProduct(Product product) {

        Product[] products = this.getProducts();

        int ind = 0;
        int counter = 0;
        String indexes = "";
        for(int i = 0 ; i < products.length ; i++) {

            if(products[i] != null) {
                if(products[i].getProductName().equals(product.getProductName())) {
                    ind = i;
                    indexes += (i+1) + " ";

                    counter++;
                }
            }
        }
        if(counter > 1) {

            for(int i = ind ; i > 0 ; i/=10) {

                products[i%10] = null;
            }
        }

        else if(counter==1){

            products[ind] = null;

        }

        else {
            Toast.makeText(getActivity(), "No Such Product", Toast.LENGTH_SHORT).show();
        }

        this.fixProductArray();
    }

    /**
     * This private method is used to fix the product array from null object and will move all the
     * null objects to the end.
     */

    private void fixProductArray() {
        int length = this.getProducts().length;

        for (int i = 0; i < length; i++) {
            if (products[i] == null) {
                for (int j = i; j < length; j++) {
                    if (products[j] != null) {
                        Product tmp = products[j];
                        products[j] = null;
                        products[i] = tmp;

                        break;

                    }
                }

            } else {
                continue;
            }

        }

    }

    /**
     * This method is used to clear the products from the cart.
     * @param view
     */
    public void clearProducts(View view){
        this.products = new Product[100];
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(fUser, i);
        parcel.writeString(cartID);
        parcel.writeTypedArray(products, i);
        parcel.writeInt(productCount);
    }
}