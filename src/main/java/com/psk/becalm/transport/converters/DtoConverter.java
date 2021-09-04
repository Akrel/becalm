package com.psk.becalm.transport.converters;

import javax.persistence.MappedSuperclass;

abstract class DtoConverter<ENTITY, DATACLASS> {
    protected abstract ENTITY convertToEntity(DATACLASS dataclass);
    protected abstract DATACLASS convertToDto(ENTITY entity);

}
