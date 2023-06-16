package com.example.chatgptapp.model;

import com.example.chatgptapp.R;
import com.example.chatgptapp.appView.BookNoteAppView;
import com.example.chatgptapp.appView.StoryAppView;

import java.util.Arrays;
import java.util.List;

public class App {

    public static List<App> Apps = Arrays.asList(
            new App("故事创作", R.mipmap.story_create,StoryAppView.class),
            new App("阅读书签", R.mipmap.book_note, BookNoteAppView.class)
    );

    private String name;
    private int imageResId;
    private Class classView;

    public App(String name, int imageResId, Class a) {
        this.name = name;
        this.imageResId = imageResId;
        this.classView = a;
    }


    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public Class getClassView() {
        return classView;
    }
}
