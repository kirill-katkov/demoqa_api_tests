package owner;
import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/links.properties"
})
public interface LinksConfigOwner extends Config  {
    String uiURL();
    String apiURI();
}
