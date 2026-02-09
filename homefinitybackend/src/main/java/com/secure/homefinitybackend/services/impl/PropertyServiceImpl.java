package com.secure.homefinitybackend.services.impl;

import com.secure.homefinitybackend.models.Property;
import com.secure.homefinitybackend.repositories.PropertyRepository;
import com.secure.homefinitybackend.services.PropertyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Property createPropertyForUser(String userName, String content){
        Property property = new Property();
        property.setContent(content);
        property.setOwnerUserName(userName);
        return propertyRepository.save(property);
    }

    @Override
    public Property updatePropertyForUser(Long propertyId, String content, String userName) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new RuntimeException("Property not found"));
        property.setContent(content);
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getPropertyByOwnerUserName(String userName) {
        return propertyRepository.findByOwnerUserName(userName);
    }

    @Override
    public void deletePropertyForUser(Long propertyId, String userName) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new RuntimeException("Property not found"));
        propertyRepository.delete(property);
    }
}
