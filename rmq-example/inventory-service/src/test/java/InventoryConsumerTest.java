

import com.damon.inventory.application.GoodsInventoryDedcutionDTO;
import com.damon.inventory.application.IGoodsInventoryApplicationService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.ArrayList;
import java.util.List;

public class InventoryConsumerTest {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public static void main(String[] args) throws Exception {
        //1
        ReferenceConfig<IGoodsInventoryApplicationService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(IGoodsInventoryApplicationService.class);
        reference.setVersion("1.0");
        IGoodsInventoryApplicationService service = reference.get();
        GoodsInventoryDedcutionDTO dedcutionDTO = new GoodsInventoryDedcutionDTO();
        dedcutionDTO.setOrderId(1L);
        List<GoodsInventoryDedcutionDTO.PlaceOrderGoods> goodsList = new ArrayList<>();
        GoodsInventoryDedcutionDTO.PlaceOrderGoods goods1 = new GoodsInventoryDedcutionDTO.PlaceOrderGoods();
        goods1.setGoodsId(1L);
        goods1.setNumber(1);
        GoodsInventoryDedcutionDTO.PlaceOrderGoods goods2 = new GoodsInventoryDedcutionDTO.PlaceOrderGoods();
        goods2.setGoodsId(2L);
        goods2.setNumber(1);
        goodsList.add(goods1);
        goodsList.add(goods2);
        dedcutionDTO.setGoods(goodsList);
        boolean message1 = service.deductionGoodsInventory(dedcutionDTO);
        //boolean message2 = service.rollbackGoodsInventory(1L);
        System.out.println(message1);
        //System.out.println(message2);


    }
}
