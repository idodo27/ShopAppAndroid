package com.micdoz.ShopApp.baseObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * In this class we have used the "Parcelable" class in order to transfer data between activities,
 * Specially the "Product" Object.
 */

public class Product implements Parcelable {

    /**
     * Class variables
     */

    private String productName;
    private String productID;
    private double productPrice;

    /**
     * Class constructors
     * @param name
     * @param id
     * @param price
     */

    public Product(String name, String id, double price) {

        this.setProductName(name);
        this.setProductID(id);
        this.setProductPrice(price);

    }

    protected Product(Parcel in) {
        productName = in.readString();
        productID = in.readString();
        productPrice = in.readDouble();
    }


    /**
     * Getters
     * @return
     */

    public double getProductPrice() {
        return productPrice;
    }
    public String getProductID() {
        return productID;
    }
    public String getProductName() {
        return productName;
    }

    /**
     * Setters
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Parcel methods
     * @param dest
     * @param flags
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(productID);
        dest.writeDouble(productPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };



    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productID='" + productID + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }



    public String shopString(){
        return productID + "  -  " +productName + " - - - - - - " + productPrice + " $";
    }


}