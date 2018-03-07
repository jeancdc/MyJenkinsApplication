package com.example.ay31281_dev.myjenkinsapplication;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ay31281-dev on 07/03/2018.
 */
public class MyClassUnitTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void whatsTheText() {

        MyClass myClass = new MyClass("Hello");

        Assert.assertEquals("Hello", myClass.whatsTheText());
    }

}