

import com.damon.inventory.application.GoodsInventoryDedcutionDTO;
import com.damon.inventory.application.IGoodsInventoryApplicationService;
import com.damon.order.api.IOrderApplicationService;
import com.damon.order.api.OrderCreateDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.ArrayList;
import java.util.List;

public class OrderConsumerTest {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public static void main(String[] args) throws Exception {
        //1
        ReferenceConfig<IOrderApplicationService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(IOrderApplicationService.class);
        reference.setVersion("1.0");
        reference.setRetries(-1);
        IOrderApplicationService service = reference.get();
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setOrderId(2L);
        orderCreateDTO.setDeductionPoints(10L);
        orderCreateDTO.setGivePoints(5L);
        orderCreateDTO.setUserId(2L);

        OrderCreateDTO.OrderItemDTO orderItemDTO = new OrderCreateDTO.OrderItemDTO();
        orderItemDTO.setNumber(1);
        orderItemDTO.setGoodsId(1L);

        OrderCreateDTO.OrderItemDTO orderItemDTO2 = new OrderCreateDTO.OrderItemDTO();
        orderItemDTO2.setNumber(1);
        orderItemDTO2.setGoodsId(2l);

        orderCreateDTO.setItems(Lists.newArrayList(orderItemDTO, orderItemDTO2));

        Long orderId = service.placeOrder(orderCreateDTO);
        System.out.println(orderId);

    }
}
