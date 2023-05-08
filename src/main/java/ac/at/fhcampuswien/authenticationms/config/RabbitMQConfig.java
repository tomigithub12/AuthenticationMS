package ac.at.fhcampuswien.authenticationms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String AUTH_EXCHANGE = "auth_exchange";
    public static final String GENERATE_TOKEN_MESSAGE_QUEUE = "generateToken_msg_queue";

    public static final String GENERATE_REFRESH_TOKEN_MESSAGE_QUEUE = "generateToken_msg_queue";

    public static final String CUSTOMER_EXISTENCE_MESSAGE_QUEUE = "customerExistence_msg_queue";



    @Bean
    Queue msgQueue1() {

        return new Queue(GENERATE_TOKEN_MESSAGE_QUEUE);
    }

    @Bean
    Queue msgQueue2() {

        return new Queue(GENERATE_REFRESH_TOKEN_MESSAGE_QUEUE);
    }

    @Bean
    Queue msgQueue3() {

        return new Queue(CUSTOMER_EXISTENCE_MESSAGE_QUEUE);
    }


    @Bean
    TopicExchange exchange() {

        return new TopicExchange(AUTH_EXCHANGE);
    }

    @Bean
    Binding msgBinding1() {

        return BindingBuilder.bind(msgQueue1()).to(exchange()).with(GENERATE_TOKEN_MESSAGE_QUEUE);
    }

    @Bean
    Binding msgBinding2() {

        return BindingBuilder.bind(msgQueue2()).to(exchange()).with(GENERATE_REFRESH_TOKEN_MESSAGE_QUEUE);
    }

    @Bean
    Binding msgBinding3() {

        return BindingBuilder.bind(msgQueue3()).to(exchange()).with(CUSTOMER_EXISTENCE_MESSAGE_QUEUE);
    }


    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }



}
