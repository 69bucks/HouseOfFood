package com.example.houseoffood.Model;

import java.io.Serializable;

public class Food implements Serializable, Comparable<Food> {
    String name;
    String image;
    int price;
    int rate;
    String foodKey;
    String des;

    public Food() {
    }

    public Food(String name, String image, int price, int rate, String foodKey,String des) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.rate = rate;
        this.foodKey = foodKey;
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getFoodKey() {
        return foodKey;
    }

    public void setFoodKey(String foodKey) {
        this.foodKey = foodKey;
    }

    public String getDes(){return des;}

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", price=" + price +
                ", rate=" + rate +
                ", foodKey='" + foodKey + '\'' +
                '}';
    }

    @Override
    public int compareTo(Food food) {
        return Integer.compare( food.rate, rate);
    }
}

