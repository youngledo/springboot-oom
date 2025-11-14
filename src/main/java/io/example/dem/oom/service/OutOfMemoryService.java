package io.example.dem.oom.service;

import org.springframework.stereotype.Service;
import sun.nio.ch.DirectBuffer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

///
/// 说明：此注释风格为Java 23版本开始支持的[JEP 467: Markdown Documentation Comments](https://openjdk.org/jeps/467)
///
/// 描述：TODO
///
/// @author Huang Xiao
/// @version 1.0.0
/// @since 2025/11/11 10:20
///
@Service
public class OutOfMemoryService {

    static DirectBuffer bb;

    public void heap() {
        List<byte[]> list = new ArrayList<>();
        int i = 0;
        while (true) {
            // 每次分配 1MB
            list.add(new byte[1024 * 1024]);
            i++;
            if (i % 10 == 0) {
                System.out.println("Allocated " + i + " MB");
            }
            // 小睡一下让输出更容易看到（可选）
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void direct(boolean isClear) {
        List<DirectBuffer> list = new ArrayList<>();
        int i = 0;
        while (true) {
            // 分配 1MB 的直接内存
            DirectBuffer bb = (DirectBuffer) ByteBuffer.allocateDirect(1024 * 1024);
            list.add(bb);
            i++;
            if (i % 10 == 0) System.out.println("Allocated " + i + " MB direct buffer");
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            if (isClear) {
                bb.cleaner().clean();
            }
        }
    }

    public void metaspace() throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        List<ClassLoader> loaders = new ArrayList<>();
        int count = 0;
        while (true) {
            MyLoader loader = new MyLoader();
            // ✅ 使用完整类名（包含包名）
            Class<?> clazz = loader.loadClass("io.example.dem.oom.service.OutOfMemoryService$Dummy");
            classes.add(clazz);
            loaders.add(loader);
            count++;
            if (count % 100 == 0) {
                System.out.println("Loaded " + count + " classes");
            }
        }
    }

    public static class Dummy {
        public void hello() {
        }
    }

    static class MyLoader extends ClassLoader {
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            // 只重新加载 Dummy 类
            if (!"io.example.dem.oom.service.OutOfMemoryService$Dummy".equals(name)) {
                return super.loadClass(name, resolve);
            }

            // 构建正确资源路径
            String resourcePath = "/" + name.replace('.', '/') + ".class";
            try {
                try (InputStream in = OutOfMemoryService.class.getResourceAsStream(resourcePath)) {
                    if (in == null) {
                        throw new ClassNotFoundException("Cannot find resource for " + name + " (path=" + resourcePath + ")");
                    }
                    byte[] bytes = readFully(in);
                    Class<?> c = defineClass(name, bytes, 0, bytes.length);
                    if (resolve) resolveClass(c);
                    return c;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static byte[] readFully(InputStream in) throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        }
    }
}
