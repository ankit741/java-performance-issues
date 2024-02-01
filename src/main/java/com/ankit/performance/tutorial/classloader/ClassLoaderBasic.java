package com.ankit.performance.tutorial.classloader;

import com.sun.crypto.provider.AESKeyGenerator;
import java.util.ArrayList;

public class ClassLoaderBasic {

  public static void main(String[] args) {
    printClassLoaderDetails();
  }

  private static void printClassLoaderDetails() {
    System.out.println("java.boot.class.dirs : "+ System.getProperty("sun.boot.class.path"));
    System.out.println("java.ext.dirs : " + System.getProperty("java.ext.dirs"));
    System.out.println( "java.class.path : " +System.getProperty("java.class.path"));
    System.out.println("com.sun.crypto.provider.AESKeyGenerator : " + AESKeyGenerator.class.getClassLoader());
    System.out.println("ClassLoaderBasic : " + ClassLoaderBasic.class.getClassLoader());
    System.out.println("String array : " + String[].class.getClassLoader());
    System.out.println("ArrayList : " + ArrayList.class.getClassLoader());
  }
}
