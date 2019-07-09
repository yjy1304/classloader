package com.gemyoung.classloader;

import java.io.*;

/**
 * Created by weilong on 2019/7/9.
 */
public class FileSystemClassLoader extends ClassLoader{
    private String rootDir;

    public FileSystemClassLoader(String rootDir){
        this.rootDir = rootDir;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException{
        //从类的全限定名获取类的源码文件并转换成字节数组
        byte[] classData = getClassData(className);
        if(classData == null){
            throw new ClassNotFoundException();
        }else {
            //将源文件的字节数组转换成Class对象
            return defineClass(className, classData, 0, classData.length);
        }
    }

    private byte[] getClassData(String className){
        String path = classNameToPath(className);
        try{
            InputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesNumRead = 0;
            while((bytesNumRead = inputStream.read(buffer)) != -1){
                //字节数组输出流本身就可以存储数据
                byteArrayOutputStream.write(buffer, 0, bytesNumRead);
            }
            //最后通过toByteArray()转换存储在字节数组输出流中的数据
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private String classNameToPath(String className){
        return rootDir + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
    }
}
