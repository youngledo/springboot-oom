package io.example.dem.oom.controller;

import io.example.dem.oom.service.OutOfMemoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

///
/// 说明：此注释风格为Java 23版本开始支持的[JEP 467: Markdown Documentation Comments](https://openjdk.org/jeps/467)
///
/// 描述：OOM
///
/// @author Huang Xiao
/// @version 1.0.0
/// @since 2025/11/11 10:22
///
@RestController
@RequestMapping("/oom")
public class OutOfMemoryController {

    private final OutOfMemoryService outOfMemoryService;

    public OutOfMemoryController(OutOfMemoryService outOfMemoryService) {
        this.outOfMemoryService = outOfMemoryService;
    }

    @GetMapping("ok")
    public String ok() {
        return "ok";
    }

    @GetMapping("heap")
    public void heap() {
        outOfMemoryService.heap();
    }

    @GetMapping("direct")
    public void direct(@RequestParam(defaultValue = "true") boolean isClear) {
        outOfMemoryService.direct(isClear);
    }

    @GetMapping("metaspace")
    public void metaspace() throws ClassNotFoundException {
        outOfMemoryService.metaspace();
    }
}
