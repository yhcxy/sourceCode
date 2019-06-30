package com.yehui.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    /**
     * 扫描包 得到类
     * @param packageName
     * @return
     */
    public static List<Class> doScanPackage(String packageName){
        String packageUrl = packageName.replaceAll("[.]","/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageUrl);
        List<Class> list = new ArrayList<>();
        if("file".equals(url.getProtocol())){
            list.addAll(doDevScan(url,packageName));
        }else  if("jar".equals(url.getProtocol())){
            list.addAll(doRuntimeScan(url,packageName));
        }
        return list;
    }

    /**
     * 运行环境
     * @param url
     * @param packageName
     * @return
     */
    private static List<Class> doRuntimeScan(URL url, String packageName) {
        List<Class> classes = new ArrayList<Class>();
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry jarEntry = entries.nextElement();
                String urlName = jarEntry.getName().replace("/", ".");
                if(!urlName.startsWith(packageName)){
                    continue;
                }
                if(!urlName.endsWith(".class")){
                    continue;
                }
                //com.yehui.UserMapper.class =>com.yehui.UserMapper
                String classUrl = urlName.substring(0, urlName.lastIndexOf("."));
                try {
                    Class<?> aClass = Class.forName(classUrl);
                    if (aClass.isInterface()){
                        classes.add(aClass);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 开发环境
     * @param url
     * @param packageName
     * @return
     */
    private static List<Class> doDevScan(URL url, String packageName) {
        String filePath = url.getFile();
        File dir = new File(filePath);
        List<Class> classList = new ArrayList<>();
        LinkedList<File> files = new LinkedList<>();
        files.add(dir);
        while(files.size()>0){
            File file = files.removeFirst();
            if(file.isDirectory()){
                int i=0;
                File[] listFiles = file.listFiles();
                for (File listFile : listFiles) {
                    files.add(i++,listFile);
                }
                continue;
            }
            //com.yehui.mapper.UserMapper
            String subPath = file.getAbsolutePath()
                    .substring(dir.getAbsolutePath().length()+1,
                            file.getAbsolutePath().lastIndexOf("."));
            String classUrl = (packageName+"."+subPath);
            try {
                Class<?> aClass = Class.forName(classUrl);
                //判断是否接口
                if(aClass.isInterface()){
                    classList.add(aClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classList;
    }

}
