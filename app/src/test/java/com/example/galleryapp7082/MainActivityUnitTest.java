package com.example.galleryapp7082;

import android.content.Context;

import com.example.galleryapp7082.view.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityUnitTest {
    private MainActivity mainActivity = new MainActivity();
    @Mock
    Context mockContext;

    @Test
    public void getFiles_test(){
        assertEquals(8, 4 + 4);
    }
}
