package jvm;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyclassLoader extends ClassLoader{

    public static void main(String[] args) throws Exception {
        // 相关参数
        final String className = "Hello";
        final String methodName = "hello";
        // 创建类加载器
        ClassLoader classLoader = new MyclassLoader();
        // 加载相应的类
        Class<?> clazz = classLoader.loadClass(className);
        // 看看里面有些什么方法
        Method[] method = clazz.getDeclaredMethods();
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println("Method:"+clazz.getSimpleName() + "." + m.getName());
        }
//        // 创建对象
        Object instance = clazz.getDeclaredConstructor().newInstance();

        for (Method med : method){
            med.invoke(instance);
        }
//        // 调用实例方法
//        Method method = clazz.getMethod(methodName);
//        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 如果支持包名, 则需要进行路径转换
        String resourcePath = name.replace(".", "/");
        System.out.println(name);
        System.out.println(resourcePath);
        // 文件后缀
        final String suffix = ".xlass";
        // 获取输入流
        System.out.println(this.getClass().getClassLoader().getResource(resourcePath + suffix));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath + suffix);
        try {
            // 读取数据
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            // 转换
            byte[] classBytes = decode(byteArray);
            // 通知底层定义这个类
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            close(inputStream);
        }

    }

    // 解码
    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }

    // 关闭
    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
