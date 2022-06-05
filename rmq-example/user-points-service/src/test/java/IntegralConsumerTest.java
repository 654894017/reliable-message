import com.damon.user_points.api.IIntegralApplicationService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class IntegralConsumerTest {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public static void main(String[] args) throws Exception {
        //1
        ReferenceConfig<IIntegralApplicationService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(IIntegralApplicationService.class);
        reference.setVersion("1.0");
        IIntegralApplicationService service = reference.get();
        boolean message1 = service.deductionIntegral(1L, 100L, 1l);
        boolean message2 = service.rollbackDeductionIntegral(100L);
        System.out.println(message1);
        System.out.println(message2);


    }
}
