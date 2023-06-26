package sia.tacocloud.models;

import lombok.Data;

@Data
public class TacoToken {
    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken;
    private int expiresIn;
   
}
