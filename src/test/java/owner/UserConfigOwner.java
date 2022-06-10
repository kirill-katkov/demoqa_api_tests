package owner;

import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/user.properties"
})
public interface UserConfigOwner extends Config{
    String userLogin();
    String userPassword();
}
