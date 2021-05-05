package com.example.galleryapp7082;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityUnitTest {
    private MainActivity mainActivity = new MainActivity();
    @Mock
    Context mockContext;

    @Test
    public void getFiles_test(){
        mainActivity.setCaptionView();
    }
}
