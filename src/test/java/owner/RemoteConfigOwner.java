package owner;
import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/remote.properties"
})
public interface RemoteConfigOwner extends Config {
    String url();
}
