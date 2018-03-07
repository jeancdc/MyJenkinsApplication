package com.example.ay31281_dev.myjenkinsapplication;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ay31281-dev on 07/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MyClassInstrumentedTest {

    @Test
    public void myClassTest() throws Exception {

        MyClass myClass = new MyClass("Hello");

        String text = myClass.whatsTheText();

        Assert.assertEquals("Hello", text);

    }

}
