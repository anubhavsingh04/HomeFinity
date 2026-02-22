package com.secure.homefinitybackend.services.impl;

import com.secure.homefinitybackend.exception.ResourceNotFoundException;
import com.secure.homefinitybackend.exception.UnauthorizedException;
import com.secure.homefinitybackend.models.Property;
import com.secure.homefinitybackend.repositories.PropertyRepository;
import com.secure.homefinitybackend.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public Property createPropertyForUser(String userName, String content) {
        Property property = new Property();
        property.setContent(content);
        property.setOwnerUserName(userName);
        return propertyRepository.save(property);
    }

    @Override
    public Property updatePropertyForUser(Long propertyId, String content, String userName) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Property not found with id: " + propertyId));

        if (!property.getOwnerUserName().equals(userName)) {
            throw new UnauthorizedException(
                    "You are not authorized to update this property");
        }

        property.setContent(content);
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getPropertyByOwnerUserName(String userName) {
        return propertyRepository.findByOwnerUserName(userName);
    }

    @Override
    public void deletePropertyForUser(Long propertyId, String userName) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Property not found with id: " + propertyId));

        if (!property.getOwnerUserName().equals(userName)) {
            throw new UnauthorizedException(
                    "You are not authorized to delete this property");
        }

        propertyRepository.delete(property);
    }
}
