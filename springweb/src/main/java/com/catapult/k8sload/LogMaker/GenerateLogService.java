package com.catapult.k8sload.LogMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service("com.catapult.k8sload.LogMaker.GenerateLogService")
public class GenerateLogService {

    private static final Logger logger = LoggerFactory.getLogger(GenerateLogService.class);
    public static  AtomicLong counter = new AtomicLong(1);
    private final String smallLogMsg="This is a small log message to generate small sized logs, very harmless data.";
    private final String mediumLogMsg="This is a medium log message to generate medium sized logs, this is not an error"+
            " log so please ignore it. Relax and enjoy your coffee, there is nothing to"+
            " see here but just these boring lines of repeated messages";
    private final String largeLogMsg="Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor " +
            "incididunt ut labore et dolore magna aliqua. Magna sit amet purus gravida quis " +
            "blandit turpis. Bibendum at varius vel pharetra vel turpis nunc eget lorem. " +
            "Maecenas sed enim ut sem viverra aliquet eget sit amet. Neque viverra justo nec " +
            "ultrices. Cras ornare arcu dui vivamus. Vitae congue eu consequat ac. Id semper " +
            "risus in hendrerit gravida rutrum quisque non. Elementum eu facilisis sed odio " +
            "morbi quis commodo. Arcu felis bibendum ut tristique et egestas. Est ultricies " +
            "integer quis auctor elit sed vulputate mi. Nulla facilisi nullam vehicula ipsum " +
            "a arcu cursus vitae congue. Eget gravida cum sociis natoque penatibus et magnis " +
            "dis parturient. Consectetur lorem donec massa sapien faucibus.";


    @Async("asyncExecutor")
    public CompletableFuture generateLogs(String size, String delay) throws InterruptedException {
        if (counter.get() == 0) {
            counter.getAndIncrement();
            return CompletableFuture.completedFuture("");
        }

        while(counter.get() > 0) {
            String msg = mediumLogMsg;
            if ("small".equalsIgnoreCase(size)) {
                msg = smallLogMsg;
            } else if ("large".equalsIgnoreCase(size)) {
                msg = largeLogMsg;
            }
            msg = counter.getAndIncrement() + " " + msg;
            logger.info(msg);
            try {
                logger.info("Sleep for " + delay);
                Thread.sleep(getDelay(delay));
            } catch (Exception ie) {
                Thread.currentThread().interrupt();
            }
        }
        return CompletableFuture.completedFuture("");
    }


    public String stoplog() {
        counter.getAndSet(0);
        logger.info("Stopped load test");
        return "Stopped load test";
    }

    private int getDelay(String delay){
        int d = 1000;
        try {
            d = Integer.parseInt(delay);
        } catch (Exception ex){
            logger.error("Invalid delay value " + delay);
        }
        return d;
    }
}