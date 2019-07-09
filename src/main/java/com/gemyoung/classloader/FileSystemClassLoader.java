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
        //�����ȫ�޶�����ȡ���Դ���ļ���ת�����ֽ�����
        byte[] classData = getClassData(className);
        if(classData == null){
            throw new ClassNotFoundException();
        }else {
            //��Դ�ļ����ֽ�����ת����Class����
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
                //�ֽ��������������Ϳ��Դ洢����
                byteArrayOutputStream.write(buffer, 0, bytesNumRead);
            }
            //���ͨ��toByteArray()ת���洢���ֽ�����������е�����
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
