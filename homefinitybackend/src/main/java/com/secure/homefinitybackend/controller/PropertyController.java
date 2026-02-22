package com.secure.homefinitybackend.controller;

import com.secure.homefinitybackend.models.Property;
import com.secure.homefinitybackend.services.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public Property createProperty(@RequestBody String content,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        log.info("Creating property for user: {}", userName);
        return propertyService.createPropertyForUser(userName, content);
    }

    @PutMapping("/{propertyId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public Property updateProperty(@PathVariable Long propertyId,
                                    @RequestBody String content,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        log.info("Updating property for user and propertyId: {} {}", userName, propertyId);
        return propertyService.updatePropertyForUser(propertyId, content, userName);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'TENANT', 'ADMIN', 'BROKER')")
    public List<Property> getPropertyByOwner(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        log.info("Getting properties for user: {}", userName);
        return propertyService.getPropertyByOwnerUserName(userName);
    }

    @DeleteMapping("/{propertyId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public void deleteProperty(@PathVariable Long propertyId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        log.info("Deleting property for user and propertyId: {} {}", userName, propertyId);
        propertyService.deletePropertyForUser(propertyId, userName);
    }
}
