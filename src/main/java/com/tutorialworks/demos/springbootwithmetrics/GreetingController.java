package com.tutorialworks.demos.springbootwithmetrics;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

// 添加redis和日志输出
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
@Slf4j
//
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

// 导入redis连接工具
@Autowired
    private RedisTemplate redisTemplate;

    private final MeterRegistry registry;

    /**
     * We inject the MeterRegistry into this class
     */
    public GreetingController(MeterRegistry registry) {
        this.registry = registry;
    }

    /**
     * The @Timed annotation adds timing support, so we can see how long
     * it takes to execute in Prometheus
     * percentiles
     */
    @GetMapping("/greeting")
    @Timed(value = "greeting.time", description = "Time taken to return greeting",
            percentiles = {0.5, 0.90})
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

	//增加redis日志输出
        log.info("set redis key:aaaa=aaaa");
        //redis的操作
	redisTemplate.opsForValue().set("aaaa", "aaaa");
        redisTemplate.opsForValue().set("bbbb", "bbbb");
        redisTemplate.opsForValue().set("cccc", "cccc");
        //记录redis操作日志
	log.info("get redis key:aaaa={}.",redisTemplate.opsForValue().get("aaaa"));

        registry.counter("greetings.counter").increment();
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
