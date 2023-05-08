package ac.at.fhcampuswien.authenticationms.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshTokenDTO {
    private String refreshToken;
}
