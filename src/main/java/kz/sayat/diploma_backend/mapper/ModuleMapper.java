package kz.sayat.diploma_backend.mapper;
import kz.sayat.diploma_backend.models.Module;
import kz.sayat.diploma_backend.dto.ModuleDto;

public interface ModuleMapper {
     Module toModule(ModuleDto dto);
     ModuleDto toModuleDto(Module module);
}
