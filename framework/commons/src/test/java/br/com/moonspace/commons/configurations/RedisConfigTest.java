package br.com.moonspace.commons.configurations;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@UnitTest
class RedisConfigTest {

    @Test
    @DisplayName("builds the connection, template and cache from the configured values")
    void shouldBuildRedisBeansWhenValuesArePresent() {
        var config = new RedisConfig();
        ReflectionTestUtils.setField(config, "host", "localhost");
        ReflectionTestUtils.setField(config, "password", "root");
        ReflectionTestUtils.setField(config, "port", 6379);
        ReflectionTestUtils.setField(config, "database", 0);
        ReflectionTestUtils.setField(config, "seconds", 30);
        var connections = mock(RedisConnectionFactory.class);

        LettuceConnectionFactory factory = config.redisConnectionFactory();
        try {
            var template = config.redisTemplate(connections);
            var cache = config.redisCacheManager(connections);

            assertThat(factory.getHostName()).isEqualTo("localhost");
            assertThat(factory.getPort()).isEqualTo(6379);
            assertThat(template.getConnectionFactory()).isSameAs(connections);
            assertThat(cache).isNotNull();
        } finally {
            factory.destroy();
            factory.getClientConfiguration().getClientResources().ifPresent(resources -> resources.shutdown());
        }
    }
}
