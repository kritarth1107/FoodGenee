package com.foodgeene.cart;

public class CartModel {
    String itemName,SalePriceCart,cartPrice,quantity,imageUrl;

    public CartModel(){}

    public CartModel(String itemName, String salePriceCart, String cartPrice, String quantity, String imageUrl) {
        this.itemName = itemName;
        this.SalePriceCart = salePriceCart;
        this.cartPrice = cartPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSalePriceCart() {
        return SalePriceCart;
    }

    public void setSalePriceCart(String salePriceCart) {
        SalePriceCart = salePriceCart;
    }

    public String getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
