package com.secure.homefinitybackend.dto;

public record PropertyResponse(
        Long id,
        String content,
        String ownerUserName
) {}
