package products.demo.Config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDetailsConverter implements AttributeConverter<List<Map<String,String>>,String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String ,String>> productDetails){
        try{
            return objectMapper.writeValueAsString(productDetails);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Error converting Product Details to JSON",e);
        }
    }

    @Override
    public List<Map<String,String>> convertToEntityAttribute(String json){
        try{
            if(json==null) return new ArrayList<>();
            return objectMapper.readValue(json,List.class);
        } catch (IOException e){
            throw new RuntimeException("Error reading JSON product details",e);
        }
    }
}
