package config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * Created by fiyyanp on 10/13/2016.
 */
public class Config extends Configuration{
    @JsonProperty
    private String rabbitMQHost;
    @JsonProperty
    private String rabbitMQUsername;
    @JsonProperty
    private String rabbitMQPassword;
    @JsonProperty
    private String rabbitMQVHost;
    @JsonProperty
    private String mongoDBhost;
    @JsonProperty
    private Boolean mongoDBisAuth;
    @JsonProperty
    private String mongoDBuser;
    @JsonProperty
    private String mongoDBpassword;
    @JsonProperty
    private String mongoDBname;

    /* getter */
    public String getRabbitMQHost() {
        return rabbitMQHost;
    }

    public String getRabbitMQUsername() {
        return rabbitMQUsername;
    }

    public String getRabbitMQPassword() {
        return rabbitMQPassword;
    }

    public String getRabbitMQVHost() {
        return rabbitMQVHost;
    }

    public String getMongoDBhost() {
        return mongoDBhost;
    }

    public Boolean getMongoDBisAuth() {
        return mongoDBisAuth;
    }

    public String getMongoDBuser() {
        return mongoDBuser;
    }

    public String getMongoDBpassword() {
        return mongoDBpassword;
    }

    public String getMongoDBname() {
        return mongoDBname;
    }

}
