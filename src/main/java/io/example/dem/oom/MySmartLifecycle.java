package io.example.dem.oom;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

///
/// 说明：此注释风格为Java 23版本开始支持的[JEP 467: Markdown Documentation Comments](https://openjdk.org/jeps/467)
///
/// 描述：TODO
///
/// @author Huang Xiao
/// @version 1.0.0
/// @since 2025/12/29 10:33
///
@Component
public class MySmartLifecycle implements SmartLifecycle {
    @Override
    public void start() {
        System.out.println("MySmartLifecycle begin");
        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        System.out.println("MySmartLifecycle end");
    }

    @Override
    public void stop() {
        System.out.println("MySmartLifecycle stop");
    }

    @Override
    public boolean isRunning() {
        System.out.println("MySmartLifecycle isRunning");
        return false;
    }
}
