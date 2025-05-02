package rut.miit.tech.web.domain.util.convertor;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DtoConverter {
    private final ModelMapper modelMapper;

    public <M, D> D toDto(M model, Class<D> destination) {
        return modelMapper.map(model, destination);
    }

    public <M, D> List<D> toDto(List<M> models, Class<D> destination) {
        return models.stream().map(o -> modelMapper.map(o, destination)).toList();
    }

    public <M, D> M toModel(D dto, Class<M> destination) {
        return modelMapper.map(dto, destination);
    }
}
