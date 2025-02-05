package kz.sayat.diploma_backend.mapper.implementation;

import kz.sayat.diploma_backend.mapper.ModuleMapper;
import kz.sayat.diploma_backend.models.Module;
import kz.sayat.diploma_backend.dto.ModuleDto;
import kz.sayat.diploma_backend.models.Course;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapperImpl implements ModuleMapper {

    @Override
    public Module toModule(ModuleDto dto) {
        if (dto == null) {
            return null;
        }

        Module module = new Module();
        module.setTitle(dto.getTitle());

        return module;
    }

    @Override
    public ModuleDto toModuleDto(Module module) {
        if (module == null) {
            return null;
        }

        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setCourseId(module.getCourse().getId());

        return dto;
    }
}
