package org.domain;

public interface Factory<E, eType, DTO> {
    E create(eType type, DTO dto);

}
