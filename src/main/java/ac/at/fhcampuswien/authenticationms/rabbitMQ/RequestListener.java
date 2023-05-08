package ac.at.fhcampuswien.authenticationms.rabbitMQ;


import ac.at.fhcampuswien.authenticationms.config.RabbitMQConfig;
import ac.at.fhcampuswien.authenticationms.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestListener {

    Logger logger = LoggerFactory.getLogger(RequestListener.class);
    @Autowired
    JwtService jwtService;

    @RabbitListener(queues = RabbitMQConfig.GENERATE_TOKEN_MESSAGE_QUEUE)
    public String onGenerateTokenRequest(String eMail) {
        logger.warn("Retrieved request from CustomerAccountMS to generate access token");
        return jwtService.generateToken(eMail, JwtService.Token.AccessToken);
    }

    @RabbitListener(queues = RabbitMQConfig.GENERATE_REFRESH_TOKEN_MESSAGE_QUEUE)
    public String onGenerateRefreshTokenRequest(String eMail) {
        logger.warn("Retrieved request from CustomerAccountMS to generate refresh token");
        return jwtService.generateToken(eMail, JwtService.Token.RefreshToken);
    }
}
