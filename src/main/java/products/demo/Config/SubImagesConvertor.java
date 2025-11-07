package products.demo.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SubImagesConvertor implements AttributeConverter<List<String>,String> {
    private static final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> subImages){
        try{
            return objectMapper.writeValueAsString(subImages);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException("Error converting");
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String jsonImages){
        try{
            if(jsonImages==null || jsonImages.isEmpty()){
                return new ArrayList<>();
            }
            return objectMapper.readValue(jsonImages,List.class);
        }
        catch (IOException e){
            throw new RuntimeException("Error Reading",e);
        }
    }
}
