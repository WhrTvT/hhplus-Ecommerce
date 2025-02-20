package kr.hhplus.be.server.interfaces.support.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class JsonUtil {

    private JsonUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final String ERROR = "ERROR : ";

    public static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
            .deserializerByType(LocalDate.class, new LocalDateTypeConvertDeserializer())
            .deserializerByType(LocalDateTime.class, new LocalDateTimeTypeConvertDeserializer())
            .featuresToEnable(SerializationFeature.INDENT_OUTPUT) // Pretty Printing 활성화
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // dto 에 존재 하지 않는 속성이 있는 경우 무시
            .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY) // public 필드 / getter 메서드 유무 상관 없이 직렬화
            .build();

    /**
     * Java Object => Json 직렬화된 String 반환
     */
    public static String objectToString(Object javaObject) {
        String jsonString;
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(javaObject);
        } catch (JsonProcessingException e) {
            log.error(ERROR, e);
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    /**
     * String Object 로 변환
     */
    public static <T> T stringToObject(String request, Class<T> valueType) {
        if (StringUtils.hasLength(request)) return null;

        T returnValue;
        try {
            returnValue = OBJECT_MAPPER.readValue(request, valueType);
        } catch (JsonProcessingException e) {
            log.error(ERROR, e);
            throw new RuntimeException(e);
        }
        return returnValue;
    }
}
