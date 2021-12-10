package com.example.pinterest;

public class Post {
    public String image;
    public String desc;
    public Post(String image,String desc)
    {
        this.image = image;
        this.desc = desc;
    }

    public String getImage()
    {
        return image;
    }

    public String getDesc()
    {
        return desc;
    }

}
