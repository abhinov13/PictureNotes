package com.example.picturenotes;

public class Post {
    public String image;
    public String desc;
    public String category;
    public Post(String image,String desc,String category)
    {
        this.image = image;
        this.desc = desc;
        this.category = category;
    }

    public String getImage()
    {
        return image;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getCategory() { return category; }
}
