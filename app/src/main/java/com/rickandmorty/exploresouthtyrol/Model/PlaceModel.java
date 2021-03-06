package com.rickandmorty.exploresouthtyrol.Model;

import com.google.gson.annotations.SerializedName;

public class PlaceModel {

    @SerializedName("title")
    public String title;

    @SerializedName("text")
    public String description;

    @SerializedName("x")
    public float x;

    @SerializedName("y")
    public float y;

    @SerializedName("z")
    public float z;

    @SerializedName("dist")
    public float distance;

    @SerializedName("type")
    public String type;

    @Override
    public String toString() {
        return "PlaceModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", distance=" + distance +
                '}';
    }


}
