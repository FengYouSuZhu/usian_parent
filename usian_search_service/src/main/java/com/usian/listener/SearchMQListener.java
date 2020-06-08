package com.usian.listener;

import com.usian.service.SearchItemService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/7 21:18
 */
@Component
public class SearchMQListener {
    @Autowired
    private SearchItemService searchItemService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="search_queue",durable = "true"),
            exchange = @Exchange(value="item_exchage",type= ExchangeTypes.TOPIC),
            key= {"item.add"}
    ))
    public void listen(String msg) throws Exception {
        System.out.println("接收到消息：" + msg);
        int result = searchItemService.addDocement(msg);
        if(result>0){
            throw new RuntimeException("同步失败");
        }
    }
}
