package com.psk.becalm.transport.converters;

abstract class DtoConverter<ENTITY, DATACLASS> {
    protected abstract ENTITY convertToEntity(DATACLASS dataclass);

    protected abstract DATACLASS convertToDto(ENTITY entity);

}
