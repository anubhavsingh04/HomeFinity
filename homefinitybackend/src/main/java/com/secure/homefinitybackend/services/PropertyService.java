package com.secure.homefinitybackend.services;
import com.secure.homefinitybackend.models.Property;

import java.util.List;

public interface PropertyService {
    Property createPropertyForUser(String userName, String content);

    Property updatePropertyForUser(Long propertyId, String content, String userName);

    List<Property> getPropertyByOwnerUserName(String ownerUserName);

    void deletePropertyForUser(Long propertyId, String userName);
}
